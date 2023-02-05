package com.github.lucashazardous.booklist.Model;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Document
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @MongoId
    private String id;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public int getPages() {
        return pages;
    }

    public String getTitle() {
        return title;
    }

    public boolean isFinished() {
        return finished;
    }


    private String title;
    private String author;
    private int pages;
    private int currentPage;
    private boolean finished;
    private Date addedDate;
}
