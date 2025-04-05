package models;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Payroll {
    private int payrollId;
    private int employeeId;
    private LocalDate payPeriodStart;
    private LocalDate payPeriodEnd;
    private BigDecimal basicSalary;
    private BigDecimal allowances;
    private BigDecimal deductions;
    private BigDecimal netSalary;
    private LocalDate paymentDate;
    private String paymentStatus; // Pending, Paid, Cancelled
    private String paymentMethod; // Bank Transfer, Cash, Cheque
    private String bankAccountNumber;
    private String bankName;
    
    // Constructors
    public Payroll() {}
    
    public Payroll(int payrollId, int employeeId, LocalDate payPeriodStart, 
                  LocalDate payPeriodEnd, BigDecimal basicSalary, 
                  BigDecimal allowances, BigDecimal deductions, 
                  LocalDate paymentDate, String paymentStatus, 
                  String paymentMethod, String bankAccountNumber, String bankName) {
        this.payrollId = payrollId;
        this.employeeId = employeeId;
        this.payPeriodStart = payPeriodStart;
        this.payPeriodEnd = payPeriodEnd;
        this.basicSalary = basicSalary;
        this.allowances = allowances;
        this.deductions = deductions;
        this.paymentDate = paymentDate;
        this.paymentStatus = paymentStatus;
        this.paymentMethod = paymentMethod;
        this.bankAccountNumber = bankAccountNumber;
        this.bankName = bankName;
        calculateNetSalary();
    }
    
    // Calculate net salary
    private void calculateNetSalary() {
        this.netSalary = basicSalary.add(allowances).subtract(deductions);
    }
    
    // Getters and Setters
    public int getPayrollId() { return payrollId; }
    public void setPayrollId(int payrollId) { this.payrollId = payrollId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public LocalDate getPayPeriodStart() { return payPeriodStart; }
    public void setPayPeriodStart(LocalDate payPeriodStart) { this.payPeriodStart = payPeriodStart; }
    
    public LocalDate getPayPeriodEnd() { return payPeriodEnd; }
    public void setPayPeriodEnd(LocalDate payPeriodEnd) { this.payPeriodEnd = payPeriodEnd; }
    
    public BigDecimal getBasicSalary() { return basicSalary; }
    public void setBasicSalary(BigDecimal basicSalary) { 
        this.basicSalary = basicSalary;
        calculateNetSalary();
    }
    
    public BigDecimal getAllowances() { return allowances; }
    public void setAllowances(BigDecimal allowances) { 
        this.allowances = allowances;
        calculateNetSalary();
    }
    
    public BigDecimal getDeductions() { return deductions; }
    public void setDeductions(BigDecimal deductions) { 
        this.deductions = deductions;
        calculateNetSalary();
    }
    
    public BigDecimal getNetSalary() { return netSalary; }
    
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { 
        this.bankAccountNumber = bankAccountNumber; 
    }
    
    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }
}
