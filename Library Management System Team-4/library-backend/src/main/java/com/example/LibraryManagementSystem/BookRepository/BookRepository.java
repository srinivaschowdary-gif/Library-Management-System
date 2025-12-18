package com.example.LibraryManagementSystem.BookRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.LibraryManagementSystem.book.Book;

public interface BookRepository extends MongoRepository<Book, String> {}
