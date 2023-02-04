package com.github.lucashazardous.booklist.Controller;

import com.github.lucashazardous.booklist.Model.Book;
import com.github.lucashazardous.booklist.Repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Objects;

@Controller
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("books", this.bookRepository.findAll());
        return "index";
    }

    @GetMapping("/add-book")
    public String showAddBook(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }


    @PostMapping("/add-book")
    public String addBook(Book book, BindingResult result) {
        if(Objects.equals(book.getTitle(), "")) result.rejectValue("title", "", "Title can't be empty.");
        if (result.hasErrors()) {
            return "add-book";
        }
        this.bookRepository.save(book);
        return "redirect:/";
    }
}