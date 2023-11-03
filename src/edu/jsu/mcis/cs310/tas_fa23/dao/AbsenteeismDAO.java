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
    
    private final String FIND_QUERY = "Select * from absenteeism Where employeeid = ? AND payperiod = ?";
    private final String CREATE_QUERY = "Insert into absenteeism ('employeeid, timestamp, absenteeism) values (?, ?, ?)";
    private final String UPDATE_QUERY = "Update percentage = ? where percentage = ?";
    
    AbsenteeismDAO(DAOFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    public Absenteeism find(Employee employee, LocalDate date)
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
                preparedstatement = conn.prepareStatement(FIND_QUERY);
                preparedstatement.setInt(1, employee.getId());
                preparedstatement.setObject(2, date);
                preparedstatement.execute();
                
                resultSet = preparedstatement.getResultSet();
                if (resultSet.next())
                {
                    absentPercentage = resultSet.getBigDecimal(3);
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
    
    public void create(Absenteeism absenteeism) throws SQLException
    {
        Connection connection = daoFactory.getConnection();
        
        PreparedStatement preparedstatement = null;
        ResultSet resultset = null;
  

        BigDecimal absentPercent = absenteeism.getAbsentPercenatge();
        
        Integer employeeID = absenteeism.getEmployee().getId();
        LocalDate timeStamp = absenteeism.getDate();
        
        try
        {
            if(connection.isValid(0))
            {
                preparedstatement = connection.prepareStatement(FIND_QUERY);
                preparedstatement.setInt(1, employeeID);
                preparedstatement.setObject(2, timeStamp);
                
                boolean hasResult = preparedstatement.execute();
                
                resultset = preparedstatement.getResultSet();
                
                if (!hasResult)
                {
                    preparedstatement = connection.prepareStatement(CREATE_QUERY);
                    preparedstatement.setInt(1, employeeID);
                    preparedstatement.setObject(2, timeStamp);
                    preparedstatement.setDouble(3, absentPercent.doubleValue());
                    preparedstatement.execute();
                }
                else
                {
                    preparedstatement = connection.prepareStatement(UPDATE_QUERY);
                    preparedstatement.setDouble(1, absentPercent.doubleValue());
                    preparedstatement.setDouble(2, resultset.getDouble(3));
                    preparedstatement.execute();
                }
            }
        }
        catch(DAOException e)
        {
            throw new DAOException(e.getMessage());
        }
    }
}