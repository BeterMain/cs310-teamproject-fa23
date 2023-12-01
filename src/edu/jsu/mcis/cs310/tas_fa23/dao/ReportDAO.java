package edu.jsu.mcis.cs310.tas_fa23.dao;


import com.github.cliftonlabs.json_simple.Jsoner;
import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Department;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import java.time.*;

public class ReportDAO { 
    
    private final String SINGLE_QUERY = "SELECT * FROM employee WHERE departmentid = ? ORDER BY lastname, firstname ";
    private final String MANY_QUERY = "SELECT * FROM employee ORDER BY lastname, firstname ";
    private final String EMPLOYEETYPE_QUERY = "SELECT * FROM employeetype WHERE id = ?";
    private final String EMPLOYEEID_QUERY = "SELECT * FROM employee WHERE id = ?";
    private final String ABSENTEEISMACEND_QUERY = "SELECT * FROM absenteeism WHERE employeeid = ? ORDER BY payperiod";
    private final String ABSENTEEISMDESC_QUERY  = "SELECT * FROM absenteeism WHERE employeeid = ? ORDER BY payperiod desc";
    private final String SINGLEDEPARTMENT_QUERY = "SELECT * FROM employee WHERE departmentid = ?";
    private final String ALLEMPLOYEES_QUERY = "SELECT * FROM employee";
    private final DAOFactory daoFactory;
    
    public ReportDAO(DAOFactory daoFactory) {
        
        this.daoFactory = daoFactory;
    }
    public String getBadgeSummary(Integer id) {
        
        String result;
        ArrayList<HashMap<String,String>> jsonData = new ArrayList<>();
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
        
        String employeeType = null;
        String query;
        
        /* Check if the id is null */
        if (id == null) {
            query = MANY_QUERY;
        }
        else {
            query = SINGLE_QUERY; 
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2;
        
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                /* Run employee Table Query */
                if (id == null) {
                    ps = conn.prepareStatement(query);
                }
                else {
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, id);
                }

                boolean hasFirstResults = ps.execute();

                if (hasFirstResults) {

                    rs = ps.getResultSet();
                        
                    while (rs.next()) {
                        
                        /* Run employeetype Table Query */
                        ps = conn.prepareStatement(EMPLOYEETYPE_QUERY);
                        ps.setInt(1, rs.getInt("employeetypeid"));

                        boolean hasSecondResults = ps.execute();
                        
                        if (hasSecondResults) {
                        
                            rs2 = ps.getResultSet();

                            /* Get Correct Description of Employee */

                            if (rs2.next()) {
                                employeeType = rs2.getString("description");
                            }
                            
                            /* Search through first results and assign values */
                            
                            /* Find a badge object for complete description */
                            Badge b = badgeDAO.find(rs.getString("badgeid"));

                            /* Find a department object for complete description */
                            Department d = departmentDAO.find(rs.getInt("departmentid"));

                            /* Put all of the data into the HashMap */
                            HashMap<String,String> employeeData = new HashMap<>();

                            employeeData.put("badgeid", b.getId());
                            employeeData.put("name", b.getDescription());
                            employeeData.put("department", d.getDescription());
                            employeeData.put("type", employeeType);

                            /* Add the HashMap to the ArrayList */
                            jsonData.add(employeeData);
                        }
                    }
                    
                }
                    
            }
                
            /* If no data available, print an error */

            else {

                System.err.println("No Data Found!");

            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        result = Jsoner.serialize(jsonData);
        
        return result;
    }
    
