package com.example.LibraryManagementSystem.BookController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.LibraryManagementSystem.BookRepository.BookRepository;
import com.example.LibraryManagementSystem.BookRepository.LoanRepository;
import com.example.LibraryManagementSystem.BookRepository.TransactionRepository;
import com.example.LibraryManagementSystem.book.Book;
import com.example.LibraryManagementSystem.book.Loan;
import com.example.LibraryManagementSystem.book.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    // Get all books
    @GetMapping
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    // Get book by ID
    @GetMapping("/{id}")
    public Optional<Book> getBookById(@PathVariable String id) {
        return bookRepository.findById(id);
    }

    // Add new book
    @PostMapping
    public Book addBook(@RequestBody Book book) {
        return bookRepository.save(book);
    }

    // Update book
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable String id, @RequestBody Book updatedBook) {
        return bookRepository.findById(id)
                .map(book -> {
                    book.setTitle(updatedBook.getTitle());
                    book.setAuthor(updatedBook.getAuthor());
                    book.setCopies(updatedBook.getCopies());
                    return bookRepository.save(book);
                })
                .orElseGet(() -> {
                    updatedBook.setId(id);
                    return bookRepository.save(updatedBook);
                });
    }

    // Delete book
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable String id) {
        bookRepository.deleteById(id);
    }

    // ✅ Borrow a book
    @PostMapping("/borrow/{bookId}")
    public Response borrowBook(@PathVariable String bookId, @RequestParam String userId) {
        Optional<Book> opt = bookRepository.findById(bookId);
        if (opt.isEmpty()) return new Response(false, "Book not found");
        Book book = opt.get();

        if (book.getCopies() <= 0) return new Response(false, "No copies available");

        book.setCopies(book.getCopies() - 1);
        bookRepository.save(book);

        Loan loan = new Loan();
        loan.setBookId(bookId);
        loan.setUserId(userId);
        loan.setBorrowedAt(LocalDate.now());
        loan.setDueAt(LocalDate.now().plusWeeks(2));
        loan.setReturned(false);
        loanRepository.save(loan);

        Transaction transaction = new Transaction(
                book.getTitle(),
                userId,
                LocalDate.now(),
                LocalDate.now().plusWeeks(2)
        );
        transactionRepository.save(transaction);

        return new Response(true, "Book borrowed successfully");
    }

    // ✅ Delete transaction (return book)
    @DeleteMapping("/transaction/{transactionId}")
    public Response deleteTransaction(@PathVariable String transactionId) {
        Optional<Transaction> transOpt = transactionRepository.findById(transactionId);
        if (transOpt.isEmpty()) return new Response(false, "Transaction not found");

        Transaction transaction = transOpt.get();
        transactionRepository.deleteById(transactionId);

        // Increase book copies
        List<Book> books = bookRepository.findAll();
        Book targetBook = books.stream()
                .filter(b -> b.getTitle().equalsIgnoreCase(transaction.getBook()))
                .findFirst()
                .orElse(null);

        if (targetBook != null) {
            targetBook.setCopies(targetBook.getCopies() + 1);
            bookRepository.save(targetBook);
        }

        // Mark loan as returned
        List<Loan> loans = loanRepository.findAll();
        loans.stream()
                .filter(l -> l.getUserId().equals(transaction.getBorrower())
                        && targetBook != null
                        && l.getBookId().equals(targetBook.getId())
                        && !l.isReturned())
                .forEach(l -> {
                    l.setReturned(true);
                    loanRepository.save(l);
                });

        return new Response(true, "Transaction deleted and book returned successfully");
    }

    // Get borrowed books
    @GetMapping("/borrowed/{userId}")
    public List<Loan> getBorrowedByUser(@PathVariable String userId) {
        return loanRepository.findByUserIdAndReturnedFalse(userId);
    }

    @GetMapping("/borrowed/details/{userId}")
    public List<BorrowedBookDto> getBorrowedDetails(@PathVariable String userId) {
        List<Loan> loans = loanRepository.findByUserIdAndReturnedFalse(userId);
        return loans.stream()
                .map(loan -> {
                    Book book = bookRepository.findById(loan.getBookId()).orElse(null);
                    if (book == null) return null;
                    return new BorrowedBookDto(loan.getId(), loan.getBookId(), book.getTitle(), loan.getDueAt());
                })
                .filter(dto -> dto != null)
                .collect(Collectors.toList());
    }

    @PostMapping("/return/{loanId}")
    public Response returnBook(@PathVariable String loanId) {
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) return new Response(false, "Loan not found");

        Loan loan = loanOpt.get();
        if (loan.isReturned()) return new Response(false, "Book already returned");

        loan.setReturned(true);
        loanRepository.save(loan);

        Optional<Book> bookOpt = bookRepository.findById(loan.getBookId());
        bookOpt.ifPresent(book -> {
            book.setCopies(book.getCopies() + 1);
            bookRepository.save(book);
        });

        return new Response(true, "Book returned successfully");
    }

    static class BorrowedBookDto {
        public String loanId;
        public String bookId;
        public String title;
        public LocalDate dueAt;
        public BorrowedBookDto(String loanId, String bookId, String title, LocalDate dueAt) {
            this.loanId = loanId;
            this.bookId = bookId;
            this.title = title;
            this.dueAt = dueAt;
        }
    }

    static class Response {
        public boolean success;
        public String message;
        public Response(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
