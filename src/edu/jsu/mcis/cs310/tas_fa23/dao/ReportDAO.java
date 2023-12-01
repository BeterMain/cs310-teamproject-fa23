package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.EmployeeType;
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
import java.time.format.DateTimeFormatter;


public class ReportDAO { 
    
    private final String SINGLE_QUERY = "SELECT * FROM employee WHERE departmentid = ? ORDER BY lastname, firstname ";
    private final String MANY_QUERY = "SELECT * FROM employee ORDER BY lastname, firstname ";
    private final String EMPLOYEETYPE_QUERY = "SELECT * FROM employeetype WHERE id = ?";
    
    private final String EMPLOYEETYPE_QUERY2 = "SELECT e.*, et.description AS employeetype, s.description AS shift,"
            + " b.id AS badgeid, b.description AS name, d.description AS department FROM employee e JOIN employeetype et "
            + "ON e.employeetypeid = et.id JOIN shift s ON e.shiftid = s.id JOIN badge b ON e.badgeid = b.id JOIN department d "
            +"ON e.departmentid = d.id WHERE e.departmentid = ? ORDER BY d.description, firstname,lastname, e.middlename";
    
    private final String SHIFTDESC_QUERY = "SELECT * FROM shift WHERE id = ?";
    private final String EMPLOYEEID_QUERY = "SELECT * FROM employee WHERE id = ?";
    private final String ABSENTEEISMACEND_QUERY = "SELECT * FROM absenteeism WHERE employeeid = ? ORDER BY payperiod";
    private final String ABSENTEEISMDESC_QUERY  = "SELECT * FROM absenteeism WHERE employeeid = ? ORDER BY payperiod desc";
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
        
        JsonArray jsonData = new JsonArray();
        JsonArray fullTimeCI = new JsonArray();
        JsonArray tempCI = new JsonArray();
        JsonArray fullTimeCO = new JsonArray();
        JsonArray tempCO = new JsonArray();
        
        String employeeTypeString;
        Boolean CI = false;
        
        PunchDAO punchDAO = daoFactory.getPunchDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        
        PreparedStatement ps;
        ResultSet rs, rs2 = null;
        
        LocalDateTime arrivalTime = null;
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("dd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MM");
        
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
                    ps = conn.prepareStatement(SHIFTDESC_QUERY);
                    ps.setInt(1, rs.getInt("shiftid"));
                    Boolean hasAResult = false;
                    String shiftString = "";
                    
                    hasAResult = ps.execute();
                    
                    if (hasAResult)
                    {
                        rs2 = ps.executeQuery();
                    }
                    
                    while(rs2.next())
                    {
                        shiftString = rs2.getString("description");
                    }
                    
                    Boolean wasClockedIn = false, isFullTime = false;
                    
                    JsonObject employees = new JsonObject();
                    String inOrOut = "Out", dayOfWeek = "";
                    
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
                        //if (employee is clocked in && was before the timestamp)
                        if (p.getPunchType() == p.getPunchType().CLOCK_IN && p.getOriginaltimestamp().isBefore(timestamp))
                        {
                            CI = true;
                            arrivalTime = p.getOriginaltimestamp();
                            dayOfWeek = arrivalTime.toLocalDate().getDayOfWeek().toString();
                            dayOfWeek = dayOfWeek.substring(0, 3);
                        }
                            
                        //if has a valid clock in and a clock out punch is done after the timestamp
                        if (CI == true && p.getPunchType() == p.getPunchType().CLOCK_OUT && p.getOriginaltimestamp().isAfter(timestamp))
                        {
                            /* If find matching pair */
                            /* Break out the loop */
                            //assign the wasClockedIn variable as true and assign inOut String as In
                            //also add an arrived timestamp at the beginning for when the clocked in
                            employees.put("arrived", dayOfWeek + " " + arrivalTime.toLocalDate().format(monthFormatter)
                                + "/" + arrivalTime.toLocalDate().format(dayFormatter) + "/" + arrivalTime.toLocalDate().getYear()
                                + " " + arrivalTime.toLocalTime());
                            CI = false;
                            wasClockedIn = true;
                            inOrOut = "In";
                            break;
                        }

                        //if clockout was before the timestamp reset the loop
                        else if (CI == true && p.getPunchType() == p.getPunchType().CLOCK_OUT && p.getOriginaltimestamp().isBefore(timestamp))
                        {
                            /* Else set clockedIn to false and look for another pair */
                        
                            CI = false;
                        }
                        
                        
                    }

                    employees.put("employeetype", employeeTypeString);
                    employees.put("firstname", rs.getString("firstname"));
                    employees.put("badgeid", rs.getString("badgeid"));
                    employees.put("shift", shiftString);
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
    public String getHoursSummary(LocalDate date, Integer departmentId, EmployeeType employee) {        
        
        ArrayList<HashMap<String, String>> hoursSummary = new ArrayList<>();

        PreparedStatement ps = null;
        
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {
                if (departmentId != null) {

                } else {

                }
                
                ps = conn.prepareStatement(EMPLOYEETYPE_QUERY);
                
                rs = ps.executeQuery();

            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }

        return Jsoner.serialize(hoursSummary);

    }
    public String getEmployeeSummary(Integer departmentId) {
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();

        PreparedStatement ps = null;
        
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                if (departmentId == null) {
                    
                    ps = conn.prepareStatement(EMPLOYEETYPE_QUERY);
                
                } else {
                    
                    ps = conn.prepareStatement(EMPLOYEETYPE_QUERY2);
                    
                    ps.setInt(1, departmentId);
                }

                rs = ps.executeQuery();

                while (rs.next()) {
                    
                    HashMap<String, String> employeeData = new HashMap<>();
                    
                    String F_name = rs.getString("firstname");
                    
                    String M_name = rs.getString("middlename");
                    
                    String L_name = rs.getString("lastname");
                    
                    String EmployeeType = rs.getString("employeetype");
                    
                    String Shift = rs.getString("shift");
                    
                    String Department = rs.getString("department");
                    
                    LocalDate Active = rs.getDate("active").toLocalDate();
                    
                    String Badgeid = rs.getString("badgeid");
                    
                    
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");

                    
                    employeeData.put("firstname", F_name);
                    
                    employeeData.put("middlename", M_name);
                    
                    employeeData.put("lastname", L_name);
                    
                    employeeData.put("active", Active.format(formatter));
                    
                    employeeData.put("badgeid", Badgeid);
                    
                    employeeData.put("employeetype", EmployeeType);
                    
                    employeeData.put("shift", Shift);
                    
                    employeeData.put("department", Department);
                    
                    jsonData.add(employeeData);
                }

            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }

        return Jsoner.serialize(jsonData);

    }

}
