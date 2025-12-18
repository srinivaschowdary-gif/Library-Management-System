package com.example.LibraryManagementSystem.BookController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.LibraryManagementSystem.BookRepository.UserRepository;
import com.example.LibraryManagementSystem.book.User;

import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@CrossOrigin(origins = "*")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    // GET profile by user id
    @GetMapping("/{id}")
    public ResponseEntity<User> getProfile(@PathVariable String id) {
        Optional<User> u = userRepository.findById(id);
        return u.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // UPDATE profile (username, email, passwordHash). For password you should hash in production.
    @PutMapping("/{id}")
    public ResponseEntity<User> updateProfile(@PathVariable String id, @RequestBody User payload) {
        return userRepository.findById(id).map(user -> {
            if (payload.getUsername() != null) user.setUsername(payload.getUsername());
            if (payload.getEmail() != null) user.setEmail(payload.getEmail());
            if (payload.getPasswordHash() != null && !payload.getPasswordHash().isEmpty()) {
                // In production: hash the password using BCrypt before saving
                user.setPasswordHash(payload.getPasswordHash());
            }
            userRepository.save(user);
            return ResponseEntity.ok(user);
        }).orElse(ResponseEntity.notFound().build());
    }
}
