package com.github.lucashazardous.booklist;

import com.github.lucashazardous.booklist.Repository.BookRepository;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "/application.properties",
        properties = "spring.data.mongodb.host=localhost")
class BooklistApplicationTests {
    private final List<NameValuePair> bookValues;
    private final String BOOK_ID = "test";
    private final String BOOK_TITLE = "Test";

    {
        bookValues = new ArrayList<>();
        bookValues.add(new BasicNameValuePair("id", BOOK_ID));
        bookValues.add(new BasicNameValuePair("title", BOOK_TITLE));
        bookValues.add(new BasicNameValuePair("author", "Test"));
        bookValues.add(new BasicNameValuePair("pages", "100"));
        bookValues.add(new BasicNameValuePair("currentPage", "1"));
    }

    @Autowired
    private BookRepository bookRepository;

    @Test
    @Order(1)
    public void bookCreation() throws IOException, AssertionError {
        HttpUriRequest request = RequestBuilder.create("POST")
                .setUri("http://localhost:8080/add-book")
                .setEntity(new UrlEncodedFormEntity(bookValues))
                .build();

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assert bookRepository.findById(BOOK_ID).isPresent();
        assert httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY;
    }

    @Test
    @Order(2)
    public void bookModification() throws IOException, AssertionError {
        String newBookTitle = BOOK_TITLE + " 2";

        bookValues.remove(1);
        bookValues.add(1, new BasicNameValuePair("title", newBookTitle));

        HttpUriRequest request = RequestBuilder.create("POST")
                .setUri("http://localhost:8080/edit-book/" + BOOK_ID)
                .setEntity(new UrlEncodedFormEntity(bookValues))
                .build();

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assert bookRepository.findById(BOOK_ID).isPresent() && bookRepository.findById(BOOK_ID).get().getTitle().equals(newBookTitle);
        assert httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY;
    }

    @Test
    @Order(3)
    public void bookSearch() throws AssertionError {
        var result = this.bookRepository.findAllByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(BOOK_TITLE, BOOK_TITLE);

        assert result.size() > 0;
    }

    @Test
    @Order(4)
    public void bookDeletion() throws IOException, AssertionError {
        HttpUriRequest request = RequestBuilder.create("DELETE")
                .setUri("http://localhost:8080/delete/" + BOOK_ID)
                .build();

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assert bookRepository.findById(BOOK_ID).isEmpty();
        assert httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK;
    }
}
