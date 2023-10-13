package edu.jsu.mcis.cs310.tas_fa23;

import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_fa23.dao.PunchDAO;
import org.junit.*;
import static org.junit.Assert.*;

public class PunchFindTest {

    private DAOFactory daoFactory;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");

    }

    @Test
    public void testFindPunches1() {

        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Retrieve Punches from Database */
        
        Punch p1 = punchDAO.find(3433);
        Punch p2 = punchDAO.find(3325);
        Punch p3 = punchDAO.find(1963);

        /* Compare to Expected Values */
        
        assertEquals("#D2C39273 CLOCK IN: WED 09/05/2018 07:00:07", p1.printOriginal());
        assertEquals("#DFD9BB5C CLOCK IN: TUE 09/04/2018 08:00:00", p2.printOriginal());
        assertEquals("#99F0C0FA CLOCK IN: SAT 08/18/2018 06:00:00", p3.printOriginal());

    }

    @Test
    public void testFindPunches2() {

        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Retrieve Punches from Database */

        Punch p4 = punchDAO.find(5702);
        Punch p5 = punchDAO.find(4976);
        Punch p6 = punchDAO.find(2193);

        /* Compare to Expected Values */

        assertEquals("#0FFA272B CLOCK OUT: MON 09/24/2018 17:30:04", p4.printOriginal());
        assertEquals("#FCE87D9F CLOCK OUT: TUE 09/18/2018 17:34:00", p5.printOriginal());
        assertEquals("#FCE87D9F CLOCK OUT: MON 08/20/2018 17:30:00", p6.printOriginal());

    }
    
    @Test
    public void testFindPunches3() {

        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Retrieve Punches from Database */

        Punch p7 = punchDAO.find(954);
        Punch p8 = punchDAO.find(258);
        Punch p9 = punchDAO.find(717);

        /* Compare to Expected Values */

        assertEquals("#618072EA TIME OUT: FRI 08/10/2018 00:12:35", p7.printOriginal());
        assertEquals("#0886BF12 TIME OUT: THU 08/02/2018 06:06:38", p8.printOriginal());
        assertEquals("#67637925 TIME OUT: TUE 08/07/2018 23:12:34", p9.printOriginal());

    }
    
    @Test
    public void testFindPunches4() {

        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Retrieve Punches from Database */

        Punch p7 = punchDAO.find(188);
        Punch p8 = punchDAO.find(189);
        Punch p9 = punchDAO.find(190);

        /* Compare to Expected Values */

        assertEquals("#DD6E2C0C CLOCK IN: WED 08/01/2018 06:59:19", p7.printOriginal());
        assertEquals("#ADD650A8 CLOCK IN: WED 08/01/2018 06:59:33", p8.printOriginal());
        assertEquals("#D2C39273 CLOCK IN: WED 08/01/2018 06:59:38", p9.printOriginal());

    }
    
    @Test
    public void testFindPunches5() {

        PunchDAO punchDAO = daoFactory.getPunchDAO();

        /* Retrieve Punches from Database */

        Punch p7 = punchDAO.find(383);
        Punch p8 = punchDAO.find(1179);
        Punch p9 = punchDAO.find(999);

        /* Compare to Expected Values */

        assertEquals("#28DC3FB8 CLOCK IN: FRI 08/03/2018 06:50:19", p7.printOriginal());
        assertEquals("#4E04B5FE CLOCK IN: MON 08/13/2018 06:50:05", p8.printOriginal());
        assertEquals("#08D745A6 CLOCK IN: FRI 08/10/2018 06:59:34", p9.printOriginal());

    }
    
}
