package edu.jsu.mcis.cs310.tas_fa23;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
/**
 *
 * @author Connor
 */
public class Absenteeism 
{
    private final Employee employee; 
    private final LocalDate date;
    private final BigDecimal absentPercentage;
    
    public Absenteeism(Employee employee, LocalDate date, BigDecimal absentPercentage)
    {
        this.employee = employee;
        this.date = date;
        this.absentPercentage = absentPercentage;
    }
    
    public Employee getEmployee()
    {
        return employee;
    }
    
    public LocalDate getDate()
    {
        return date;
    }
    
    public BigDecimal getAbsentPercenatge()
    {
        return absentPercentage;
    }
    
    @Override
    public String toString()
    {
        
        BigDecimal absentPercent = absentPercentage;
        Integer employeeID = employee.getId();
        LocalDate day = date;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        return "#" + employeeID.toString() + " (Pay Period Starting " + formatter.format(day) + "): " + absentPercent.toPlainString() + "%";
    }
}