package com.example.LibraryManagementSystem.BookController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.LibraryManagementSystem.book.Loan;
import com.example.LibraryManagementSystem.book.Book;
import com.example.LibraryManagementSystem.BookRepository.LoanRepository;
import com.example.LibraryManagementSystem.BookRepository.BookRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/loans")
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private BookRepository bookRepository;

    // ✅ Get all active (not returned) loans for a user
    @GetMapping("/user/{userId}")
    public List<Loan> getLoansByUser(@PathVariable String userId) {
        return loanRepository.findByUserIdAndReturnedFalse(userId);
    }

    // ✅ Return book: mark loan returned + increment book copies
    @PutMapping("/{loanId}/return")
    public ResponseEntity<ReturnResponse> markAsReturned(@PathVariable String loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            return ResponseEntity.badRequest().body(new ReturnResponse(false, "Loan not found"));
        }

        Loan loan = loanOpt.get();
        if (loan.isReturned()) {
            return ResponseEntity.badRequest().body(new ReturnResponse(false, "Book already returned"));
        }

        // Mark the loan as returned
        loan.setReturned(true);
        loanRepository.save(loan);

        // Increment copies on the associated book
        Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());
        if (bookOpt.isPresent()) {
            Book book = bookOpt.get();
            book.setCopies(book.getCopies() + 1);
            bookRepository.save(book);
        } else {
            // Book missing — still return success for the loan
            return ResponseEntity.ok(
                new ReturnResponse(true, "Loan marked returned; book not found to increment copies")
            );
        }

        return ResponseEntity.ok(new ReturnResponse(true, "Book returned successfully"));
    }

    // ✅ Simple response DTO for frontend feedback
    static class ReturnResponse {
        public boolean success;
        public String message;

        public ReturnResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
