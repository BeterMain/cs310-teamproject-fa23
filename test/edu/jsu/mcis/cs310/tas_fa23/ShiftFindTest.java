package edu.jsu.mcis.cs310.tas_fa23;

import edu.jsu.mcis.cs310.tas_fa23.dao.ShiftDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;
import org.junit.*;
import static org.junit.Assert.*;

public class ShiftFindTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testFindShiftByID1() {

        ShiftDAO shiftDAO = daoFactory.getShiftDAO();

        /* Retrieve Shift Rulesets from Database */
        
        Shift s1 = shiftDAO.find(1);
        Shift s2 = shiftDAO.find(2);
        Shift s3 = shiftDAO.find(3);

        /* Compare to Expected Values */
        
        assertEquals("Shift 1: 07:00 - 15:30 (510 minutes); Lunch: 12:00 - 12:30 (30 minutes)", s1.toString());
        assertEquals("Shift 2: 12:00 - 20:30 (510 minutes); Lunch: 16:30 - 17:00 (30 minutes)", s2.toString());
        assertEquals("Shift 1 Early Lunch: 07:00 - 15:30 (510 minutes); Lunch: 11:30 - 12:00 (30 minutes)", s3.toString());

    }

    @Test
    public void testFindShiftByBadge1() {

        ShiftDAO shiftDAO = daoFactory.getShiftDAO();
        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();

        /* Create Badge Objects */
        
        Badge b1 = badgeDAO.find("B6902696");
        Badge b2 = badgeDAO.find("76E920D9");
        Badge b3 = badgeDAO.find("4382D92D");

        /* Retrieve Shift Rulesets from Database */
        
        Shift s1 = shiftDAO.find(b1);
        Shift s2 = shiftDAO.find(b2);
        Shift s3 = shiftDAO.find(b3);

        /* Compare to Expected Values */
        
        assertEquals("Shift 1: 07:00 - 15:30 (510 minutes); Lunch: 12:00 - 12:30 (30 minutes)", s1.toString());
        assertEquals("Shift 2: 12:00 - 20:30 (510 minutes); Lunch: 16:30 - 17:00 (30 minutes)", s2.toString());
        assertEquals("Shift 1 Early Lunch: 07:00 - 15:30 (510 minutes); Lunch: 11:30 - 12:00 (30 minutes)", s3.toString());
    }

}
