package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.sql.*;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.util.HashMap;
import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.DailySchedule;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

public class ShiftDAO {
    
    private final String SHIFTIDQUERY = "SELECT * FROM shift WHERE id = ?";
    private final String SCHEDULEIDQUERY = "SELECT * FROM dailyschedule WHERE id = ?";
    private final String OVERRIDEQUERY = "SELECT * FROM scheduleoverride WHERE DATE(start) = ?";
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
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
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
                        shiftId = rs.getInt("shiftid");
                    }
                }
                
                /* Use Previous Find() Method to get new Shift Object */
                shift = find(shiftId);
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        /* Return Shift Object */
        return shift;
    }
    
    public Shift find(Badge badgeid, LocalDate ts)
    {
        Shift shift = null;
        boolean badgeMatch = false;
        int overrideDay = 0;
        int overrideSchedule = 0;
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
                        shiftId = rs.getInt("shiftid");
                    }
                }
                
                /* Use Previous Find() Method to get new Shift Object */
                shift = find(shiftId);
                
                // Check for if local date equals schedule override table value "start"
                ps = conn.prepareStatement(OVERRIDEQUERY);
                ts = ts.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
                ps.setDate(1, Date.valueOf(ts));
                
                result = ps.execute();
                
                if (result) 
                {
                    rs = ps.getResultSet();
                    
                    if (rs.next()) 
                    {
                        // Get day value from table and replace corresponding default schedule with value in table
                        overrideDay = rs.getInt("day");
                        overrideSchedule = rs.getInt("dailyscheduleid");
                        
                        if (rs.getString("badgeid") == null) {
                            badgeMatch = true;
                        }
                        else if (rs.getString("badgeid").equalsIgnoreCase(badgeid.getId()) )
                        {
                            badgeMatch = true;
                        }
                        
                    }
                }
                
                // Check for overrided DailySchedule
                ps = conn.prepareStatement(SCHEDULEIDQUERY);
                ps.setInt(1, overrideSchedule);
                
                result = ps.execute();
                
                if (result) {
                    rs = ps.getResultSet();
                    
                    if (rs.next() && badgeMatch) {
                        
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
                        
                        HashMap<Integer, DailySchedule> scheduleMap = new HashMap<>();
                        
                        for (int i = 1; i < 6; i++)
                        {
                            
                            DayOfWeek day = DayOfWeek.of(i);
                            
                            if (day.getValue() == overrideDay)
                            {
                                scheduleMap.put(day.getValue(), dailySchedule);
                            }
                            else 
                            {
                                scheduleMap.put(day.getValue(), shift.getDefaultSchedule());
                            }
                            
                        }
                        
                        shift.setScheduleMap(scheduleMap);
                        
                    }
                    else 
                    {
                        HashMap<Integer, DailySchedule> scheduleMap = new HashMap<>();

                        for (int i = 1; i < 6; i++)
                        {
                            
                            DayOfWeek day = DayOfWeek.of(i);
                            
                            scheduleMap.put(day.getValue(), shift.getDefaultSchedule());
                            
                        }

                        shift.setScheduleMap(scheduleMap);
                    }
                    
                }
                
                
                
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        /* Return Shift Object */
        return shift;
    }
    
}
