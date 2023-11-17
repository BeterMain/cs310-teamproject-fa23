package edu.jsu.mcis.cs310.tas_fa23.dao;

import java.time.*;
import java.util.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import com.github.cliftonlabs.json_simple.*;
import edu.jsu.mcis.cs310.tas_fa23.DailySchedule;
import edu.jsu.mcis.cs310.tas_fa23.EventType;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import edu.jsu.mcis.cs310.tas_fa23.Shift;
import java.math.BigDecimal;
import java.math.RoundingMode;

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
    
    public static int calculateTotalMinutes(ArrayList<Punch> punchlist, Shift s)
    {
        boolean hasClockIn = false, hasClockOut = false;
        Integer minutesWorked = 0, minutesWorkedInShift;
        LocalDateTime clockIn = null, clockOut = null;
        
        for(int i = 0; i < punchlist.size(); i++)
        {
            DayOfWeek day = punchlist.get(i).getAdjustedtimestamp().getDayOfWeek();
            DailySchedule schedule;
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
                schedule = s.getDefaultSchedule();
            }
            else {
                schedule = s.getDailySchedule(day);
            }
            
            LocalTime lunchStop = schedule.getLunchStop();
            LocalTime lunchStart = schedule.getLunchStart();

            int lunchLength = (int) ChronoUnit.MINUTES.between(lunchStart, lunchStop);
            
            // Logic
            if (punchlist.get(i).getPunchType() == EventType.CLOCK_IN && !hasClockIn)
            {
                clockIn = punchlist.get(i).getAdjustedtimestamp();
                
                hasClockIn = true;
            }
            
            
            else if (punchlist.get(i).getPunchType() == EventType.CLOCK_OUT && hasClockIn)
            {
                clockOut = punchlist.get(i).getAdjustedtimestamp();
                
                hasClockOut = true;
            }
            
            else if (punchlist.get(i).getPunchType() == EventType.TIME_OUT && hasClockIn)
            {
                hasClockIn = false;
            }
            
            
            if (hasClockIn && hasClockOut)
            {
               
                minutesWorkedInShift = (int) ChronoUnit.MINUTES.between(clockIn, clockOut);
                
                if (minutesWorkedInShift > s.getLunchThreshold())
                {
                    minutesWorkedInShift -= lunchLength;
                }
                
                minutesWorked += minutesWorkedInShift;
                
                hasClockOut = false;
                hasClockIn = false;
            }
        }
        return minutesWorked;
    }
    
    public static BigDecimal calculateAbsenteeism(ArrayList<Punch> punchlist, Shift s) {
        
        /* Declare Variables */
        
        BigDecimal result;
        
        HashMap<Integer, DailySchedule> scheduleMap = s.getScheduleMap();
        
        BigDecimal minutesWorked, scheduledMinutes;
        float tempMin = 0;
        
        /* Get employee minutes worked */
        
        minutesWorked = BigDecimal.valueOf(calculateTotalMinutes(punchlist, s));
        
        /* Get scheduled minutes to work */
        
        for (DailySchedule schedule : scheduleMap.values()) 
        {
            LocalTime sStart = schedule.getShiftStart();
            LocalTime sStop = schedule.getShiftStop();
            LocalTime lStart = schedule.getLunchStart();
            LocalTime lStop = schedule.getLunchStop();
                    
            tempMin += (ChronoUnit.MINUTES.between(sStart, sStop) - ChronoUnit.MINUTES.between(lStart, lStop));
        }
        
        scheduledMinutes = BigDecimal.valueOf(tempMin);
        
        /* Divide worked minutes over scheduled minutes */
        
        result = new BigDecimal("1").subtract(minutesWorked.divide(scheduledMinutes, 5,RoundingMode.UP));
        
        /* Calculate Percentage */
        
        result = result.multiply(new BigDecimal("100")).setScale(2,RoundingMode.HALF_UP);
        
        /* Return Result */
        
        return result;
    }
    
    public static String getPunchListPlusTotalsAsJSON(ArrayList<Punch> punchlist, Shift shift) {
        
        String result;
        
        /* Create ArrayList Object */
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<>();
        
        for (Punch punch : punchlist) {
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
        
        /* Create new HashMap Object */
        
        HashMap<String, Object> totalsData = new HashMap<>();
        
        /* Insert List of Punches and new Totals */
        
        totalsData.put("totalminutes", calculateTotalMinutes(punchlist, shift));
        totalsData.put("absenteeism", String.valueOf(calculateAbsenteeism(punchlist, shift)) + "%");
        totalsData.put("punchlist", jsonData);
        
        /* Serialize and Return */
        
        result = Jsoner.serialize(totalsData);
        
        return result;
        
    }
    
}