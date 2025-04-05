package ui;

import services.*;
import models.*;
import java.util.Scanner;
import java.time.LocalDate;
import java.util.List;

public class MainMenu {
    private Scanner scanner;
    private AuthService authService;
    private EmployeeService employeeService;
    private AttendanceService attendanceService;
    private PayrollService payrollService;
    
    public MainMenu() {
        scanner = new Scanner(System.in);
        authService = new AuthService();
        employeeService = new EmployeeService();
        attendanceService = new AttendanceService();
        payrollService = new PayrollService();
    }
    
    public void start() {
        while (true) {
            displayMainMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    handleLogin();
                    break;
                case 2:
                    handleRegister();
                    break;
                case 0:
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void displayMainMenu() {
        System.out.println("\n=== Employee Management System ===");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void handleLogin() {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        if (authService.login(username, password)) {
            System.out.println("Login successful!");
            User currentUser = authService.getUserByUsername(username);
            showLoggedInMenu(currentUser);
        } else {
            System.out.println("Login failed. Please try again.");
        }
    }
    
    private void handleRegister() {
        System.out.println("\n=== User Registration ===");
        
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        
        // Check if username already exists
        if (authService.getUserByUsername(username) != null) {
            System.out.println("Username already exists. Please choose a different username.");
            return;
        }
        
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        
        System.out.print("Enter role (admin/employee): ");
        String role = scanner.nextLine().toUpperCase();
        
        if (!role.equals("ADMIN") && !role.equals("EMPLOYEE")) {
            System.out.println("Invalid role. Please choose 'admin' or 'employee'.");
            return;
        }
        
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setEmail(email);
        newUser.setRole(role);
        newUser.setActive(true);
        
        if (authService.register(newUser)) {
            System.out.println("Registration successful! You can now login.");
        } else {
            System.out.println("Registration failed. Please try again.");
        }
    }
    
    private void showLoggedInMenu(User currentUser) {
        while (true) {
            if (currentUser.getRole().equals("ADMIN")) {
                displayAdminMenu();
            } else {
                displayEmployeeMenu();
            }
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            if (currentUser.getRole().equals("ADMIN")) {
                handleAdminChoice(choice, currentUser);
            } else {
                handleEmployeeChoice(choice, currentUser);
            }
            
            if (choice == 0) {
                break; // Logout
            }
        }
    }
    
    private void displayAdminMenu() {
        System.out.println("\n=== Admin Menu ===");
        System.out.println("1. Manage Employees");
        System.out.println("2. Manage Attendance");
        System.out.println("3. Manage Payroll");
        System.out.println("4. View Reports");
        System.out.println("5. Change Password");
        System.out.println("0. Logout");
        System.out.print("Enter your choice: ");
    }
    
    private void displayEmployeeMenu() {
        System.out.println("\n=== Employee Menu ===");
        System.out.println("1. View Profile");
        System.out.println("2. Mark Attendance");
        System.out.println("3. View Attendance History");
        System.out.println("4. View Payroll History");
        System.out.println("5. Change Password");
        System.out.println("0. Logout");
        System.out.print("Enter your choice: ");
    }
    
    private void handleAdminChoice(int choice, User user) {
        switch (choice) {
            case 1:
                manageEmployees();
                break;
            case 2:
                manageAttendance();
                break;
            case 3:
                managePayroll();
                break;
            case 4:
                viewReports();
                break;
            case 5:
                handleChangePassword(user);
                break;
            case 0:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void handleEmployeeChoice(int choice, User user) {
        switch (choice) {
            case 1:
                viewProfile(user);
                break;
            case 2:
                markAttendance(user);
                break;
            case 3:
                viewAttendanceHistory(user);
                break;
            case 4:
                viewPayrollHistory(user);
                break;
            case 5:
                handleChangePassword(user);
                break;
            case 0:
                System.out.println("Logging out...");
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void manageEmployees() {
        System.out.println("\n=== Manage Employees ===");
        System.out.println("1. Add Employee");
        System.out.println("2. View All Employees");
        System.out.println("3. Update Employee");
        System.out.println("4. Delete Employee");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                addEmployee();
                break;
            case 2:
                viewAllEmployees();
                break;
            case 3:
                updateEmployee();
                break;
            case 4:
                deleteEmployee();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void manageAttendance() {
        System.out.println("\n=== Manage Attendance ===");
        System.out.println("1. View All Attendance");
        System.out.println("2. Mark Leave");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                viewAllAttendance();
                break;
            case 2:
                markLeave();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void managePayroll() {
        System.out.println("\n=== Manage Payroll ===");
        System.out.println("1. Generate Payroll");
        System.out.println("2. View All Payroll");
        System.out.println("3. Process Payment");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                generatePayroll();
                break;
            case 2:
                viewAllPayroll();
                break;
            case 3:
                processPayment();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void viewReports() {
        System.out.println("\n=== View Reports ===");
        System.out.println("1. Attendance Report");
        System.out.println("2. Payroll Report");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                generateAttendanceReport();
                break;
            case 2:
                generatePayrollReport();
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void handleChangePassword(User user) {
        System.out.println("\n=== Change Password ===");
        System.out.print("Enter old password: ");
        String oldPassword = scanner.nextLine();
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();
        
        if (authService.changePassword(user.getUserId(), oldPassword, newPassword)) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Please try again.");
        }
    }
    
    private void viewProfile(User user) {
        System.out.println("\n=== User Profile ===");
        System.out.println("Username: " + user.getUsername());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Role: " + user.getRole());
        System.out.println("Last Login: " + (user.getLastLogin() != null ? user.getLastLogin() : "Never"));
    }
    
    private void markAttendance(User user) {
        System.out.println("\n=== Mark Attendance ===");
        System.out.println("1. Check In");
        System.out.println("2. Check Out");
        System.out.println("0. Back");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        switch (choice) {
            case 1:
                checkIn(user);
                break;
            case 2:
                checkOut(user);
                break;
            case 0:
                return;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }
    
    private void viewAttendanceHistory(User user) {
        System.out.println("\n=== Attendance History ===");
        // Implementation for viewing attendance history
    }
    
    private void viewPayrollHistory(User user) {
        System.out.println("\n=== Payroll History ===");
        // Implementation for viewing payroll history
    }
    
    private void addEmployee() {
        System.out.println("\n=== Add Employee ===");
        
        Employee employee = new Employee();
        
        System.out.print("Enter first name: ");
        employee.setFirstName(scanner.nextLine());
        
        System.out.print("Enter last name: ");
        employee.setLastName(scanner.nextLine());
        
        System.out.print("Enter email: ");
        employee.setEmail(scanner.nextLine());
        
        System.out.print("Enter phone: ");
        employee.setPhone(scanner.nextLine());
        
        System.out.print("Enter address: ");
        employee.setAddress(scanner.nextLine());
        
        System.out.print("Enter department: ");
        employee.setDepartment(scanner.nextLine());
        
        System.out.print("Enter position: ");
        employee.setPosition(scanner.nextLine());
        
        System.out.print("Enter start date (YYYY-MM-DD): ");
        employee.setStartDate(LocalDate.parse(scanner.nextLine()));
        
        System.out.print("Enter emergency contact name: ");
        employee.setEmergencyContactName(scanner.nextLine());
        
        System.out.print("Enter emergency contact phone: ");
        employee.setEmergencyContactPhone(scanner.nextLine());
        
        System.out.print("Enter initial leave balance: ");
        employee.setLeaveBalance(scanner.nextInt());
        scanner.nextLine(); // Consume newline
        
        if (employeeService.addEmployee(employee)) {
            System.out.println("Employee added successfully!");
            System.out.println("Employee ID: " + employee.getEmployeeId());
        } else {
            System.out.println("Failed to add employee. Please try again.");
        }
    }
    
    private void viewAllEmployees() {
        System.out.println("\n=== All Employees ===");
        List<Employee> employees = employeeService.getAllEmployees();
        
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        
        for (Employee employee : employees) {
            System.out.println("\n" + employee.toString());
        }
    }
    
    private void updateEmployee() {
        System.out.println("\n=== Update Employee ===");
        
        System.out.print("Enter employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        Employee employee = employeeService.getEmployee(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }
        
        System.out.println("\nCurrent employee details:");
        System.out.println(employee.toString());
        System.out.println("\nEnter new details (press Enter to keep current value):");
        
        System.out.print("First name [" + employee.getFirstName() + "]: ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setFirstName(input);
        }
        
        System.out.print("Last name [" + employee.getLastName() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setLastName(input);
        }
        
        System.out.print("Email [" + employee.getEmail() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setEmail(input);
        }
        
        System.out.print("Phone [" + employee.getPhone() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setPhone(input);
        }
        
        System.out.print("Address [" + employee.getAddress() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setAddress(input);
        }
        
        System.out.print("Department [" + employee.getDepartment() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setDepartment(input);
        }
        
        System.out.print("Position [" + employee.getPosition() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setPosition(input);
        }
        
        System.out.print("Start date [" + employee.getStartDate() + "] (YYYY-MM-DD): ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setStartDate(LocalDate.parse(input));
        }
        
        System.out.print("Emergency contact name [" + employee.getEmergencyContactName() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setEmergencyContactName(input);
        }
        
        System.out.print("Emergency contact phone [" + employee.getEmergencyContactPhone() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setEmergencyContactPhone(input);
        }
        
        System.out.print("Leave balance [" + employee.getLeaveBalance() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) {
            employee.setLeaveBalance(Integer.parseInt(input));
        }
        
        if (employeeService.updateEmployee(employee)) {
            System.out.println("Employee updated successfully!");
        } else {
            System.out.println("Failed to update employee. Please try again.");
        }
    }
    
    private void deleteEmployee() {
        System.out.println("\n=== Delete Employee ===");
        
        System.out.print("Enter employee ID: ");
        int employeeId = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        Employee employee = employeeService.getEmployee(employeeId);
        if (employee == null) {
            System.out.println("Employee not found.");
            return;
        }
        
        System.out.println("\nEmployee details:");
        System.out.println(employee.toString());
        
        System.out.print("\nAre you sure you want to delete this employee? (y/n): ");
        String confirm = scanner.nextLine().toLowerCase();
        
        if (confirm.equals("y")) {
            if (employeeService.deleteEmployee(employeeId)) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Failed to delete employee. Please try again.");
            }
        } else {
            System.out.println("Delete operation cancelled.");
        }
    }
    
    private void viewAllAttendance() {
        System.out.println("\n=== All Attendance ===");
        // Implementation for viewing all attendance
    }
    
    private void markLeave() {
        System.out.println("\n=== Mark Leave ===");
        // Implementation for marking leave
    }
    
    private void generatePayroll() {
        System.out.println("\n=== Generate Payroll ===");
        // Implementation for generating payroll
    }
    
    private void viewAllPayroll() {
        System.out.println("\n=== All Payroll ===");
        // Implementation for viewing all payroll
    }
    
    private void processPayment() {
        System.out.println("\n=== Process Payment ===");
        // Implementation for processing payment
    }
    
    private void generateAttendanceReport() {
        System.out.println("\n=== Attendance Report ===");
        // Implementation for generating attendance report
    }
    
    private void generatePayrollReport() {
        System.out.println("\n=== Payroll Report ===");
        // Implementation for generating payroll report
    }
    
    private void checkIn(User user) {
        System.out.println("\n=== Check In ===");
        // Implementation for checking in
    }
    
    private void checkOut(User user) {
        System.out.println("\n=== Check Out ===");
        // Implementation for checking out
    }
    
    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.start();
    }
} 