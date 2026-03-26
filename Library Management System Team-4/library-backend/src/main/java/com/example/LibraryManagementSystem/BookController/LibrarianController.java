package com.example.LibraryManagementSystem.BookController;

import org.springframework.web.bind.annotation.*;

import com.example.LibraryManagementSystem.book.Book;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class LibrarianController {

 // POST /api/management/books (or /api/books if shared)
@PostMapping("/api/books")
public ResponseEntity<?> addBook(@RequestBody Book book) {
     // Logic to save a new book (only accessible by Librarians)
    return ResponseEntity.ok("Book added successfully (Librarian only)");
}

 // PUT /api/management/books/{id} (or /api/books/{id})
@PutMapping("/api/books/{id}")
public ResponseEntity<?> updateBook(@PathVariable String id, @RequestBody Book bookDetails) {
     // Logic to update an existing book

    return ResponseEntity.ok("Book updated successfully (Librarian only)");
}

 // DELETE /api/management/books/{id} (or /api/books/{id})
@DeleteMapping("/api/books/{id}")
public ResponseEntity<?> deleteBook(@PathVariable String id) {
     // Logic to delete a book
    return ResponseEntity.ok("Book deleted successfully (Librarian only)");
}

 // GET /api/management/users (or /api/users if shared)
@GetMapping("/users")
public ResponseEntity<?> getAllUsers() {

    return ResponseEntity.ok("List of all users (Librarian only)");
}

 // GET /api/management/transactions (or /api/transactions if shared)
@GetMapping("/transactions")
public ResponseEntity<?> getAllTransactions() {
     // Logic to fetch all transactions
    return ResponseEntity.ok("List of all transactions (Librarian only)");
    }
}