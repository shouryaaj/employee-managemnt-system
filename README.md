# Employee Management System

A Java-based Employee Management System that allows administrators to manage employees, track attendance, and handle payroll.

## Features

- **User Authentication**: Login and registration with role-based access (Admin/Employee)
- **Employee Management**: Add, view, update, and delete employee records
- **Attendance Tracking**: Mark attendance, view attendance history
- **Payroll Management**: Generate payroll, process payments
- **Reporting**: Generate attendance and payroll reports

## Database Structure

The system uses MySQL database with the following tables:
- `users`: Stores user authentication information
- `employees`: Stores employee details
- `attendance`: Tracks employee attendance
- `payroll`: Manages employee payroll information

## Setup Instructions

1. Ensure you have Java JDK and MySQL installed
2. Create a MySQL database named `employee_management`
3. Run the SQL scripts in the `sql` directory to set up the database schema
4. Compile the Java files:
   ```
   javac -d bin src/models/*.java
   javac -d bin -cp bin src/utils/*.java
   javac -d bin -cp bin src/services/*.java
   javac -d bin -cp bin src/reports/*.java
   javac -d bin -cp bin src/ui/*.java
   javac -d bin -cp bin src/Main.java
   ```
5. Run the application:
   ```
   java -cp "bin;lib/*" Main
   ```

## Default Admin Credentials

- Username: admin
- Password: admin123

## Project Structure

- `src/models/`: Data model classes
- `src/services/`: Business logic and database operations
- `src/utils/`: Utility classes (database connection, encryption)
- `src/ui/`: User interface classes
- `src/reports/`: Report generation classes
- `sql/`: SQL scripts for database setup
- `lib/`: External libraries (MySQL JDBC driver)

## Contributing

1. Fork the repository
2. Create a new branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 