package edu.jsu.mcis.cs310.tas_fa23;

import edu.jsu.mcis.cs310.tas_fa23.dao.DepartmentDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;
import org.junit.*;
import static org.junit.Assert.*;

public class DepartmentFindTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testFindDepartment1() {

        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();

        /* Retrieve Department from Database */
        
        Department d1 = departmentDAO.find(1);

        /* Compare to Expected Values */
        
        assertEquals("#1 (Assembly), Terminal ID: 103", d1.toString());

    }

    @Test
    public void testFindDepartment2() {

        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();

        /* Retrieve Department from Database */
        
        Department d2 = departmentDAO.find(6);

        /* Compare to Expected Values */
        
        assertEquals("#6 (Office), Terminal ID: 102", d2.toString());

    }

    @Test
    public void testFindDepartment3() {

        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();

        /* Retrieve Department from Database */
        
        Department d3 = departmentDAO.find(8);

        /* Compare to Expected Values */
        
        assertEquals("#8 (Shipping), Terminal ID: 107", d3.toString());

    }
    
    @Test
    public void testFindMaintenance()
    {
        
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
        
        Department Maintenance = departmentDAO.find(10);
        
        assertEquals("#10 (Maintenance), Terminal ID: 104", Maintenance.toString());
    }

    @Test
    public void testFindPress()
    {
        DepartmentDAO departmentDAO = daoFactory.getDepartmentDAO();
        
        Department Press = departmentDAO.find(7);
        
        assertEquals("#7 (Press), Terminal ID: 104", Press.toString());
    }
}