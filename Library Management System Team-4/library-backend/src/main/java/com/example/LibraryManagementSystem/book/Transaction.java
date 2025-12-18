package com.example.LibraryManagementSystem.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;
    private String book;        // Book ID
    private String borrower;    // User ID
    private String bookTitle;   // For display (populated server-side)
    private Object borrowerName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate issueDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    private double fine;

    public Transaction() {}

    public Transaction(String book, String borrower, LocalDate issueDate, LocalDate dueDate) {
        this.book = book;
        this.borrower = borrower;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.fine = 0.0;
    }

    // Getters & Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBook() { return book; }
    public void setBook(String book) { this.book = book; }

    public String getBorrower() { return borrower; }
    public void setBorrower(String borrower) { this.borrower = borrower; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public Object getBorrowerName() { return borrowerName; }
    public void setBorrowerName(Object borrowerName2) { this.borrowerName = borrowerName2; }

    public LocalDate getIssueDate() { return issueDate; }
    public void setIssueDate(LocalDate issueDate) { this.issueDate = issueDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public double getFine() { return fine; }
    public void setFine(double fine) { this.fine = fine; }
}
