package com.example.LibraryManagementSystem.BookRepository;

import com.example.LibraryManagementSystem.book.ManageUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ManageUserRepository extends MongoRepository<ManageUser, String> {

    List<ManageUser> findByNameContainingIgnoreCaseOrRoleContainingIgnoreCase(String name, String role);
}
