package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Department;
import edu.jsu.mcis.cs310.tas_fa23.Employee;
import edu.jsu.mcis.cs310.tas_fa23.EmployeeType;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class EmployeeDAO {
    
    private static final String QUERY_ID_FIND = "SELECT * FROM employee WHERE id = ?";
    private static final String QUERY_BADGEID_FIND = "SELECT * FROM employee WHERE badgeid = ?";
    
    private final DAOFactory daoFactory;
    
    EmployeeDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }
    
    /* Create Find Methods */
    
    public Employee find(int id) {
        // Search database based on int id
        
        Employee employee = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_ID_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    if (rs.next()) {
                        
                        // Get the name of the employee
                        String firstName = rs.getString("firstname");
                        String middleName = rs.getString("middlename");
                        String lastName = rs.getString("lastname");
                        
                        // Get the date the employee was active
                        LocalDateTime active = rs.getTimestamp("active").toLocalDateTime();
                        
                        // Create a new Badge object that has the required badge field
                        StringBuilder s = new StringBuilder(lastName);
                        s.append(", ").append(firstName).append(" ").append(middleName);
                        Badge badge = new Badge(rs.getString("badgeid"),s.toString());
                        
                        // Create a new Department object that has the required department field
                        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
                        Department department = departmentDAO.find(rs.getInt("departmentid"));
                        
                        ShiftDAO shiftDAO = daoFactory.getShiftDAO();
                        Shift shift = shiftDAO.find(rs.getInt("shiftid"));
                        
                        // Create a EmployeeType object and assign the correct description
                        EmployeeType employeeType = null;
                        switch(rs.getInt("employeetypeid")) {
                            case 0:
                                employeeType = EmployeeType.PART_TIME;
                                break;
                            case 1:
                                employeeType = EmployeeType.FULL_TIME;
                                break;
                        }
                        
                        // Set the return variable
                        HashMap<String, Object> parameterMap = new HashMap<>();
                        parameterMap.put("id", id);
                        parameterMap.put("firstName", firstName);
                        parameterMap.put("middleName", middleName);
                        parameterMap.put("lastName", lastName);
                        parameterMap.put("active", active);
                        parameterMap.put("badge", badge);
                        parameterMap.put("department", department);
                        parameterMap.put("shift", shift);
                        parameterMap.put("employeeType", employeeType);
                        employee = new Employee(parameterMap);
                    }

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

        return employee;

    }
    
    public Employee find(Badge b) {
        // Search database based on Badge ID
        
        Employee employee = null;
        
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_BADGEID_FIND);
                ps.setString(1, b.getId());

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    if (rs.next()) {
                        
                        // Get the name of the employee
                        String firstName = rs.getString("firstname");
                        String middleName = rs.getString("middlename");
                        String lastName = rs.getString("lastname");
                        
                        // Get the date the employee was active
                        LocalDateTime active = rs.getTimestamp("active").toLocalDateTime();
                        
                        // Create a new Badge object that has the required badge field
                        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
                        Badge badge = badgeDAO.find(rs.getString("badgeid"));
                        
                        // Create a new Department object that has the required department field
                        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
                        Department department = departmentDAO.find(rs.getInt("departmentid"));
                        
                        // Add Shift Object
                        ShiftDAO shiftDAO = daoFactory.getShiftDAO();
                        Shift shift = shiftDAO.find(b);
                        
                        // Create a EmployeeType object and assign the correct description
                        EmployeeType employeeType = null;
                        switch(rs.getInt("employeetypeid")) {
                            case 0:
                                employeeType = EmployeeType.PART_TIME;
                                break;
                            case 1:
                                employeeType = EmployeeType.FULL_TIME;
                                break;
                        }
                        
                        // Set the return variable
                        HashMap<String, Object> parameterMap = new HashMap<>();
                        parameterMap.put("id", rs.getInt("id"));
                        parameterMap.put("firstName", firstName);
                        parameterMap.put("middleName", middleName);
                        parameterMap.put("lastName", lastName);
                        parameterMap.put("active", active);
                        parameterMap.put("badge", badge);
                        parameterMap.put("department", department);
                        parameterMap.put("shift", shift);
                        parameterMap.put("employeeType", employeeType);
                        employee = new Employee(parameterMap);
                    }

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

        return employee;

    }
    
}
