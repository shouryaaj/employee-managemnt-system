package services;

import models.Attendance;
import utils.DBConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AttendanceService {
    private Connection connection;
    
    public AttendanceService() {
        this.connection = DBConnection.getConnection();
    }
    
    public boolean markAttendance(int employeeId, LocalDateTime checkIn) {
        String sql = "INSERT INTO attendance (employee_id, check_in, status) VALUES (?, ?, 'Present')";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            stmt.setTimestamp(2, Timestamp.valueOf(checkIn));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean updateCheckOut(int employeeId, LocalDateTime checkOut) {
        String sql = "UPDATE attendance SET check_out = ? WHERE employee_id = ? " +
                    "AND DATE(check_in) = CURDATE() AND check_out IS NULL";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(checkOut));
            stmt.setInt(2, employeeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean markLeave(int employeeId, LocalDate leaveDate, String leaveType, String leaveReason) {
        // First check if employee has enough leave balance
        String checkBalanceSql = "SELECT leave_balance FROM employees WHERE employee_id = ?";
        String updateBalanceSql = "UPDATE employees SET leave_balance = leave_balance - 1 WHERE employee_id = ?";
        String insertLeaveSql = "INSERT INTO attendance (employee_id, leave_date, leave_type, leave_reason, status) " +
                               "VALUES (?, ?, ?, ?, 'On Leave')";
        
        try {
            connection.setAutoCommit(false);
            
            // Check leave balance
            try (PreparedStatement stmt = connection.prepareStatement(checkBalanceSql)) {
                stmt.setInt(1, employeeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next() && rs.getInt("leave_balance") <= 0) {
                    connection.rollback();
                    return false;
                }
            }
            
            // Update leave balance
            try (PreparedStatement stmt = connection.prepareStatement(updateBalanceSql)) {
                stmt.setInt(1, employeeId);
                stmt.executeUpdate();
            }
            
            // Insert leave record
            try (PreparedStatement stmt = connection.prepareStatement(insertLeaveSql)) {
                stmt.setInt(1, employeeId);
                stmt.setDate(2, Date.valueOf(leaveDate));
                stmt.setString(3, leaveType);
                stmt.setString(4, leaveReason);
                stmt.executeUpdate();
            }
            
            connection.commit();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    public List<Attendance> getEmployeeAttendance(int employeeId) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE employee_id = ? ORDER BY check_in DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }
    
    public List<Attendance> getAttendanceByDateRange(LocalDateTime start, LocalDateTime end) {
        List<Attendance> attendances = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE check_in BETWEEN ? AND ? ORDER BY check_in";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setTimestamp(1, Timestamp.valueOf(start));
            stmt.setTimestamp(2, Timestamp.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                attendances.add(mapResultSetToAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attendances;
    }
    
    public List<Attendance> getLeaveHistory(int employeeId) {
        List<Attendance> leaves = new ArrayList<>();
        String sql = "SELECT * FROM attendance WHERE employee_id = ? AND status = 'On Leave' ORDER BY leave_date DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                leaves.add(mapResultSetToAttendance(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return leaves;
    }
    
    private Attendance mapResultSetToAttendance(ResultSet rs) throws SQLException {
        Attendance attendance = new Attendance();
        attendance.setAttendanceId(rs.getInt("attendance_id"));
        attendance.setEmployeeId(rs.getInt("employee_id"));
        
        Timestamp checkIn = rs.getTimestamp("check_in");
        if (checkIn != null) {
            attendance.setCheckIn(checkIn.toLocalDateTime());
        }
        
        Timestamp checkOut = rs.getTimestamp("check_out");
        if (checkOut != null) {
            attendance.setCheckOut(checkOut.toLocalDateTime());
        }
        
        attendance.setStatus(rs.getString("status"));
        
        if ("On Leave".equals(attendance.getStatus())) {
            attendance.setLeaveType(rs.getString("leave_type"));
            attendance.setLeaveDate(rs.getDate("leave_date").toLocalDate());
            attendance.setLeaveReason(rs.getString("leave_reason"));
        }
        
        return attendance;
    }
}
