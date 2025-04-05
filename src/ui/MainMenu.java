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
                showAttendanceMenu(user);
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
    
    private void showAttendanceMenu(User currentUser) {
        while (true) {
            System.out.println("\n=== Attendance Management ===");
            System.out.println("1. Mark Attendance (Check-in/Check-out)");
            System.out.println("2. Apply for Leave");
            System.out.println("3. View Attendance History");
            System.out.println("4. View Leave History");
            System.out.println("0. Back to Main Menu");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            
            switch (choice) {
                case 1:
                    handleAttendance(currentUser);
                    break;
                case 2:
                    handleLeaveApplication(currentUser);
                    break;
                case 3:
                    viewAttendanceHistory(currentUser);
                    break;
                case 4:
                    viewLeaveHistory(currentUser);
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private void handleAttendance(User currentUser) {
        System.out.println("\n=== Mark Attendance ===");
        System.out.println("1. Check-in");
        System.out.println("2. Check-out");
        System.out.print("Enter your choice: ");
        
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        // Get employee ID for the current user
        Employee employee = employeeService.getEmployeeByUserId(currentUser.getUserId());
        if (employee == null) {
            System.out.println("Employee record not found.");
            return;
        }
        
        switch (choice) {
            case 1:
                if (attendanceService.markAttendance(employee.getEmployeeId(), java.time.LocalDateTime.now())) {
                    System.out.println("Check-in successful!");
                } else {
                    System.out.println("Check-in failed. You may have already checked in today.");
                }
                break;
            case 2:
                if (attendanceService.updateCheckOut(employee.getEmployeeId(), java.time.LocalDateTime.now())) {
                    System.out.println("Check-out successful!");
                } else {
                    System.out.println("Check-out failed. You may not have checked in today.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private void handleLeaveApplication(User currentUser) {
        System.out.println("\n=== Apply for Leave ===");
        
        // Get employee ID for the current user
        Employee employee = employeeService.getEmployeeByUserId(currentUser.getUserId());
        if (employee == null) {
            System.out.println("Employee record not found.");
            return;
        }
        
        // Check leave balance
        if (employee.getLeaveBalance() <= 0) {
            System.out.println("You have no leave balance remaining.");
            return;
        }
        
        System.out.println("Current leave balance: " + employee.getLeaveBalance() + " days");
        
        System.out.print("Enter leave date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate leaveDate;
        try {
            leaveDate = LocalDate.parse(dateStr);
        } catch (Exception e) {
            System.out.println("Invalid date format.");
            return;
        }
        
        System.out.println("\nLeave Types:");
        System.out.println("1. Annual Leave");
        System.out.println("2. Sick Leave");
        System.out.println("3. Unpaid Leave");
        System.out.print("Enter leave type (1-3): ");
        
        int typeChoice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        
        String leaveType;
        switch (typeChoice) {
            case 1:
                leaveType = "Annual";
                break;
            case 2:
                leaveType = "Sick";
                break;
            case 3:
                leaveType = "Unpaid";
                break;
            default:
                System.out.println("Invalid leave type.");
                return;
        }
        
        System.out.print("Enter leave reason: ");
        String leaveReason = scanner.nextLine();
        
        if (attendanceService.markLeave(employee.getEmployeeId(), leaveDate, leaveType, leaveReason)) {
            System.out.println("Leave application submitted successfully!");
        } else {
            System.out.println("Leave application failed. Please try again.");
        }
    }
    
    private void viewAttendanceHistory(User currentUser) {
        System.out.println("\n=== Attendance History ===");
        
        // Get employee ID for the current user
        Employee employee = employeeService.getEmployeeByUserId(currentUser.getUserId());
        if (employee == null) {
            System.out.println("Employee record not found.");
            return;
        }
        
        List<Attendance> attendances = attendanceService.getEmployeeAttendance(employee.getEmployeeId());
        if (attendances.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        }
        
        System.out.println("\nDate\t\tCheck-in\t\tCheck-out\t\tStatus");
        System.out.println("------------------------------------------------------------");
        for (Attendance attendance : attendances) {
            if (!attendance.isLeaveRecord()) {
                System.out.printf("%s\t%s\t%s\t%s\n",
                    attendance.getCheckIn().toLocalDate(),
                    attendance.getCheckIn().toLocalTime(),
                    attendance.getCheckOut() != null ? attendance.getCheckOut().toLocalTime() : "Not checked out",
                    attendance.getStatus());
            }
        }
    }
    
    private void viewLeaveHistory(User currentUser) {
        System.out.println("\n=== Leave History ===");
        
        // Get employee ID for the current user
        Employee employee = employeeService.getEmployeeByUserId(currentUser.getUserId());
        if (employee == null) {
            System.out.println("Employee record not found.");
            return;
        }
        
        List<Attendance> leaves = attendanceService.getLeaveHistory(employee.getEmployeeId());
        if (leaves.isEmpty()) {
            System.out.println("No leave records found.");
            return;
        }
        
        System.out.println("\nDate\t\tType\t\tReason");
        System.out.println("----------------------------------------");
        for (Attendance leave : leaves) {
            System.out.printf("%s\t%s\t%s\n",
                leave.getLeaveDate(),
                leave.getLeaveType(),
                leave.getLeaveReason());
        }
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
    
    public static void main(String[] args) {
        MainMenu menu = new MainMenu();
        menu.start();
    }
} 