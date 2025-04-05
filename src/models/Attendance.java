package models;

import java.time.LocalDateTime;
import java.time.LocalDate;

public class Attendance {
    private int attendanceId;
    private int employeeId;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String status; // Present, Absent, Late, On Leave
    private String leaveType; // Annual, Sick, Unpaid, etc.
    private LocalDate leaveDate;
    private String leaveReason;
    
    // Constructors
    public Attendance() {}
    
    public Attendance(int attendanceId, int employeeId, LocalDateTime checkIn, 
                     LocalDateTime checkOut, String status) {
        this.attendanceId = attendanceId;
        this.employeeId = employeeId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }
    
    // Constructor for leave records
    public Attendance(int attendanceId, int employeeId, LocalDate leaveDate, 
                     String leaveType, String leaveReason) {
        this.attendanceId = attendanceId;
        this.employeeId = employeeId;
        this.leaveDate = leaveDate;
        this.leaveType = leaveType;
        this.leaveReason = leaveReason;
        this.status = "On Leave";
    }
    
    // Getters and Setters
    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    
    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }
    
    public LocalDateTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalDateTime checkIn) { this.checkIn = checkIn; }
    
    public LocalDateTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalDateTime checkOut) { this.checkOut = checkOut; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getLeaveType() { return leaveType; }
    public void setLeaveType(String leaveType) { this.leaveType = leaveType; }
    
    public LocalDate getLeaveDate() { return leaveDate; }
    public void setLeaveDate(LocalDate leaveDate) { this.leaveDate = leaveDate; }
    
    public String getLeaveReason() { return leaveReason; }
    public void setLeaveReason(String leaveReason) { this.leaveReason = leaveReason; }
    
    // Helper method to check if attendance is a leave record
    public boolean isLeaveRecord() {
        return "On Leave".equals(status);
    }
}
