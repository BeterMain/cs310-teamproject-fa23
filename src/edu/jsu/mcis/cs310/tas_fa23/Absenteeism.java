package edu.jsu.mcis.cs310.tas_fa23;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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
    
    /* Construtor */
    
    public Absenteeism(Employee employee, LocalDate date, BigDecimal absentPercentage)
    {
        this.employee = employee;
        this.date = date;
        
        /* Set Scale for percentage */
        
        this.absentPercentage = absentPercentage.setScale(2);
        
    }
    
    /* Getters */
    
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
    
    /* Overrided toString  Method */
    
    @Override
    public String toString()
    {
        
        String employeeID = employee.getBadge().getId();
        LocalDate day = date;
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        
        return "#" + employeeID + " (Pay Period Starting " + formatter.format(day) + "): " + absentPercentage.toString() + "%";
    }
}