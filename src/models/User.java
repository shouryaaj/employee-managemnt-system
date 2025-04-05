package models;

import java.time.LocalDateTime;

public class User {
    private int userId;
    private String username;
    private String password;
    private String salt;
    private String email;
    private String role; // ADMIN, HR, EMPLOYEE
    private boolean isActive;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public User() {}
    
    public User(int userId, String username, String password, String salt, 
                String email, String role, boolean isActive) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.salt = salt;
        this.email = email;
        this.role = role;
        this.isActive = isActive;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getSalt() { return salt; }
    public void setSalt(String salt) { this.salt = salt; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Helper methods for role-based access control
    public boolean isAdmin() {
        return "ADMIN".equals(role);
    }
    
    public boolean isHR() {
        return "HR".equals(role);
    }
    
    public boolean isEmployee() {
        return "EMPLOYEE".equals(role);
    }
    
    public boolean hasAccess(String requiredRole) {
        if (isAdmin()) return true;
        if (isHR() && !"ADMIN".equals(requiredRole)) return true;
        return role.equals(requiredRole);
    }
}
