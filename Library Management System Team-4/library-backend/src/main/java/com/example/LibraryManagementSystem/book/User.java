package com.example.LibraryManagementSystem.book;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

@Document(collection = "users")
public class User
{
	@Id
	private String id;
	private String username;
	@Indexed(unique = true)
	private String email;

	//  Ensure this field exists for consistency with getName()
    private String name;

    private String passwordHash;
    private String role;

    // Constructors
    public User() {}

    public User(String username, String email, String passwordHash, String role) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;

    }

    // getters & setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUsername(){
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // Getter and Setter for Role
    public String getRole() {
        return role;
    }
    public void setRole(String role) { this.role = role; }

    public String getPassword() { return passwordHash; }

    public String getName() {

        return (name != null && !name.isEmpty()) ? name : username;
    }
    public void setName(String name) {
        this.name = name;
    }
}