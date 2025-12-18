package com.example.LibraryManagementSystem.BookRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.LibraryManagementSystem.book.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
	List<User> findByUsernameContainingIgnoreCaseOrRoleContainingIgnoreCase(String q, String q2);
}

