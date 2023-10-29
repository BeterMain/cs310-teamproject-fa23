
package edu.jsu.mcis.cs310.tas_fa23;

import edu.jsu.mcis.cs310.tas_fa23.dao.BadgeDAO;
import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_fa23.dao.PunchDAO;
import java.time.*;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;

public class PunchRangeListTest {
    
    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testPunchRangeList1() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate tsBegin = LocalDate.of(2018, Month.SEPTEMBER, 17);
        LocalDate tsEnd = LocalDate.of(2018, Month.SEPTEMBER, 18);

        Badge b = badgeDAO.find("67637925");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, tsBegin, tsEnd);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(4716));
        p2.add(punchDAO.find(4811));
        p2.add(punchDAO.find(4813));
        p2.add(punchDAO.find(4847));
        p2.add(punchDAO.find(4884));
        p2.add(punchDAO.find(4949));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }

    @Test
    public void testPunchRangeList2() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate tsBegin = LocalDate.of(2018, Month.SEPTEMBER, 01);
        LocalDate tsEnd = LocalDate.of(2018, Month.SEPTEMBER, 20);

        Badge b = badgeDAO.find("87176FD7");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, tsBegin, tsEnd);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        
        p2.add(punchDAO.find(3666));
        p2.add(punchDAO.find(3672));
        p2.add(punchDAO.find(3894));
        p2.add(punchDAO.find(3958));
        p2.add(punchDAO.find(4044));
        p2.add(punchDAO.find(4094));
        p2.add(punchDAO.find(4100));
        p2.add(punchDAO.find(4123));
        p2.add(punchDAO.find(4225));
        p2.add(punchDAO.find(4260));
        p2.add(punchDAO.find(4265));
        p2.add(punchDAO.find(4272));
        p2.add(punchDAO.find(4381));
        p2.add(punchDAO.find(4409));
        p2.add(punchDAO.find(4418));
        p2.add(punchDAO.find(4426));
        p2.add(punchDAO.find(4745));
        p2.add(punchDAO.find(4827));
        p2.add(punchDAO.find(4918));
        p2.add(punchDAO.find(4956));
        p2.add(punchDAO.find(5048));
        p2.add(punchDAO.find(5103));
        p2.add(punchDAO.find(5213));
        p2.add(punchDAO.find(5249));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }

    @Test
    public void testPunchRangeList3() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate tsBegin = LocalDate.of(2018, Month.AUGUST, 01);
        LocalDate tsEnd = LocalDate.of(2018, Month.AUGUST, 02);

        Badge b = badgeDAO.find("95497F63");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, tsBegin, tsEnd);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(191));
        p2.add(punchDAO.find(209));
        p2.add(punchDAO.find(297));
        p2.add(punchDAO.find(328));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }
    
    @Test
    public void testPunchRangeList4() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate tsBegin = LocalDate.of(2018, Month.JANUARY, 01);
        LocalDate tsEnd = LocalDate.of(2018, Month.DECEMBER, 20);

        Badge b = badgeDAO.find("021890C0");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, tsBegin, tsEnd);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(4181));
        p2.add(punchDAO.find(4202));
        p2.add(punchDAO.find(4315));
        p2.add(punchDAO.find(4382));
        p2.add(punchDAO.find(4468));
        p2.add(punchDAO.find(4526));
        p2.add(punchDAO.find(4565));
        p2.add(punchDAO.find(4649));
        p2.add(punchDAO.find(4686));
        p2.add(punchDAO.find(4728));
        p2.add(punchDAO.find(4861));
        p2.add(punchDAO.find(5671));
        p2.add(punchDAO.find(5672));
        p2.add(punchDAO.find(5665));
        p2.add(punchDAO.find(5673));
        p2.add(punchDAO.find(5666));
        p2.add(punchDAO.find(5674));
        p2.add(punchDAO.find(5377));
        p2.add(punchDAO.find(5515));
        p2.add(punchDAO.find(5516));
        p2.add(punchDAO.find(5667));
        p2.add(punchDAO.find(5633));
        p2.add(punchDAO.find(5718));
        p2.add(punchDAO.find(5780));
        p2.add(punchDAO.find(5879));
        p2.add(punchDAO.find(5909));
        p2.add(punchDAO.find(5984));
        p2.add(punchDAO.find(6072));
        p2.add(punchDAO.find(6162));
        p2.add(punchDAO.find(6218));
        p2.add(punchDAO.find(6305));
        p2.add(punchDAO.find(6352));
        p2.add(punchDAO.find(6392));
        p2.add(punchDAO.find(6402));
        p2.add(punchDAO.find(6432));
        

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }
    
    @Test
    public void testPunchRangeList5() {

        BadgeDAO badgeDAO = daoFactory.getBadgeDAO();
        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Create StringBuilders for Test Output */
        
        StringBuilder s1 = new StringBuilder();
        StringBuilder s2 = new StringBuilder();

        /* Create Timestamp and Badge Objects for Punch List */
        
        LocalDate tsBegin = LocalDate.of(2018, Month.AUGUST, 01);
        LocalDate tsEnd = LocalDate.of(2018, Month.AUGUST, 20);

        Badge b = badgeDAO.find("ADD650A8");

        /* Retrieve Punch List #1 (created by DAO) */
        
        ArrayList<Punch> p1 = punchDAO.list(b, tsBegin, tsEnd);

        /* Export Punch List #1 Contents to StringBuilder */
        
        for (Punch p : p1) {
            s1.append(p.printOriginal());
            s1.append("\n");
        }

        /* Create Punch List #2 (created manually) */
        
        ArrayList<Punch> p2 = new ArrayList<>();

        /* Add Punches */
        p2.add(punchDAO.find(189));
        p2.add(punchDAO.find(246));
        p2.add(punchDAO.find(996));
        p2.add(punchDAO.find(1054));
        p2.add(punchDAO.find(1100));
        p2.add(punchDAO.find(1168));
        p2.add(punchDAO.find(1201));
        p2.add(punchDAO.find(1239));
        p2.add(punchDAO.find(1345));
        p2.add(punchDAO.find(1402));
        p2.add(punchDAO.find(1460));
        p2.add(punchDAO.find(1520));
        p2.add(punchDAO.find(1580));
        p2.add(punchDAO.find(1970));
        p2.add(punchDAO.find(1919));
        p2.add(punchDAO.find(2000));

        /* Export Punch List #2 Contents to StringBuilder */
        
        for (Punch p : p2) {
            s2.append(p.printOriginal());
            s2.append("\n");
        }

        /* Compare Output Strings */
        
        assertEquals(s2.toString(), s1.toString());

    }
    
}
