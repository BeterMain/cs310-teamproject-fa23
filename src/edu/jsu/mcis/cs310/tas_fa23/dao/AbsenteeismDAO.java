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
    
    private final String FIND_QUERY = "Select * from Absenteeism Where employee = ? AND payperiod = ?";
    
    AbsenteeismDAO(DAOFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    Absenteeism find(Employee employee, LocalDate date)
    {
        Absenteeism absenteeism = null;
        
        Connection conn = daoFactory.getConnection();
        
        PreparedStatement preparedstatement = null;
        ResultSet resultSet = null;
        
        BigDecimal absentPercentage = null;
        
        try 
        {
            if (conn.isValid(0))
            {                
                resultSet = preparedstatement.executeQuery(FIND_QUERY);
                preparedstatement.setInt(1, employee.getId());
                preparedstatement.setObject(2, date);
                
                absentPercentage = resultSet.getBigDecimal(3);
            }
            
            absenteeism = new Absenteeism(employee, date, absentPercentage);
        }
        catch (SQLException e)
        {
            throw new DAOException(e.toString());
        }
        
        return absenteeism;
    }
}