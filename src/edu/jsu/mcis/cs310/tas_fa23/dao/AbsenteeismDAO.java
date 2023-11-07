package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.sql.*;
import java.time.DayOfWeek;
import java.time.temporal.TemporalAdjusters;


public class AbsenteeismDAO 
{
    private final DAOFactory daoFactory;
    
    private final String FIND_QUERY = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiod = ?";
    private final String CREATE_QUERY = "INSERT INTO absenteeism (employeeid, payperiod, percentage) values (?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE absenteeism SET percentage = ? WHERE employeeid = ? AND payperiod = ?";
    
    /* Constructor  */
    
    AbsenteeismDAO(DAOFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    /* Find Method */
    
    public Absenteeism find(Employee employee, LocalDate date)
    {
        /* Declare Variables */
        
        Absenteeism absenteeism = null;
        
        Connection conn = daoFactory.getConnection();
        
        PreparedStatement ps;
        ResultSet rs;
        
        BigDecimal absentPercentage = null;
        
        date = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        
        try 
        {
            if (conn.isValid(0))
            {
                
                /* Setup Query and Check for Results */
                
                ps = conn.prepareStatement(FIND_QUERY);
                ps.setInt(1, employee.getId());
                ps.setDate(2, Date.valueOf(date));
                
                boolean hasResults = ps.execute();
                
                if (hasResults) {
                    
                    rs = ps.getResultSet();
                    
                    /* If results then get new absentPercent */
                    
                    if (rs.next())
                    {
                        absentPercentage = rs.getBigDecimal("percentage");
                    }
                    
                }
                
            }
            
            /* Create new Absenteeism Object */
            
            absenteeism = new Absenteeism(employee, date, absentPercentage);
            
            
        }
        catch (SQLException e)
        {
            throw new DAOException(e.toString());
        }
        
        /* Return Result */
        
        return absenteeism;
    }
    
    /* Create Method */
    
    public void create(Absenteeism absenteeism)
    {
        Connection connection = daoFactory.getConnection();
        
        PreparedStatement ps = null;
        ResultSet rs = null;

        BigDecimal absentPercent = absenteeism.getAbsentPercenatge();
        
        Integer employeeID = absenteeism.getEmployee().getId();
        LocalDate date = absenteeism.getDate();
        LocalDate timeStamp = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        
        try
        {
            if(connection.isValid(0))
            {
                
                /* Search for existing data */
                
                ps = connection.prepareStatement(FIND_QUERY);
                ps.setInt(1, employeeID);
                ps.setDate(2, Date.valueOf(timeStamp));
                
                boolean hasResult = ps.execute();
                
                if (hasResult)
                {
                    rs = ps.getResultSet();
                    
                    /* Replace old percentage if data already exists */
                    
                    if (rs.next()) {
                        
                        ps = connection.prepareStatement(UPDATE_QUERY);
                        ps.setDouble(1, absentPercent.doubleValue());
                        ps.setInt(2, rs.getInt("employeeid"));
                        ps.setDate(3, rs.getDate("payperiod"));
                        ps.execute();
                        
                    }
                    
                    /* Add new data if it doesn't exist  */
                    
                    else {
                        
                        ps = connection.prepareStatement(CREATE_QUERY);
                        ps.setInt(1, employeeID);
                        ps.setDate(2, Date.valueOf(timeStamp));
                        ps.setDouble(3, absentPercent.doubleValue());
                        ps.execute();
                        
                    }
                }
                
                
                
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
    }
}