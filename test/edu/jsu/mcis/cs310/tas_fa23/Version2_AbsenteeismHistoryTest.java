package edu.jsu.mcis.cs310.tas_fa23;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import org.junit.*;
import static org.junit.Assert.*;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.dao.*;

public class Version2_AbsenteeismHistoryTest {
    
    private final int NUM_PAY_PERIODS = 7;

    private AbsenteeismDAO absenteeismDao;
    private DAOFactory daoFactory;
    private EmployeeDAO employeeDao;
    private ReportDAO reportDao;
    private PunchDAO punchDao;

    @Before
    public void setup() {

        daoFactory = new DAOFactory("tas.jdbc");
        
        absenteeismDao = daoFactory.getAbsenteeismDAO();
        employeeDao = daoFactory.getEmployeeDAO();
        punchDao = daoFactory.getPunchDAO();
        reportDao = daoFactory.getReportDAO();

    }
    
    @Test
    public void testAbsenteeismHistory1() {
        
        JsonObject jsonExpected = null, jsonActual = null;
        
        int employeeid = 29;
        
        try {
            
            clearAndComputeAbsenteeism(employeeid);
        
            String jsonExpectedString = "{\"badgeid\":\"3DA8B226\",\"absenteeismhistory\":[{\"payperiod\":\"2018-09-16\",\"percentage\":\"5.62\",\"lifetime\":\"1.43\"},{\"payperiod\":\"2018-09-09\",\"percentage\":\"-30.00\",\"lifetime\":\"0.73\"},{\"payperiod\":\"2018-09-02\",\"percentage\":\"75.00\",\"lifetime\":\"6.88\"},{\"payperiod\":\"2018-08-26\",\"percentage\":\"6.88\",\"lifetime\":\"-10.16\"},{\"payperiod\":\"2018-08-19\",\"percentage\":\"-35.62\",\"lifetime\":\"-15.83\"},{\"payperiod\":\"2018-08-12\",\"percentage\":\"-2.50\",\"lifetime\":\"-5.94\"},{\"payperiod\":\"2018-08-05\",\"percentage\":\"-9.38\",\"lifetime\":\"-9.38\"}],\"name\":\"Hamm, Doris R\",\"department\":\"Hafting\"}";
            jsonExpected = (JsonObject)Jsoner.deserialize(jsonExpectedString);
            
            /* Get "Absenteeism History" Report */

            String jsonActualString = reportDao.getAbsenteeismHistory(employeeid);
            jsonActual = (JsonObject)Jsoner.deserialize(jsonActualString);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        /* Compare to Expected Values */
        
        assertNotNull(jsonExpected);
        assertNotNull(jsonActual);
        assertEquals(jsonExpected, jsonActual);

    }
    
    @Test
    public void testAbsenteeismHistory2() {
        
        JsonObject jsonExpected = null, jsonActual = null;
        
        int employeeid = 73;
        
        try {
            
            clearAndComputeAbsenteeism(employeeid);
        
            String jsonExpectedString = "{\"badgeid\":\"99F0C0FA\",\"absenteeismhistory\":[{\"payperiod\":\"2018-09-16\",\"percentage\":\"15.00\",\"lifetime\":\"-4.91\"},{\"payperiod\":\"2018-09-09\",\"percentage\":\"-33.12\",\"lifetime\":\"-8.23\"},{\"payperiod\":\"2018-09-02\",\"percentage\":\"-10.00\",\"lifetime\":\"-3.25\"},{\"payperiod\":\"2018-08-26\",\"percentage\":\"-20.00\",\"lifetime\":\"-1.56\"},{\"payperiod\":\"2018-08-19\",\"percentage\":\"46.25\",\"lifetime\":\"4.58\"},{\"payperiod\":\"2018-08-12\",\"percentage\":\"-22.50\",\"lifetime\":\"-16.25\"},{\"payperiod\":\"2018-08-05\",\"percentage\":\"-10.00\",\"lifetime\":\"-10.00\"}],\"name\":\"Kite, Ernest A\",\"department\":\"Assembly\"}";
            jsonExpected = (JsonObject)Jsoner.deserialize(jsonExpectedString);
            
            /* Get "Absenteeism History" Report */

            String jsonActualString = reportDao.getAbsenteeismHistory(employeeid);
            jsonActual = (JsonObject)Jsoner.deserialize(jsonActualString);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        /* Compare to Expected Values */
        
        assertNotNull(jsonExpected);
        assertNotNull(jsonActual);
        assertEquals(jsonExpected, jsonActual);

    }
    
    @Test
    public void testAbsenteeismHistory3() {
        
        JsonObject jsonExpected = null, jsonActual = null;
        
        int employeeid = 8;
        
        try {
            
            clearAndComputeAbsenteeism(employeeid);
        
            String jsonExpectedString = "{\"badgeid\":\"0FFA272B\",\"absenteeismhistory\":[{\"payperiod\":\"2018-09-16\",\"percentage\":\"55.00\",\"lifetime\":\"54.02\"},{\"payperiod\":\"2018-09-09\",\"percentage\":\"19.38\",\"lifetime\":\"53.85\"},{\"payperiod\":\"2018-09-02\",\"percentage\":\"42.50\",\"lifetime\":\"60.75\"},{\"payperiod\":\"2018-08-26\",\"percentage\":\"80.00\",\"lifetime\":\"65.31\"},{\"payperiod\":\"2018-08-19\",\"percentage\":\"100.00\",\"lifetime\":\"60.42\"},{\"payperiod\":\"2018-08-12\",\"percentage\":\"61.25\",\"lifetime\":\"40.62\"},{\"payperiod\":\"2018-08-05\",\"percentage\":\"20.00\",\"lifetime\":\"20.00\"}],\"name\":\"Corwin, John L\",\"department\":\"Press\"}";
            jsonExpected = (JsonObject)Jsoner.deserialize(jsonExpectedString);
            
            /* Get "Absenteeism History" Report */

            String jsonActualString = reportDao.getAbsenteeismHistory(employeeid);
            jsonActual = (JsonObject)Jsoner.deserialize(jsonActualString);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        /* Compare to Expected Values */
        
        assertNotNull(jsonExpected);
        assertNotNull(jsonActual);
        assertEquals(jsonExpected, jsonActual);

    }
    
    private void clearAndComputeAbsenteeism(Integer employeeid) {
        
        /* Get Employee and Shift Objects */

        Employee employee = employeeDao.find(employeeid);
        Shift shift = employee.getShift();
        
        /* Clear Absenteeism */
        
        absenteeismDao.clear(employeeid);

        /* Compute Absenteeism (starting with first pay period in test database) */

        LocalDate date = LocalDate.parse("2018-08-05");

        for (int i = 0; i < NUM_PAY_PERIODS; ++i) {

            LocalDate begin = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
            LocalDate end = begin.with(TemporalAdjusters.next(DayOfWeek.SATURDAY));

            ArrayList<Punch> punchList = punchDao.list(employee.getBadge(), begin, end);

            for (Punch p : punchList) {
                p.adjust(shift);
            }

            BigDecimal percentage = DAOUtility.calculateAbsenteeism(punchList, shift);
            Absenteeism absenteeism = new Absenteeism(employee, date, percentage);

            absenteeismDao.create(absenteeism);

            date = date.plus(1, ChronoUnit.WEEKS);

        }
        
    }
    
}
