package edu.jsu.mcis.cs310.tas_fa23.dao;


import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

public class ReportDAO { 
    
    private final String Query_1 = "SELECT * FROM employee WHERE departmentid = ? ORDER BY lastname, firstname ";
    private final String Query_2 = "SELECT * FROM employee ORDER BY lastname, firstname ";
    private final DAOFactory daoFactory;
    
    public ReportDAO(DAOFactory daoFactory) {
        
        this.daoFactory = daoFactory;
    }
    public ArrayList<ReportDAO> list() {
        
        ArrayList<ReportDAO> result = new ArrayList<>();
        boolean hasResults;
        
        PreparedStatement ps = null;
        ResultSet rs = null;
        
        try {
            
            Connection conn = daoFactory.getConnection();
            
            if (conn.isValid(0)) {
                // Use prepared statement to search for a match between dates and a badge id
                ps = conn.prepareStatement(Query_1);
              
                ps.setString(,employee );

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {

                        String description = rs.getString("description");
                        b = new (id, description);

                    }
                    
                    //ArrayList<HashMap<String,String>>

                }
                    
            }
                
            /* If no data available, print an error */

            else {

                System.err.println(Jsoner.prettyPrint(jsonExpectedString));

            }
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
        finally {
            
            if (rs != null) { try { rs.close(); } catch (Exception e) { e.printStackTrace(); } }
            if (ps != null) { try { ps.close(); } catch (Exception e) { e.printStackTrace(); } }
            
        }
        
        return result;
    }
}
