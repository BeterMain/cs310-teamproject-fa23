package edu.jsu.mcis.cs310.tas_fa23;

import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.dao.DAOFactory;
import edu.jsu.mcis.cs310.tas_fa23.dao.ReportDAO;
import org.junit.*;
import static org.junit.Assert.*;

public class Version2_EmployeeSummaryTest {

    private ReportDAO reportDAO;

    @Before
    public void setup() {

        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        reportDAO = daoFactory.getReportDAO();

    }
    
    @Test
    public void testEmployeeSummaryCleaning() {
        
        JsonArray jsonExpected = null, jsonActual = null;
        
        try {
            
            int departmentid = 2;
        
            String jsonExpectedString = "[{\"firstname\":\"Ann\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"E215F3DB\",\"shift\":\"Shift 1\",\"middlename\":\"T\",\"active\":\"09/22/2015\",\"department\":\"Cleaning\",\"lastname\":\"Wright\"},{\"firstname\":\"Cruz\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"9186E711\",\"shift\":\"Shift 1\",\"middlename\":\"C\",\"active\":\"01/17/2016\",\"department\":\"Cleaning\",\"lastname\":\"Adams\"},{\"firstname\":\"Curtis\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"2A5620A0\",\"shift\":\"Shift 1\",\"middlename\":\"M\",\"active\":\"10/16/2015\",\"department\":\"Cleaning\",\"lastname\":\"Eaton\"},{\"firstname\":\"Fredrick\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"B09A75D7\",\"shift\":\"Shift 1\",\"middlename\":\"C\",\"active\":\"11/23/2016\",\"department\":\"Cleaning\",\"lastname\":\"Lawrence\"},{\"firstname\":\"Jose\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"DFE4EB13\",\"shift\":\"Shift 1\",\"middlename\":\"S\",\"active\":\"07/26/2016\",\"department\":\"Cleaning\",\"lastname\":\"Black\"}]";
            jsonExpected = (JsonArray)Jsoner.deserialize(jsonExpectedString);
            
            /* Get "Employee Summary" Report (Cleaning Department) */

            String jsonActualString = reportDAO.getEmployeeSummary(departmentid);
            
            jsonActual = (JsonArray)Jsoner.deserialize(jsonActualString);
            
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
    public void testEmployeeSummaryPress() {
        
        JsonArray jsonExpected = null, jsonActual = null;
        
        try {
            
            int departmentid = 7;
        
            String jsonExpectedString = "[{\"firstname\":\"Amy\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"67637925\",\"shift\":\"Shift 1\",\"middlename\":\"B\",\"active\":\"02/02/2017\",\"department\":\"Press\",\"lastname\":\"Brenner\"},{\"firstname\":\"Carol\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"0D87987C\",\"shift\":\"Shift 1\",\"middlename\":\"L\",\"active\":\"01/17/2016\",\"department\":\"Press\",\"lastname\":\"Trice\"},{\"firstname\":\"Claire\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"1C920A23\",\"shift\":\"Shift 1\",\"middlename\":\"T\",\"active\":\"10/04/2015\",\"department\":\"Press\",\"lastname\":\"Hutchison\"},{\"firstname\":\"John\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"0FFA272B\",\"shift\":\"Shift 1\",\"middlename\":\"L\",\"active\":\"10/11/2015\",\"department\":\"Press\",\"lastname\":\"Corwin\"},{\"firstname\":\"Katherine\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"1B2052DE\",\"shift\":\"Shift 1\",\"middlename\":\"H\",\"active\":\"09/22/2015\",\"department\":\"Press\",\"lastname\":\"Sanchez\"},{\"firstname\":\"Kathleen\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"229324A4\",\"shift\":\"Shift 1\",\"middlename\":\"C\",\"active\":\"02/02/2017\",\"department\":\"Press\",\"lastname\":\"Donaldson\"},{\"firstname\":\"Michael\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"8001201A\",\"shift\":\"Shift 1\",\"middlename\":\"E\",\"active\":\"10/11/2015\",\"department\":\"Press\",\"lastname\":\"Stevens\"}]";
            jsonExpected = (JsonArray)Jsoner.deserialize(jsonExpectedString);
            
            /* Get "Employee Summary" Report (Press Department) */

            String jsonActualString = reportDAO.getEmployeeSummary(departmentid);
            
            jsonActual = (JsonArray)Jsoner.deserialize(jsonActualString);
            
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
    public void testEmployeeSummaryShipping() {
        
        JsonArray jsonExpected = null, jsonActual = null;
        
        try {
            
            int departmentid = 8;
        
            String jsonExpectedString = "[{\"firstname\":\"Brian\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"B4BBABEC\",\"shift\":\"Shift 1\",\"middlename\":\"L\",\"active\":\"11/04/2015\",\"department\":\"Shipping\",\"lastname\":\"Lawless\"},{\"firstname\":\"Christine\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"73591F00\",\"shift\":\"Shift 1\",\"middlename\":\"M\",\"active\":\"09/22/2015\",\"department\":\"Shipping\",\"lastname\":\"Gauna\"},{\"firstname\":\"Derrick\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"2305D772\",\"shift\":\"Shift 1\",\"middlename\":\"B\",\"active\":\"09/01/2015\",\"department\":\"Shipping\",\"lastname\":\"Antonio\"},{\"firstname\":\"Elisa\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"B7A6171D\",\"shift\":\"Shift 1\",\"middlename\":\"G\",\"active\":\"09/22/2015\",\"department\":\"Shipping\",\"lastname\":\"Taylor\"},{\"firstname\":\"James\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"9BFCB537\",\"shift\":\"Shift 1\",\"middlename\":\"M\",\"active\":\"09/29/2015\",\"department\":\"Shipping\",\"lastname\":\"Lilly\"},{\"firstname\":\"Jarvis\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"9D527CFB\",\"shift\":\"Shift 1\",\"middlename\":\"B\",\"active\":\"09/22/2015\",\"department\":\"Shipping\",\"lastname\":\"Rodriquez\"},{\"firstname\":\"Kathryn\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"8C4CE4AC\",\"shift\":\"Shift 1\",\"middlename\":\"R\",\"active\":\"12/18/2015\",\"department\":\"Shipping\",\"lastname\":\"Rhoades\"},{\"firstname\":\"Lori\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"4FA55999\",\"shift\":\"Shift 1\",\"middlename\":\"W\",\"active\":\"10/11/2015\",\"department\":\"Shipping\",\"lastname\":\"Davie\"},{\"firstname\":\"Margaret\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"3437588A\",\"shift\":\"Shift 1\",\"middlename\":\"M\",\"active\":\"09/18/2015\",\"department\":\"Shipping\",\"lastname\":\"Mullen\"},{\"firstname\":\"Mitchell\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"E216D413\",\"shift\":\"Shift 1\",\"middlename\":\"C\",\"active\":\"09/29/2015\",\"department\":\"Shipping\",\"lastname\":\"Jones\"},{\"firstname\":\"Nadine\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"B6902696\",\"shift\":\"Shift 1\",\"middlename\":\"P\",\"active\":\"10/09/2015\",\"department\":\"Shipping\",\"lastname\":\"Wright\"},{\"firstname\":\"Nancy\",\"employeetype\":\"Temporary Employee\",\"badgeid\":\"EC531DE6\",\"shift\":\"Shift 1\",\"middlename\":\"L\",\"active\":\"09/22/2015\",\"department\":\"Shipping\",\"lastname\":\"Elliott\"},{\"firstname\":\"Robert\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"FF591F68\",\"shift\":\"Shift 1\",\"middlename\":\"K\",\"active\":\"07/28/2015\",\"department\":\"Shipping\",\"lastname\":\"Miller\"},{\"firstname\":\"Salvador\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"CEBCC740\",\"shift\":\"Shift 1\",\"middlename\":\"F\",\"active\":\"09/18/2015\",\"department\":\"Shipping\",\"lastname\":\"Ryan\"},{\"firstname\":\"Tyson\",\"employeetype\":\"Full-Time Employee\",\"badgeid\":\"E880B82A\",\"shift\":\"Shift 1\",\"middlename\":\"E\",\"active\":\"12/05/2015\",\"department\":\"Shipping\",\"lastname\":\"Williams\"}]";
            jsonExpected = (JsonArray)Jsoner.deserialize(jsonExpectedString);
            
            /* Get "Employee Summary" Report (Shipping Department) */

            String jsonActualString = reportDAO.getEmployeeSummary(departmentid);
            
            jsonActual = (JsonArray)Jsoner.deserialize(jsonActualString);
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        /* Compare to Expected Values */
        
        assertNotNull(jsonExpected);
        assertNotNull(jsonActual);
        assertEquals(jsonExpected, jsonActual);

    }
    
}