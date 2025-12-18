package com.example.LibraryManagementSystem.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;

@Document(collection = "manageusers")
public class ManageUser {

    @Id
    private String id;
    private String name;
    private String role;
    private Instant createdAt;

    public ManageUser() {
        this.createdAt = Instant.now();
    }

    public ManageUser(String name, String role) {
        this.name = name;
        this.role = role;
        this.createdAt = Instant.now();
    }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
