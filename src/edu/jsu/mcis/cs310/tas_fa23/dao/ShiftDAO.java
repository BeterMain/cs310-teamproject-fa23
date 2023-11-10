package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.sql.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.util.HashMap;
import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.DailySchedule;
import java.time.LocalDate;

/**
 *
 * @author cmunc
 */

public class ShiftDAO {
    
    private final String SHIFTIDQUERY = "SELECT * FROM shift WHERE id = ?";
    private final String SCHEDULEIDQUERY = "SELECT * FROM dailyschedule WHERE id = ?";
    private final String BADGEQUERY = "SELECT * FROM employee WHERE badgeid = ?";
    
    private final DAOFactory daoFactory;
    
    ShiftDAO(DAOFactory daoFactory)
    {
        this.daoFactory = daoFactory;
    }
    
    public Shift find(int id)
    {
        Shift shift = null;
        
        int dailyScheduleId = 0;
        String description = "";
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try
        {
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0))
            {
                
                /* Look For a Match With ID in Shift Table */
                ps = conn.prepareStatement(SHIFTIDQUERY);
                ps.setInt(1, id);
                
                
                boolean result = ps.execute();
                
                if(result)
                {
                    rs = ps.getResultSet();
                    
                    if (rs.next())
                    {
                        /* Get Shift description and dailyscheduleid from database */
                        description = rs.getString("description");
                        
                        dailyScheduleId = rs.getInt("dailyscheduleid");
                        
                        rs.close();
                    }
                }
                
                /* Look For a Match with DailySchedule ID */
                ps = conn.prepareStatement(SCHEDULEIDQUERY);
                ps.setInt(1, dailyScheduleId);
                
                result = ps.execute();
                
                if (result) {
                    rs = ps.getResultSet();
                    
                    if (rs.next()) {
                        
                        /* Initialize New Map */
                        
                        HashMap<String, Object> hashmap = new HashMap<>();
                        
                        hashmap.put("shiftstart", rs.getTime("shiftstart"));
                        hashmap.put("shiftstop", rs.getTime("shiftstop"));
                        hashmap.put("roundinterval", rs.getInt("roundinterval"));
                        hashmap.put("graceperiod",rs.getInt("graceperiod"));
                        hashmap.put("dockpenalty",rs.getInt("dockpenalty"));
                        hashmap.put("lunchstart", rs.getTime("lunchstart"));
                        hashmap.put("lunchstop", rs.getTime("lunchstop"));
                        hashmap.put("lunchthreshold",rs.getInt("lunchthreshold"));
                        
                        DailySchedule dailySchedule = new DailySchedule(hashmap);
                        
                        /* Create new Object */
                        
                        shift = new Shift(id, description, dailySchedule);
                        
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
        
        /* Return Shift Object */
        return shift;
    }
    
    public Shift find(Badge badgeid)
    {
        Shift shift = null;
        int shiftId = 0;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try
        {
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0))
            {
                /* Look For a Match With Badge ID in Employee Table */
                ps = conn.prepareStatement(BADGEQUERY);
                ps.setString(1, badgeid.getId());
                
                boolean result = ps.execute();
                
                if (result)
                {
                    rs = ps.getResultSet();
                    
                    if (rs.next())
                    {
                        try 
                        {
                            shiftId = rs.getInt("shiftid");
                        }
                        catch (DAOException e)
                        {
                            throw new DAOException (e.getMessage());
                        }
                        finally
                        {
                            rs.close();
                        }
                    }
                }
                
                /* Use Previous Find() Method to get new Shift Object */
                shift = find(shiftId);
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
        
        /* Return Shift Object */
        return shift;
    }
}
