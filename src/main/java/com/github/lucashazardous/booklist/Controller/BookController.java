package com.github.lucashazardous.booklist.Controller;

import com.github.lucashazardous.booklist.Model.Book;
import com.github.lucashazardous.booklist.Repository.BookRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("books", this.bookRepository.findAll().stream()
                .sorted(Comparator.comparing(Book::getModifiedDate).reversed())
                .collect(Collectors.toList()));
        return "index";
    }

    @GetMapping("/add-book")
    public String showAddBook(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/search")
    public String searchBook(@RequestParam("term") String term, Model model) {
        model.addAttribute("books", this.bookRepository.findAllByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(term, term));
        model.addAttribute("term", term);
        return "index";
    }

    @PostMapping("/add-book")
    public String addBook(Book book, BindingResult result) {
        return saveBookWithCurrentDateOrReject("add-book", book, result);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") String id) {
        this.bookRepository.deleteById(id);
        return "index";
    }

    @GetMapping("/edit-book/{id}")
    public String showEditBook(@PathVariable("id") String id, Model model) {
        model.addAttribute("book", this.bookRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid book id: " + id)));
        return "edit-book";
    }

    @PostMapping("/edit-book/{id}")
    public String editBook(Book book, BindingResult result) {
        return saveBookWithCurrentDateOrReject("edit-book", book, result);
    }

    private String saveBookWithCurrentDateOrReject(String operation, Book book, BindingResult result) {
        book.setModifiedDate(Date.from(Instant.now()));
        bookValidation(book, result);

        if (result.hasErrors())
            return operation;

        try {
            this.bookRepository.save(book);
        } catch (DuplicateKeyException ignored) {
            result.rejectValue("title", "", "Book with this exact title from the same author already exists.");
        }

        if (result.hasErrors())
            return operation;
        
        return "redirect:/";
    }

    private void bookValidation(Book book, BindingResult result) {
        if(book.getTitle() == null || Objects.equals(book.getTitle().strip(), "")) result.rejectValue("title", "", "Title can't be empty.");
        if(book.getAuthor() == null || Objects.equals(book.getAuthor().strip(), "")) result.rejectValue("author", "", "Author can't be empty.");
        if(book.getPages() <= 0) result.rejectValue("pages", "", "Book must have at least one page.");
        if(book.getCurrentPage() < 0 || book.getCurrentPage() > book.getPages()) result.rejectValue("currentPage", "", "Invalid current page.");
    }
}
