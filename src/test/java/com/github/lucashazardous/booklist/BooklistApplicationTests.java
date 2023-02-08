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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class BooklistApplicationTests {
    private final List<NameValuePair> bookValues;

    {
        bookValues = new ArrayList<>();
        bookValues.add(new BasicNameValuePair("id", "test"));
        bookValues.add(new BasicNameValuePair("title", "Test"));
        bookValues.add(new BasicNameValuePair("author", "Test"));
        bookValues.add(new BasicNameValuePair("pages", "100"));
        bookValues.add(new BasicNameValuePair("currentPage", "1"));
    }

    @Autowired
    private BookRepository bookRepository;


    @Test
    public void bookCreation() throws IOException, AssertionError {
        HttpUriRequest request = RequestBuilder.create("POST")
                .setUri("http://localhost:8080/add-book")
                .setEntity(new UrlEncodedFormEntity(bookValues))
                .build();

        HttpResponse httpResponse = HttpClientBuilder.create().build().execute(request);

        assert bookRepository.findById("test").isPresent();
        assert httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY;
    }
}
