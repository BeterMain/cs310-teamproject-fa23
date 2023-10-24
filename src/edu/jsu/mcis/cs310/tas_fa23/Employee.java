
package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Employee {
    
    /* Initialize Variables */
    
    private final int id;
    private final String firstName, middleName, lastName;
    private final LocalDateTime active;
    private final Badge badge;
    private final Department department;
    private final Shift shift;
    private final EmployeeType employeeType;
    
    /* Create Constructor */
    
    public Employee(HashMap<String, Object> parameterMap) {
        this.id = (int) parameterMap.get("id");
        this.firstName = (String) parameterMap.get("firstName");
        this.middleName = (String) parameterMap.get("middleName");
        this.lastName = (String) parameterMap.get("lastName");
        this.active = (LocalDateTime) parameterMap.get("active");
        this.badge = (Badge) parameterMap.get("badge");
        this.department = (Department) parameterMap.get("department");
        this.shift = (Shift) parameterMap.get("shift");
        this.employeeType = (EmployeeType) parameterMap.get("employeeType");
    }
    
    /* Create Getters */ 
    
    public int getId() {
        return this.id;
    }
    
    public Badge getBadge() {
        return this.badge;
    }
    
    public String getFirstName() {
        return this.firstName;
    }
    
    public String getMiddleName() {
        return this.middleName;
    }
    
    public String getLastName() {
        return this.lastName;
    }
    
    public LocalDateTime getActive() {
        return this.active;
    }
    
    public Department getDepartment() {
        return this.department;
    }
    
    public Shift getShift() {
        return this.shift;
    }
    
    public EmployeeType getEmployeeType() {
        return this.employeeType;
    }
    
    /* Override toString method */
    
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        
        // Append ID
        s.append("ID #").append(id).append(": ");
        // Append Name
        s.append(badge.getDescription());
        // Append Badge Number
        s.append(" (#").append(badge.getId());
        // Append Employee Type
        s.append("), Type: ").append(employeeType.toString());
        // Append Department
        s.append(", Department: ").append(department.getDescription());
        // Append Active Date
        s.append(", Active: ").append(active.format(formatter));
        
        return s.toString();
    }
    
}
