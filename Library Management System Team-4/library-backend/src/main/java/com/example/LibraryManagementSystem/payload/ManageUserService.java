package com.example.LibraryManagementSystem.payload;

import com.example.LibraryManagementSystem.book.ManageUser;
import com.example.LibraryManagementSystem.BookRepository.ManageUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ManageUserService {

    private final ManageUserRepository repo;

    public ManageUserService(ManageUserRepository repo) {
        this.repo = repo;
    }

    public List<ManageUser> getAll(String q) {
        if (q == null || q.isBlank()) {
            return repo.findAll();
        }	
        return repo.findByNameContainingIgnoreCaseOrRoleContainingIgnoreCase(q, q);
    }

    public Optional<ManageUser> getById(String id) {
        return repo.findById(id);
    }

    public ManageUser create(ManageUser user) {
        return repo.save(user);
    }

    public ManageUser update(String id, ManageUser updated) {
        return repo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setRole(updated.getRole());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(String id) {
        repo.deleteById(id);
    }
}
