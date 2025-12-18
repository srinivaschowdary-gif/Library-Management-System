package com.example.LibraryManagementSystem.payload;

import com.example.LibraryManagementSystem.book.Transaction;
import com.example.LibraryManagementSystem.book.Book;
import com.example.LibraryManagementSystem.book.User;
// Necessary imports
import com.example.LibraryManagementSystem.book.Librarian; 
import com.example.LibraryManagementSystem.BookRepository.LibrarianRepository; 
import com.example.LibraryManagementSystem.BookRepository.TransactionRepository;
import com.example.LibraryManagementSystem.BookRepository.BookRepository;
import com.example.LibraryManagementSystem.BookRepository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final BookRepository bookRepo;
    private final UserRepository userRepo;
    // Inject the Librarian repository
    private final LibrarianRepository librarianRepo; 

    private static final double FINE_PER_DAY = 5.0;

    // UPDATED CONSTRUCTOR: Must include LibrarianRepository
    public TransactionService(TransactionRepository transactionRepo, BookRepository bookRepo, 
                              UserRepository userRepo, LibrarianRepository librarianRepo) {
        this.transactionRepo = transactionRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
        this.librarianRepo = librarianRepo;
    }

    public List<Transaction> getAll() {
        List<Transaction> list = transactionRepo.findAll();
        // Populate display names and compute fine
        list.forEach(this::populateNames);
        list.forEach(this::calculateFine);
        return list;
    }

    public Optional<Transaction> getById(String id) {
        Optional<Transaction> opt = transactionRepo.findById(id);
        opt.ifPresent(t -> {
            populateNames(t);
            calculateFine(t);
        });
        return opt;
    }

    public Transaction add(Transaction t) {
        Book book = bookRepo.findById(t.getBook()).orElseThrow(() -> new RuntimeException("Book not found"));

        // FIX 1: Consolidated lookup for borrower
        String borrowerName = findBorrowerNameById(t.getBorrower());
        if (borrowerName == null) {
            // This is the error message seen in your screenshot 
            throw new RuntimeException("Error issuing book: User not found with ID " + t.getBorrower()); 
        }
        
        if (book.getCopies() <= 0) {
            throw new RuntimeException("No copies available");
        }

        // Update copies
        book.setCopies(book.getCopies() - 1);
        bookRepo.save(book);

        // Set names for display
        t.setBookTitle(book.getTitle());
        t.setBorrowerName(borrowerName); // Set the name from the lookup
        t.setFine(0.0);

        return transactionRepo.save(t);
    }

    public Transaction update(String id, Transaction t) {
        return transactionRepo.findById(id)
                .map(existing -> {
                    existing.setBook(t.getBook());
                    existing.setBorrower(t.getBorrower());
                    existing.setIssueDate(t.getIssueDate());
                    existing.setDueDate(t.getDueDate());
                    populateNames(existing);
                    calculateFine(existing);
                    return transactionRepo.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public void delete(String id) {
        transactionRepo.findById(id).ifPresent(transaction -> {
            bookRepo.findById(transaction.getBook()).ifPresent(book -> {
                book.setCopies(book.getCopies() + 1);
                bookRepo.save(book);
            });
        });
        transactionRepo.deleteById(id);
    }

    // MODIFIED HELPER: Uses consolidated lookup for table display
    private void populateNames(Transaction t) {
        if (t.getBook() != null) {
            bookRepo.findById(t.getBook()).ifPresent(b -> t.setBookTitle(b.getTitle()));
        }
        if (t.getBorrower() != null) {
            String borrowerName = findBorrowerNameById(t.getBorrower());
            // This is crucial for displaying the name in the table
            if (borrowerName != null) {
                t.setBorrowerName(borrowerName);
            } else {
                // If the name is not found, keep the borrower ID in borrowerName temporarily 
                // so the front-end has something, though the ID should usually be used.
                // However, the front-end fallback logic will handle it: ${t.borrowerName || t.borrower}
                t.setBorrowerName(null);
            }
        }
    }
    
    // CONSOLIDATED LOOKUP FUNCTION (Checks both User and Librarian)
    private String findBorrowerNameById(String id) {
        // 1. Check Student (User) collection
        Optional<User> userOpt = userRepo.findById(id);
        if (userOpt.isPresent()) {
            String name = userOpt.get().getName();
            // Return only if the name is not null or empty
            if (name != null && !name.trim().isEmpty()) return name;
        }

        // 2. Check Librarian collection
        Optional<Librarian> librarianOpt = librarianRepo.findById(id);
        if (librarianOpt.isPresent()) {
            String name = librarianOpt.get().getName();
            // Return only if the name is not null or empty
            if (name != null && !name.trim().isEmpty()) return name;
        }
        
        return null; // ID not found in either collection, or name/username field is empty
    }


    private void calculateFine(Transaction t) {
        LocalDate due = t.getDueDate();
        if (due != null && LocalDate.now().isAfter(due)) {
            long daysLate = ChronoUnit.DAYS.between(due, LocalDate.now());
            t.setFine(daysLate * FINE_PER_DAY);
        } else {
            t.setFine(0.0);
        }
    }
}