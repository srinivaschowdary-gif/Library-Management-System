package com.example.LibraryManagementSystem.BookRepository;

//src/main/java/com/yourpackage/repository/LibrarianRepository.java

import com.example.LibraryManagementSystem.book.Librarian;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface LibrarianRepository extends MongoRepository<Librarian, String> {
 
 // Method to find a Librarian by email for login/checks
 Optional<Librarian> findByEmail(String email);
}