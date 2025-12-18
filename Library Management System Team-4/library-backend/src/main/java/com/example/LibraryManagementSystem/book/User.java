package com.example.LibraryManagementSystem.book;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User
{
	 @Id
	 private String id;
	 private String username;
	 @Indexed(unique = true)
	 private String email;

	// 🛑 Ensure this field exists for consistency with getName()
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
        // Note: The 'name' field is not set here, which could be an issue if it's required for transactions.
        // If 'name' is empty in the database, the transaction service will use 'username' as a fallback if implemented.
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
    
    // 🛑 CRITICAL FIX: Ensure getName() returns String and provides a name.
    public String getName() {
        // Use 'name' if available, otherwise fall back to 'username'
        return (name != null && !name.isEmpty()) ? name : username;
    }
    public void setName(String name) {
        this.name = name;
    }
}