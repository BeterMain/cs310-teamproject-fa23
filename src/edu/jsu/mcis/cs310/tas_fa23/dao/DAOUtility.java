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
        //for(each dailypunchlist in the weekly<punchlist>)
        DAOFactory daoFactory = new DAOFactory("tas.jdbc");
        PunchDAO punchDao = daoFactory.getPunchDAO();
        ShiftDAO shiftDao = daoFactory.getShiftDAO();
        
        Boolean timeOut = false;
        int loopCounter = 0;
        
        Integer minutesWorked = 0;
        
        ArrayList<Punch> punchlist = dailyPunchList;
        
        for (Punch punch : punchlist)
        {
            punch.adjust(shift);
        }
        
        while(loopCounter * 4 < punchlist.size())
        {
            Boolean hasLunch = false;
            
            LocalDateTime index1 = punchlist.get(1 + (4 * loopCounter)).getAdjustedtimestamp();
            LocalDateTime index2 = punchlist.get(2 + (4 * loopCounter)).getAdjustedtimestamp();
            
            Integer index2Minutes =(index2.getHour() * 60) + index2.getMinute();
            Integer index1Minutes = index1.getHour() * 60 + index1.getMinute();
            
            Integer difference = index2Minutes - index1Minutes;
            
            if (difference > 20 && difference < 45)
            {
                hasLunch = true;
            }
            
            if (hasLunch)
            {
                LocalDateTime clockIn = (punchlist.get(0 + (4 * loopCounter))).getAdjustedtimestamp();
                LocalDateTime clockOut = (punchlist.get(3 + (4 * loopCounter))).getAdjustedtimestamp();
                LocalDateTime lunchStart = (punchlist.get(1 + (4 * loopCounter))).getAdjustedtimestamp();
                LocalDateTime lunchStop = (punchlist.get(2 + (4 * loopCounter))).getAdjustedtimestamp();

                Integer clockOutMinutes = (clockOut.getHour() * 60) + clockOut.getMinute();
                Integer clockInMinutes = (clockIn.getHour() * 60) + clockIn.getMinute();
                Integer lunchStopMinutes = (lunchStop.getHour() * 60) + lunchStop.getMinute();
                Integer lunchStartMinutes = (lunchStart.getHour() * 60) + lunchStart.getMinute();

            if (punchlist.get(4).getPunchType() == EventType.TIME_OUT)
            {
                timeOut = true;
            }
            if (timeOut == true)
            {
                //if timeout occurs don't add minutes
            }
            else
            {
                minutesWorked += ((clockOutMinutes - clockInMinutes) - (lunchStopMinutes - lunchStartMinutes));
            }
            }

            else if (!hasLunch)
            {
                LocalDateTime clockIn  = punchlist.get(0 + (4 * loopCounter)).getAdjustedtimestamp();
                LocalDateTime clockOut = punchlist.get(1 + (4 * loopCounter)).getAdjustedtimestamp();
                LocalDateTime clockIn2 = punchlist.get(2 + (4 * loopCounter)).getAdjustedtimestamp();
                LocalDateTime clockOut2 = punchlist.get(3 + (4 * loopCounter)).getAdjustedtimestamp();

                Integer clockOutMinutes = (clockOut.getHour() * 60) + clockOut.getMinute();
                Integer clockInMinutes = (clockIn.getHour() * 60) + clockIn.getMinute();
                Integer clockOutMinutes2 = (clockOut2.getHour() * 60 + clockOut2.getMinute());
                Integer clockInMinutes2 = (clockIn2.getHour() * 60 + clockIn2.getMinute());
                
                Integer minutesWorked1 = clockOutMinutes - clockInMinutes;
                Integer minutesWorked2 = clockOutMinutes2 - clockInMinutes2;
             
                if (punchlist.get(1 + (loopCounter)).getPunchType() == EventType.TIME_OUT)
                {
                    minutesWorked1 = 0;
                }
                
                if (punchlist.get(3 + (4 * loopCounter)).getPunchType() == EventType.TIME_OUT)
                {
                    minutesWorked2 = 0;
                }
                
                minutesWorked = minutesWorked1 + minutesWorked2;

                if (minutesWorked1 > shift.getLunchThreshold())
                {
                    minutesWorked -= 30;
                }
                if (minutesWorked2 > shift.getLunchThreshold())
                {
                    minutesWorked -= 30;
                }
            }
            loopCounter ++;
        }
        
        if ((loopCounter * 4) - 2 == punchlist.size())
        {
            LocalDateTime clockIn = punchlist.get((loopCounter * 4) - 3).getAdjustedtimestamp();
            LocalDateTime clockOut = punchlist.get((loopCounter * 4) - 2).getAdjustedtimestamp();
            
            Integer clockInMinutes = (clockIn.getHour() * 60) + clockIn.getMinute();
            Integer clockOutMinutes = (clockOut.getHour() * 60) + clockOut.getHour();
            
            minutesWorked += clockOutMinutes - clockInMinutes;
            
            if ((clockOutMinutes - clockInMinutes) >= shift.getLunchThreshold())
            {
                minutesWorked -= 30;
            }
        }
        
        return minutesWorked;
    }
}