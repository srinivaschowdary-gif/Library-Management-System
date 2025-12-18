package com.example.LibraryManagementSystem.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "librarians")
public class Librarian {

	@Id
private String id;
private String username;
private String email;
private String password;
private String role; 


public String getId() { return id; }
public void setId(String id) { this.id = id; }
public String getUsername() { return username; }
public void setUsername(String username) { this.username = username; }
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
public String getPassword() { return password; }
public void setPassword(String password) { this.password = password; }
public String getRole() { return role; }
public void setRole(String role) { this.role = role; }


// Constructors
public Librarian() {}

public Librarian(String username, String email, String password, String role) {
  this.username = username;
  this.email = email;
  this.password = password;
  this.role = role;
}
    
    // CRITICAL FIX: Ensure getName() returns String and uses the display name (username).
    public String getName() {
        return username;
    }
}