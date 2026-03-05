package com.example.LibraryManagementSystem.BookController;

import com.example.LibraryManagementSystem.book.User;
import com.example.LibraryManagementSystem.BookRepository.UserRepository;
import com.example.LibraryManagementSystem.payload.*;

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

    //  REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "Email already exists"));
        }

        String role = request.getRole().trim();

        if (!role.equalsIgnoreCase("Student") &&
            !role.equalsIgnoreCase("Librarian")) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(false, "Invalid role"));
        }

        String hashed = BCrypt.hashpw(
                request.getPassword(),
                BCrypt.gensalt(12)
        );

        User user = new User(
                request.getUsername(),
                request.getEmail(),
                hashed,
                role
        );

        userRepository.save(user);

        return ResponseEntity.ok(new AuthResponse(
                true,
                "Registered successfully",
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getId()
        ));
    }

    //  LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {

        Optional<User> userOpt = userRepository.findByEmail(request.getEmail());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (BCrypt.checkpw(request.getPassword(), user.getPasswordHash())) {
                return ResponseEntity.ok(new AuthResponse(
                        true,
                        "Login successful",
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole(),
                        user.getId()
                ));
            }
        }

        return ResponseEntity.badRequest()
                .body(new AuthResponse(false, "Invalid email or password"));
    }
}
