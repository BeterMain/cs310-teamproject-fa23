package edu.jsu.mcis.cs310.tas_fa23.dao;


import com.github.cliftonlabs.json_simple.Jsoner;
import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Department;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class ReportDAO { 
    
    private final String SINGLEQUERY = "SELECT * FROM employee WHERE departmentid = ? ORDER BY lastname, firstname ";
    private final String MANYQUERY = "SELECT * FROM employee ORDER BY lastname, firstname ";
    private final String EMPLOYEETYPEQUERY = "SELECT * FROM employeetype WHERE id = ?";
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
            query = MANYQUERY;
        }
        else {
            query = SINGLEQUERY; 
        }
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs2 = null;
        
        
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
                        ps = conn.prepareStatement(EMPLOYEETYPEQUERY);
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
}
