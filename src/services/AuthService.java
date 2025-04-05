package services;

import models.User;
import utils.DBConnection;
import utils.EncryptionUtil;
import java.sql.*;
import java.time.LocalDateTime;

public class AuthService {
    private Connection connection;
    
    public AuthService() {
        this.connection = DBConnection.getConnection();
    }
    
    public boolean login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND is_active = true";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String salt = rs.getString("salt");
                
                // Verify password
                if (EncryptionUtil.verifyPassword(password, salt, storedPassword)) {
                    // Update last login time
                    updateLastLogin(rs.getInt("user_id"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean register(User user) {
        // Check if username already exists
        if (getUserByUsername(user.getUsername()) != null) {
            return false;
        }
        
        String sql = "INSERT INTO users (username, password, salt, email, role, is_active) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            // Generate salt and hash password
            String salt = EncryptionUtil.generateSalt();
            String hashedPassword = EncryptionUtil.hashPassword(user.getPassword(), salt);
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, hashedPassword);
            stmt.setString(3, salt);
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getRole());
            stmt.setBoolean(6, user.isActive());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean changePassword(int userId, String oldPassword, String newPassword) {
        String sql = "SELECT password, salt FROM users WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String storedPassword = rs.getString("password");
                String salt = rs.getString("salt");
                
                // Verify old password
                if (EncryptionUtil.verifyPassword(oldPassword, salt, storedPassword)) {
                    // Generate new salt and hash new password
                    String newSalt = EncryptionUtil.generateSalt();
                    String newHashedPassword = EncryptionUtil.hashPassword(newPassword, newSalt);
                    
                    // Update password and salt
                    return updatePassword(userId, newHashedPassword, newSalt);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean resetPassword(String username) {
        // Generate a random password
        String newPassword = generateRandomPassword();
        String salt = EncryptionUtil.generateSalt();
        String hashedPassword = EncryptionUtil.hashPassword(newPassword, salt);
        
        String sql = "UPDATE users SET password = ?, salt = ? WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, salt);
            stmt.setString(3, username);
            
            if (stmt.executeUpdate() > 0) {
                // In a real application, send the new password via email
                System.out.println("New password for " + username + ": " + newPassword);
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    private void updateLastLogin(int userId) {
        String sql = "UPDATE users SET last_login = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private boolean updatePassword(int userId, String hashedPassword, String salt) {
        String sql = "UPDATE users SET password = ?, salt = ? WHERE user_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setString(2, salt);
            stmt.setInt(3, userId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private String generateRandomPassword() {
        // Simple random password generator
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();
        java.security.SecureRandom random = new java.security.SecureRandom();
        
        for (int i = 0; i < 12; i++) {
            int index = random.nextInt(chars.length());
            password.append(chars.charAt(index));
        }
        
        return password.toString();
    }
    
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setSalt(rs.getString("salt"));
        user.setRole(rs.getString("role"));
        user.setActive(rs.getBoolean("is_active"));
        
        Timestamp lastLogin = rs.getTimestamp("last_login");
        if (lastLogin != null) {
            user.setLastLogin(lastLogin.toLocalDateTime());
        }
        
        user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        
        return user;
    }
}
