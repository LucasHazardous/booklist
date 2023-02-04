package com.github.lucashazardous.booklist.Repository;

import com.github.lucashazardous.booklist.Model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

}
