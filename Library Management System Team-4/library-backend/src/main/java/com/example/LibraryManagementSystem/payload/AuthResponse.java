package com.example.LibraryManagementSystem.payload;

public class AuthResponse {
    private boolean success;
    private String message;
    private String username;
    private String email;
    private String role;
    private String id;

    public AuthResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public AuthResponse(boolean success, String message, String username, String email, String role, String id) {
        this.success = success;
        this.message = message;
        this.username = username;
        this.email = email;
        this.role = role;
        this.id = id;
    }

    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
}
