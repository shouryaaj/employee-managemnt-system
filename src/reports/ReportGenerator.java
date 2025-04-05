package reports;

import models.Employee;
import models.Attendance;
import models.Payroll;
import services.EmployeeService;
import services.AttendanceService;
import services.PayrollService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ReportGenerator {
    private EmployeeService employeeService;
    private AttendanceService attendanceService;
    private PayrollService payrollService;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public ReportGenerator() {
        this.employeeService = new EmployeeService();
        this.attendanceService = new AttendanceService();
        this.payrollService = new PayrollService();
    }
    
    public boolean generateEmployeeReport(String filePath) {
        List<Employee> employees = employeeService.getAllEmployees();
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=== EMPLOYEE REPORT ===");
            writer.println("Generated on: " + LocalDateTime.now().format(dateTimeFormatter));
            writer.println();
            
            writer.printf("%-5s %-20s %-30s %-15s %-20s %-15s%n", 
                         "ID", "Name", "Email", "Phone", "Department", "Position");
            writer.println("-".repeat(105));
            
            for (Employee employee : employees) {
                writer.printf("%-5d %-20s %-30s %-15s %-20s %-15s%n",
                             employee.getEmployeeId(),
                             employee.getFullName(),
                             employee.getEmail(),
                             employee.getPhone(),
                             employee.getDepartment(),
                             employee.getPosition());
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean generateAttendanceReport(String filePath, LocalDate startDate, LocalDate endDate) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);
        List<Attendance> attendances = attendanceService.getAttendanceByDateRange(startDateTime, endDateTime);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=== ATTENDANCE REPORT ===");
            writer.println("Period: " + startDate.format(dateFormatter) + " to " + endDate.format(dateFormatter));
            writer.println("Generated on: " + LocalDateTime.now().format(dateTimeFormatter));
            writer.println();
            
            writer.printf("%-5s %-20s %-20s %-20s %-15s%n", 
                         "ID", "Employee", "Check In", "Check Out", "Status");
            writer.println("-".repeat(80));
            
            for (Attendance attendance : attendances) {
                Employee employee = employeeService.getEmployee(attendance.getEmployeeId());
                String employeeName = employee != null ? employee.getFullName() : "Unknown";
                
                writer.printf("%-5d %-20s %-20s %-20s %-15s%n",
                             attendance.getAttendanceId(),
                             employeeName,
                             attendance.getCheckIn() != null ? attendance.getCheckIn().format(dateTimeFormatter) : "N/A",
                             attendance.getCheckOut() != null ? attendance.getCheckOut().format(dateTimeFormatter) : "N/A",
                             attendance.getStatus());
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean generatePayrollReport(String filePath, LocalDate startDate, LocalDate endDate) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=== PAYROLL REPORT ===");
            writer.println("Period: " + startDate.format(dateFormatter) + " to " + endDate.format(dateFormatter));
            writer.println("Generated on: " + LocalDateTime.now().format(dateTimeFormatter));
            writer.println();
            
            writer.printf("%-5s %-20s %-15s %-15s %-15s %-15s %-15s%n", 
                         "ID", "Employee", "Basic Salary", "Allowances", "Deductions", "Net Salary", "Status");
            writer.println("-".repeat(100));
            
            List<Employee> employees = employeeService.getAllEmployees();
            for (Employee employee : employees) {
                List<Payroll> payrolls = payrollService.getEmployeePayrollHistory(employee.getEmployeeId());
                for (Payroll payroll : payrolls) {
                    if (!payroll.getPayPeriodStart().isBefore(startDate) && 
                        !payroll.getPayPeriodEnd().isAfter(endDate)) {
                        writer.printf("%-5d %-20s %-15.2f %-15.2f %-15.2f %-15.2f %-15s%n",
                                     payroll.getPayrollId(),
                                     employee.getFullName(),
                                     payroll.getBasicSalary(),
                                     payroll.getAllowances(),
                                     payroll.getDeductions(),
                                     payroll.getNetSalary(),
                                     payroll.getPaymentStatus());
                    }
                }
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean generateLeaveReport(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=== LEAVE REPORT ===");
            writer.println("Generated on: " + LocalDateTime.now().format(dateTimeFormatter));
            writer.println();
            
            writer.printf("%-5s %-20s %-15s %-15s %-15s %-30s%n", 
                         "ID", "Employee", "Leave Date", "Leave Type", "Status", "Reason");
            writer.println("-".repeat(100));
            
            List<Employee> employees = employeeService.getAllEmployees();
            for (Employee employee : employees) {
                List<Attendance> leaves = attendanceService.getLeaveHistory(employee.getEmployeeId());
                for (Attendance leave : leaves) {
                    writer.printf("%-5d %-20s %-15s %-15s %-15s %-30s%n",
                                 leave.getAttendanceId(),
                                 employee.getFullName(),
                                 leave.getLeaveDate().format(dateFormatter),
                                 leave.getLeaveType(),
                                 leave.getStatus(),
                                 leave.getLeaveReason());
                }
            }
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
} 