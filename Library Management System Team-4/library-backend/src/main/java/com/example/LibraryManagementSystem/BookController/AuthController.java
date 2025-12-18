package com.example.LibraryManagementSystem.BookController;

import com.example.LibraryManagementSystem.book.User;
import com.example.LibraryManagementSystem.book.Librarian;
import com.example.LibraryManagementSystem.BookRepository.UserRepository;
import com.example.LibraryManagementSystem.BookRepository.LibrarianRepository;
import com.example.LibraryManagementSystem.payload.AuthRequest;
import com.example.LibraryManagementSystem.payload.AuthResponse;
import com.example.LibraryManagementSystem.payload.RegisterRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibrarianRepository librarianRepository;

    // ✅ REGISTER ENDPOINT (for both Student and Librarian)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        String role = registerRequest.getRole();

        // Check if email already exists in either collection
        if (userRepository.existsByEmail(registerRequest.getEmail()) ||
            librarianRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body(
                    new AuthResponse(false, "Email is already in use")
            );
        }

        String hashed = BCrypt.hashpw(registerRequest.getPassword(), BCrypt.gensalt(12));

        if ("Librarian".equalsIgnoreCase(role)) {
            Librarian librarian = new Librarian(
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    hashed,
                    "Librarian"
            );
            librarianRepository.save(librarian);
            return ResponseEntity.ok(new AuthResponse(true,
                    "Librarian registered successfully",
                    librarian.getUsername(),
                    librarian.getEmail(),
                    librarian.getRole(),
                    librarian.getId()));
        } else {
            User user = new User(
                    registerRequest.getUsername(),
                    registerRequest.getEmail(),
                    hashed,
                    "Student"
            );
            userRepository.save(user);
            return ResponseEntity.ok(new AuthResponse(true,
                    "User registered successfully",
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole(),
                    user.getId()));
        }
    }

    // ✅ LOGIN ENDPOINT (checks both collections)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {

        // 1️⃣ Check User collection
        Optional<User> maybeUser = userRepository.findByEmail(authRequest.getEmail());
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            if (BCrypt.checkpw(authRequest.getPassword(), user.getPasswordHash())) {
                return ResponseEntity.ok(new AuthResponse(true,
                        "Login successful",
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getId()));
            }
        }

        // 2️⃣ Check Librarian collection
        Optional<Librarian> maybeLibrarian = librarianRepository.findByEmail(authRequest.getEmail());
        if (maybeLibrarian.isPresent()) {
            Librarian librarian = maybeLibrarian.get();
            if (BCrypt.checkpw(authRequest.getPassword(), librarian.getPassword())) {
                return ResponseEntity.ok(new AuthResponse(true,
                        "Login successful",
                        librarian.getUsername(),
                        librarian.getEmail(),
                        librarian.getRole(),
                        librarian.getId()));
            }
        }

        // ❌ If neither matched
        return ResponseEntity.badRequest()
                .body(new AuthResponse(false, "Invalid email or password"));
    }
}
