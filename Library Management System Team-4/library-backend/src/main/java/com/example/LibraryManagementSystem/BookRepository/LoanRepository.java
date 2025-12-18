package com.example.LibraryManagementSystem.BookRepository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.LibraryManagementSystem.book.Loan;

public interface LoanRepository extends MongoRepository<Loan, String> {
    List<Loan> findByUserIdAndReturnedFalse(String userId);
}
