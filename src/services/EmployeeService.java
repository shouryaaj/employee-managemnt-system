package services;

import models.Employee;
import utils.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmployeeService {
    private Connection connection;
    
    public EmployeeService() {
        this.connection = DBConnection.getConnection();
    }
    
    public boolean addEmployee(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, email, phone, address, " +
                    "department, position, start_date, emergency_contact_name, " +
                    "emergency_contact_phone, leave_balance) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getAddress());
            stmt.setString(6, employee.getDepartment());
            stmt.setString(7, employee.getPosition());
            stmt.setDate(8, Date.valueOf(employee.getStartDate()));
            stmt.setString(9, employee.getEmergencyContactName());
            stmt.setString(10, employee.getEmergencyContactPhone());
            stmt.setInt(11, employee.getLeaveBalance());
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    employee.setEmployeeId(rs.getInt(1));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public Employee getEmployee(int employeeId) {
        String sql = "SELECT * FROM employees WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToEmployee(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees ORDER BY first_name, last_name";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
    public boolean updateEmployee(Employee employee) {
        String sql = "UPDATE employees SET first_name = ?, last_name = ?, email = ?, " +
                    "phone = ?, address = ?, department = ?, position = ?, " +
                    "start_date = ?, emergency_contact_name = ?, " +
                    "emergency_contact_phone = ?, leave_balance = ? " +
                    "WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, employee.getFirstName());
            stmt.setString(2, employee.getLastName());
            stmt.setString(3, employee.getEmail());
            stmt.setString(4, employee.getPhone());
            stmt.setString(5, employee.getAddress());
            stmt.setString(6, employee.getDepartment());
            stmt.setString(7, employee.getPosition());
            stmt.setDate(8, Date.valueOf(employee.getStartDate()));
            stmt.setString(9, employee.getEmergencyContactName());
            stmt.setString(10, employee.getEmergencyContactPhone());
            stmt.setInt(11, employee.getLeaveBalance());
            stmt.setInt(12, employee.getEmployeeId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean deleteEmployee(int employeeId) {
        String sql = "DELETE FROM employees WHERE employee_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Employee> searchEmployees(String keyword) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees WHERE first_name LIKE ? OR last_name LIKE ? " +
                    "OR email LIKE ? OR department LIKE ? OR position LIKE ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++) {
                stmt.setString(i, searchPattern);
            }
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                employees.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }
    
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        Employee employee = new Employee();
        employee.setEmployeeId(rs.getInt("employee_id"));
        employee.setFirstName(rs.getString("first_name"));
        employee.setLastName(rs.getString("last_name"));
        employee.setEmail(rs.getString("email"));
        employee.setPhone(rs.getString("phone"));
        employee.setAddress(rs.getString("address"));
        employee.setDepartment(rs.getString("department"));
        employee.setPosition(rs.getString("position"));
        employee.setStartDate(rs.getDate("start_date").toLocalDate());
        employee.setEmergencyContactName(rs.getString("emergency_contact_name"));
        employee.setEmergencyContactPhone(rs.getString("emergency_contact_phone"));
        employee.setLeaveBalance(rs.getInt("leave_balance"));
        employee.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        employee.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        return employee;
    }
}
