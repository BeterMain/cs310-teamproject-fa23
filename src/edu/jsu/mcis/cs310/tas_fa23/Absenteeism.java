package edu.jsu.mcis.cs310.tas_fa23;

import java.math.BigDecimal;
import java.sql.*;
/**
 *
 * @author Connor
 */
public class Absenteeism 
{
    private Employee employee; 
    private Date date;
    private BigDecimal absentPercentage;
    
    public Absenteeism(Employee employee, Date date, BigDecimal absentPercentage)
    {
        this.employee = employee;
        this.date = date;
        this.absentPercentage = absentPercentage;
    }
    
    public Employee getEmployee()
    {
        return employee;
    }
    
    public Date getDate()
    {
        return date;
    }
    
    public BigDecimal getAbsentPercenatge()
    {
        return absentPercentage;
    }
    
    public String toString(Absenteeism absenteeism)
    {
        
        Double absentPercent = absenteeism.getAbsentPercenatge().doubleValue();
        Integer employeeID = absenteeism.getEmployee().getId();
        Date day = absenteeism.getDate();
        
        return "#" + employeeID.toString() + " (Period Period Starting: " + day.toString() + "): " + absentPercent.toString() + "%";
    }
}