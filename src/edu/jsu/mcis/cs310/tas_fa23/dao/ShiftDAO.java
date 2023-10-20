package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.sql.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.util.HashMap;
import edu.jsu.mcis.cs310.tas_fa23.Badge;

/**
 *
 * @author cmunc
 */

public class ShiftDAO {
    private final String idQuery = "select * from shift where id = ?";
    private final String badgeQuery = "select * from employee where badgeid = ?";
    
    private final DAOFactory daoFactory;
    
    private int shiftID = 0;
    
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
                        HashMap<String, Object> hashmap = new HashMap<>();

                        
                        hashmap.put("id", id);
                        hashmap.put("description", rs.getString("description"));
                        hashmap.put("shiftstart", rs.getTime("shiftstart"));
                        hashmap.put("shiftstop", rs.getTime("shiftstop"));
                        hashmap.put("roundinterval", rs.getInt("roundinterval"));
                        hashmap.put("graceperiod",rs.getInt("graceperiod"));
                        hashmap.put("dockpenalty",rs.getInt("dockpenalty"));
                        hashmap.put("lunchstart", rs.getTime("lunchstart"));
                        hashmap.put("lunchstop", rs.getTime("lunchstop"));
                        hashmap.put("lunchthreshold",rs.getInt("lunchthreshold"));
                        shift = new Shift(hashmap);
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
    
    public Shift find(Badge badgeid)
    {
        Shift shift = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try
        {
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0))
            {
                ps = conn.prepareStatement(badgeQuery);
                ps.setString(1, badgeid.getId());
                
                boolean result = ps.execute();
                
                if (result)
                {
                    rs = ps.getResultSet();
                    
                    if (rs.next())
                    {
                        try 
                        {
                            shiftID = rs.getInt("shiftid");
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
                
                ps = conn.prepareStatement(idQuery);
                ps.setInt(1, shiftID);
                
                result = ps.execute();
                
                if(result)
                {
                    rs = ps.getResultSet();
                    
                    if (rs.next())
                    {
                        HashMap<String, Object> hashmap = new HashMap<>();

                        
                        hashmap.put("id", shiftID);
                        hashmap.put("description", rs.getString("description"));
                        hashmap.put("shiftstart", rs.getTime("shiftstart"));
                        hashmap.put("shiftstop", rs.getTime("shiftstop"));
                        hashmap.put("roundinterval", rs.getInt("roundinterval"));
                        hashmap.put("graceperiod",rs.getInt("graceperiod"));
                        hashmap.put("dockpenalty",rs.getInt("dockpenalty"));
                        hashmap.put("lunchstart", rs.getTime("lunchstart"));
                        hashmap.put("lunchstop", rs.getTime("lunchstop"));
                        hashmap.put("lunchthreshold",rs.getInt("lunchthreshold"));
                        shift = new Shift(hashmap);
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