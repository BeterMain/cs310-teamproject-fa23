package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.sql.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.time.LocalTime;

/**
 *
 * @author cmunc
 */

public class ShiftDAO {
    private final String idQuery = "select * from shift where id = ?";
    private final String badgeQuery = "select * from shift where badge = ?";
    
    private final DAOFactory daoFactory;
    
    LocalTime shiftStart = null, shiftStop = null, lunchStart = null, lunchStop = null;
    
    ShiftDAO(DAOFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    public Shift find(int id)
    {
        Shift shift = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try
        {
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0))
            {
                ps = conn.prepareStatement(idQuery);
                ps.setInt(1, id);
                
                
                boolean result = ps.execute();
                
                if(result)
                {
                    rs = ps.getResultSet();
                    
                    if (rs.next())
                    {
                        String description = rs.getString("description");
                        String shiftStartString = rs.getString("shiftstart"); 
                        String shiftStopString = rs.getString("shiftstop");
                        int roundInterval = rs.getInt("roundinterval");
                        int gracePeriod = rs.getInt("graceperiod");
                        int dockPenalty = rs.getInt("dockpenalty");
                        String lunchStartString = rs.getString("lunchstart");
                        String lunchStopString  = rs.getString("lunchstop");
                        int lunchThreshold = rs.getInt("lunchthreshold");
                        
                        shiftStart = LocalTime.parse(shiftStartString);
                        shiftStop = LocalTime.parse(shiftStopString);
                        lunchStart = LocalTime.parse(lunchStartString);
                        lunchStop = LocalTime.parse(lunchStopString);
                        
                        
                        shift = new Shift(id, description, shiftStart, shiftStop, roundInterval, gracePeriod, dockPenalty, lunchStart, lunchStop, lunchThreshold);
                    }
                }
            }
        }
        catch(SQLException e) 
        {
            throw new DAOException(e.getMessage());   
        }
        
        finally
        {
            if (rs != null)
            {
                try
                {
                    rs.close();
                }
                catch (SQLException e)
                {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null)
            {
                try
                {
                    ps.close();
                }
                catch (SQLException e)
                {
                    throw new DAOException(e.getMessage());
                }
            }
        }
        return shift;
    }
}