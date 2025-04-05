package services;

import models.Payroll;
import utils.DBConnection;
import java.sql.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PayrollService {
    private Connection connection;
    
    public PayrollService() {
        this.connection = DBConnection.getConnection();
    }
    
    public boolean generatePayroll(int employeeId, LocalDate startDate, LocalDate endDate) {
        // First get employee's basic salary
        String getSalarySql = "SELECT basic_salary FROM employees WHERE employee_id = ?";
        String insertPayrollSql = "INSERT INTO payroll (employee_id, pay_period_start, pay_period_end, " +
                                 "basic_salary, allowances, deductions, net_salary, payment_status) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, 'Pending')";
        
        try {
            connection.setAutoCommit(false);
            
            // Get basic salary
            BigDecimal basicSalary = BigDecimal.ZERO;
            try (PreparedStatement stmt = connection.prepareStatement(getSalarySql)) {
                stmt.setInt(1, employeeId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    basicSalary = rs.getBigDecimal("basic_salary");
                } else {
                    connection.rollback();
                    return false;
                }
            }
            
            // Calculate allowances (example: 10% of basic salary)
            BigDecimal allowances = basicSalary.multiply(new BigDecimal("0.10"));
            
            // Calculate deductions (example: 5% of basic salary)
            BigDecimal deductions = basicSalary.multiply(new BigDecimal("0.05"));
            
            // Calculate net salary
            BigDecimal netSalary = basicSalary.add(allowances).subtract(deductions);
            
            // Insert payroll record
            try (PreparedStatement stmt = connection.prepareStatement(insertPayrollSql)) {
                stmt.setInt(1, employeeId);
                stmt.setDate(2, Date.valueOf(startDate));
                stmt.setDate(3, Date.valueOf(endDate));
                stmt.setBigDecimal(4, basicSalary);
                stmt.setBigDecimal(5, allowances);
                stmt.setBigDecimal(6, deductions);
                stmt.setBigDecimal(7, netSalary);
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
    
    public Payroll getPayroll(int payrollId) {
        String sql = "SELECT * FROM payroll WHERE payroll_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, payrollId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToPayroll(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<Payroll> getEmployeePayrollHistory(int employeeId) {
        List<Payroll> payrolls = new ArrayList<>();
        String sql = "SELECT * FROM payroll WHERE employee_id = ? ORDER BY pay_period_start DESC";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                payrolls.add(mapResultSetToPayroll(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payrolls;
    }
    
    public boolean updatePayroll(Payroll payroll) {
        String sql = "UPDATE payroll SET payment_date = ?, payment_status = ?, " +
                    "payment_method = ?, bank_account_number = ?, bank_name = ? " +
                    "WHERE payroll_id = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, payroll.getPaymentDate() != null ? Date.valueOf(payroll.getPaymentDate()) : null);
            stmt.setString(2, payroll.getPaymentStatus());
            stmt.setString(3, payroll.getPaymentMethod());
            stmt.setString(4, payroll.getBankAccountNumber());
            stmt.setString(5, payroll.getBankName());
            stmt.setInt(6, payroll.getPayrollId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean processPayment(int payrollId, String paymentMethod, String bankAccountNumber, String bankName) {
        String sql = "UPDATE payroll SET payment_date = CURDATE(), payment_status = 'Paid', " +
                    "payment_method = ?, bank_account_number = ?, bank_name = ? " +
                    "WHERE payroll_id = ? AND payment_status = 'Pending'";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paymentMethod);
            stmt.setString(2, bankAccountNumber);
            stmt.setString(3, bankName);
            stmt.setInt(4, payrollId);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    private Payroll mapResultSetToPayroll(ResultSet rs) throws SQLException {
        Payroll payroll = new Payroll();
        payroll.setPayrollId(rs.getInt("payroll_id"));
        payroll.setEmployeeId(rs.getInt("employee_id"));
        payroll.setPayPeriodStart(rs.getDate("pay_period_start").toLocalDate());
        payroll.setPayPeriodEnd(rs.getDate("pay_period_end").toLocalDate());
        payroll.setBasicSalary(rs.getBigDecimal("basic_salary"));
        payroll.setAllowances(rs.getBigDecimal("allowances"));
        payroll.setDeductions(rs.getBigDecimal("deductions"));
        
        Date paymentDate = rs.getDate("payment_date");
        if (paymentDate != null) {
            payroll.setPaymentDate(paymentDate.toLocalDate());
        }
        
        payroll.setPaymentStatus(rs.getString("payment_status"));
        payroll.setPaymentMethod(rs.getString("payment_method"));
        payroll.setBankAccountNumber(rs.getString("bank_account_number"));
        payroll.setBankName(rs.getString("bank_name"));
        
        return payroll;
    }
}
