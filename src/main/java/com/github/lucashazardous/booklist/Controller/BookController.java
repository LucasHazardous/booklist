package com.github.lucashazardous.booklist.Controller;

import com.github.lucashazardous.booklist.Model.Book;
import com.github.lucashazardous.booklist.Repository.BookRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @PostMapping("/search")
    public String searchBook(@RequestParam("term") String term, Model model) {
        model.addAttribute("books", this.bookRepository.findAllByTitleContainsIgnoreCaseOrAuthorContainsIgnoreCase(term, term));
        model.addAttribute("term", term);
        return "index";
    }

    @PostMapping("/add-book")
    public String addBook(Book book, BindingResult result) {
        if(Objects.equals(book.getTitle(), "")) result.rejectValue("title", "", "Title can't be empty.");
        if(Objects.equals(book.getAuthor(), "")) result.rejectValue("author", "", "Author can't be empty.");
        if(book.getPages() <= 0) result.rejectValue("pages", "", "Book must have at least one page.");

        if (result.hasErrors()) {
            return "add-book";
        }
        this.bookRepository.save(book);
        return "redirect:/";
    }
}
