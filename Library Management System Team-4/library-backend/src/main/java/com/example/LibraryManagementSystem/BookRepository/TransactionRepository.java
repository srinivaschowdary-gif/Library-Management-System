package com.example.LibraryManagementSystem.BookRepository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.LibraryManagementSystem.book.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {}
