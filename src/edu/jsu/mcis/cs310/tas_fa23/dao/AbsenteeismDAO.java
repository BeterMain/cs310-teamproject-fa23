package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.*;
import java.time.LocalDate;
import java.math.BigDecimal;
import java.sql.*;


/**
 *
 * @author Connor
 */

public class AbsenteeismDAO 
{
    private final DAOFactory daoFactory;
    
    private final String FIND_QUERY = "SELECT * FROM absenteeism WHERE employeeid = ? AND payperiod = ?";
    private final String CREATE_QUERY = "INSERT INTO absenteeism (employeeid, payperiod, percentage) values (?, ?, ?)";
    private final String UPDATE_QUERY = "UPDATE absenteeism SET percentage = ? WHERE employeeid = ? AND payperiod = ?";
    
    AbsenteeismDAO(DAOFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    public Absenteeism find(Employee employee, LocalDate date)
    {
        Absenteeism absenteeism = null;
        
        Connection conn = daoFactory.getConnection();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        BigDecimal absentPercentage = null;
        
        try 
        {
            if (conn.isValid(0))
            {                
                ps = conn.prepareStatement(FIND_QUERY);
                ps.setInt(1, employee.getId());
                ps.setObject(2, date);
                
                boolean hasResults = ps.execute();
                
                if (hasResults) {
                    
                    rs = ps.getResultSet();
                
                    if (rs.next())
                    {
                        absentPercentage = rs.getBigDecimal("percentage");
                    }
                    
                }
                
            }
            
            absenteeism = new Absenteeism(employee, date, absentPercentage);
            
            
        }
        catch (SQLException e)
        {
            throw new DAOException(e.toString());
        }
        
        return absenteeism;
    }
    
    public void create(Absenteeism absenteeism)
    {
        Connection connection = daoFactory.getConnection();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
  

        BigDecimal absentPercent = absenteeism.getAbsentPercenatge();
        
        Integer employeeID = absenteeism.getEmployee().getId();
        LocalDate timeStamp = absenteeism.getDate();
        
        try
        {
            if(connection.isValid(0))
            {
                
                /* Search for existing data */
                
                ps = connection.prepareStatement(FIND_QUERY);
                ps.setInt(1, employeeID);
                ps.setDate(2, Date.valueOf(timeStamp));
                
                boolean hasResult = ps.execute();
                
                /* Replace old percentage if data already exists */
                
                if (!hasResult)
                {
                    rs = ps.getResultSet();
                    
                    ps = connection.prepareStatement(UPDATE_QUERY);
                    ps.setDouble(1, absentPercent.doubleValue());
                    ps.setInt(2, rs.getInt("employeeid"));
                    ps.setDate(3, rs.getDate("payperiod"));
                    ps.execute();
                }
                
                /* Add new data if it doesn't exist  */
                
                else
                {
                    ps = connection.prepareStatement(CREATE_QUERY);
                    ps.setInt(1, employeeID);
                    ps.setDate(2, Date.valueOf(timeStamp));
                    ps.setDouble(3, absentPercent.doubleValue());
                    ps.execute();
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