    public String getWhosInWhosOut(LocalDateTime timestamp, Integer departmentID)
    {
        String result;
        
        JsonObject employees = new JsonObject();
        JsonArray jsonData = new JsonArray();
        JsonArray fullTimeCI = new JsonArray();
        JsonArray tempCI = new JsonArray();
        JsonArray fullTimeCO = new JsonArray();
        JsonArray tempCO = new JsonArray();
        
        String employeeTypeString, inOrOut = "Out";
        Boolean wasClockedIn = false, isFullTime;
        
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        PreparedStatement ps;
        ResultSet rs;
        
        /* Choose which query to use */
        String query;
        
        if (departmentID == null) {
            query = MANY_QUERY;
        }
        else {
            query = SINGLE_QUERY;
        }
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
           /* Run employee Table Query */
            if (departmentID == null) {
                ps = conn.prepareStatement(query);
            }
            else {
                ps = conn.prepareStatement(query);
                ps.setInt(1, departmentID);
            }
            
            boolean hasResults = ps.execute();
            
            if (hasResults) 
            {
                rs = ps.getResultSet();
                
                while (rs.next())
                {
                    if (rs.getInt("employeetypeid") == 1)
                    {
                        employeeTypeString = "Full-Time Employee";
                        isFullTime = true;
                    }
                    else
                    {
                        employeeTypeString = "Temporary Employee";
                        isFullTime = false;
                    }
                    
                    /* TODO: Fix logic with calulating status for employee */
                    
                    /* Create a variables for getting punch list */
                    Badge badge = badgeDAO.find(rs.getString("badgeid"));
                    
                    ArrayList<Punch> punchList = punchDAO.list(badge, timestamp.toLocalDate());
                    
                    /* Loop through the punch list */
                    for (Punch p : punchList) 
                    {
                        //if (employee is clocked in)
                        if (p.getOriginaltimestamp().isBefore(timestamp))
                        {
                            //assign the wasClockedIn variable as true and assign inOut String as In
                            wasClockedIn = true;
                            //also add an arrived timestamp at the beginning for when the clocked in
                            employees.put("arrived", p.printOriginal());
                        }
                        
                        /* If find matching pair */
                        if (wasClockedIn && p.getOriginaltimestamp().isAfter(timestamp))
                        {
                            /* Break out the loop */
                            break;
                        }
                        else 
                        {
                            /* Else set clockedIn to false and look for another pair */
                            wasClockedIn = false;
                        }
                        
                    }

                    employees.put("employeetype", employeeTypeString);
                    employees.put("firstname", rs.getString("firstname"));
                    employees.put("badgeid", rs.getString("badgeid"));
                    employees.put("shift", rs.getInt("shiftid"));
                    employees.put("lastname", rs.getString("lastname"));
                    employees.put("status", inOrOut);

                    //if (clockedIn && isFullTime == true)
                    if (wasClockedIn && isFullTime) 
                    {
                       //add to fullTimeCI array
                       fullTimeCI.add(employees);
                    }
                    else if (!wasClockedIn && isFullTime)
                    {
                        //add to fullTimeCI array
                       fullTimeCO.add(employees);
                    }
                    else if (wasClockedIn && !isFullTime)
                    {
                        tempCI.add(employees);
                    }
                    else if (!wasClockedIn && !isFullTime)
                    {
                        tempCO.add(employees);
                    }
                    
                }
                                
                //append list together in correct order
                jsonData.addAll(fullTimeCI);
                jsonData.addAll(tempCI);
                jsonData.addAll(fullTimeCO);
                jsonData.addAll(tempCO);
                
            }
            
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        
        //return final result
        result = Jsoner.serialize(jsonData);
        
        return result;
    }
    
    public String getAbsenteeismHistory(Integer employeeId) {
        
        String result;
        
        BigDecimal lifetime;
        ArrayList<BigDecimal> lifetimeList = new ArrayList<>();
        
        JsonArray historyData = new JsonArray();
        JsonObject jsonData = new JsonObject();
        
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                
                ps = conn.prepareStatement(EMPLOYEEID_QUERY);
                ps.setInt(1, employeeId);

                boolean hasResults = ps.execute();

                if (hasResults) {
                    
                    rs = ps.getResultSet();
                    
                    if (rs.next()) {
                            
                        /* Search through first results and assign values */
                        
                        /* Find a badge object for complete description */
                        Badge b = badgeDAO.find(rs.getString("badgeid"));

                        /* Find a department object for complete description */
                        Department d = departmentDAO.find(rs.getInt("departmentid"));

                        /* Put all of the data into the HashMap */
                        jsonData.put("badgeid", b.getId());
                        jsonData.put("name", b.getDescription());
                        jsonData.put("department", d.getDescription());
                        
                    }
                }
                
                /* Use acending query to calculate lifetime across payperiods */
                ps = conn.prepareStatement(ABSENTEEISMACEND_QUERY);
                ps.setInt(1, employeeId);
                
                hasResults = ps.execute();
                
                if (hasResults) {
                    
                    rs = ps.getResultSet();
                    
                    /* Create local variables for average percentage across payperiods */
                    float percentage = 0f;
                    int count = 1;
                    
                    while (rs.next()) {
                    
                        /* Calculate Average for each payperiod */
                        percentage += rs.getDouble("percentage");
                        lifetime = BigDecimal.valueOf(percentage / count).setScale(3, RoundingMode.HALF_EVEN); // TODO: Something wrong with this
                        lifetime = lifetime.setScale(2, RoundingMode.HALF_EVEN); // TODO: Something wrong with this
                        lifetimeList.add(lifetime);
                        count++;
                        
                    }
                    
                }
                
                /* Use query to get absenteeism data */
                ps = conn.prepareStatement(ABSENTEEISMDESC_QUERY);
                ps.setInt(1, employeeId);
                
                hasResults = ps.execute();
                
                if (hasResults) {
                    
                    rs = ps.getResultSet();
                    
                    /* Create count variable for searching through lifetimeList */
                    int count = lifetimeList.size()-1;
                    
                    while (rs.next()) {
                        
                        /* Create JsonObject for each record */
                        JsonObject recordData = new JsonObject();
                        
                        /* Add payperiod to JsonObject */
                        recordData.put("payperiod", rs.getDate("payperiod").toString());
                        
                        /* Add absenteeism percentage to JsonObject */
                        recordData.put("percentage", rs.getBigDecimal("percentage").setScale(2));
                        
                        /* Add lifetime value from lifetimeList to JsonObject */
                        recordData.put("lifetime", lifetimeList.get(count));
                        count--;
                        
                        /* Add record into history data */
                        historyData.add(recordData);
                        
                    }
                    
                    /* Add completed history to jsonData */
                    jsonData.put("absenteeismhistory", historyData);
                    
                }
                    
            }
                
            /* If no data available, print an error */

            else {

                System.err.println("No Data Found!");

            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        result = Jsoner.serialize(jsonData);
        
        return result;
        
    }
}
