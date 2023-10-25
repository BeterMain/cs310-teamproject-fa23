package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.EventType;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import edu.jsu.mcis.cs310.tas_fa23.Shift;

/**
 * 
 * Utility class for DAOs.  This is a final, non-constructable class containing
 * common DAO logic and other repeated and/or standardized code, refactored into
 * individual static methods.
 * 
 */
public final class DAOUtility {
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        
        String result;
        
        /* Create ArrayList Object */
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        for (Punch punch : dailypunchlist) {
            /* Create HashMap Object (one for every Punch!) */
            HashMap<String, String> punchData = new HashMap<>();

            /* Add Punch Data to HashMap */
            punchData.put("id", String.valueOf(punch.getId()));
            punchData.put("badgeid", punch.getBadge().getId());
            punchData.put("terminalid", String.valueOf(punch.getTerminalId()));
            punchData.put("punchtype", punch.getPunchType().toString());
            punchData.put("adjustmenttype", punch.getAdjustmentType().toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
            punchData.put("originaltimestamp", punch.getOriginaltimestamp().format(formatter).toUpperCase());
            punchData.put("adjustedtimestamp", punch.getAdjustedtimestamp().format(formatter).toUpperCase());

            /* Append HashMap to ArrayList */
            jsonData.add(punchData);
        }

        result = Jsoner.serialize(jsonData);
        
        return result;
    }
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailyPunchList, Shift shift)
    {
        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        PunchDAO punchDao = daoFactory.getPunchDAO();
        ShiftDAO shiftDao = daoFactory.getShiftDAO();
        
        Boolean timeOut = false;
        
        Integer minutesWorked = 0;
        
        ArrayList<Punch> punchlist = dailyPunchList;
        
        for (Punch punch : punchlist)
        {
            punch.adjust(shift);
            
            if (punch.getPunchType() == EventType.TIME_OUT)
            {
                timeOut = true;
            }
        }
        
        if (punchlist.size() == 4)
        {
            LocalDateTime clockIn = (punchlist.get(0)).getAdjustedtimestamp();
            LocalDateTime clockOut = (punchlist.get(3)).getAdjustedtimestamp();
            LocalDateTime lunchStart = (punchlist.get(1)).getAdjustedtimestamp();
            LocalDateTime lunchStop = (punchlist.get(2)).getAdjustedtimestamp();

            Integer clockOutMinutes = (clockOut.getHour() * 60) + clockOut.getMinute();
            Integer clockInMinutes = (clockIn.getHour() * 60) + clockIn.getMinute();
            Integer lunchStopMinutes = (lunchStop.getHour() * 60) + lunchStop.getMinute();
            Integer lunchStartMinutes = (lunchStart.getHour() * 60) + lunchStart.getMinute();
            
            minutesWorked = ((clockOutMinutes - clockInMinutes) - (lunchStopMinutes - lunchStartMinutes));
        }
        
        else if (punchlist.size() == 2)
        {
            LocalDateTime clockIn  = punchlist.get(0).getAdjustedtimestamp();
            LocalDateTime clockOut = punchlist.get(1).getAdjustedtimestamp();

            Integer clockOutMinutes = (clockOut.getHour() * 60) + clockOut.getMinute();
            Integer clockInMinutes = (clockIn.getHour() * 60) + clockIn.getMinute();

            minutesWorked = (clockOutMinutes - clockInMinutes);
            
            if (minutesWorked > shift.getLunchThreshold())
            {
                minutesWorked -= 30;
            }
        }
        
        if (timeOut == true)
        {
            minutesWorked = 0;
        }
        
        return minutesWorked;
    }
}