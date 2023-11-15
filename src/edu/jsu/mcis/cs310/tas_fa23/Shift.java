package edu.jsu.mcis.cs310.tas_fa23;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

/**
 *
 * @author cmunc
 */
public class Shift
{
    
    final private int id, lunchDuration, shiftDuration;
    final private String description;
    final private DailySchedule defaultSchedule;
    private HashMap<Integer, DailySchedule> scheduleMap = new HashMap<>();
    
    public Shift(int id, String description, DailySchedule defaultSchedule)
    {
        /* Add Default Schedules to the Map */
        for (int i = 1; i < 6; i++)
        {
            DayOfWeek day = DayOfWeek.of(i);
                            
            scheduleMap.put(day.getValue(), defaultSchedule);
        }
        
        /* Initialize Variables */
        this.id = id;
        this.description = description;
        this.defaultSchedule = defaultSchedule;
        this.lunchDuration = (int) ChronoUnit.MINUTES.between(defaultSchedule.getLunchStart(), defaultSchedule.getLunchStop());
        this.shiftDuration = ((defaultSchedule.getShiftStop().minusHours(defaultSchedule.getShiftStart().getHour()).minusMinutes(defaultSchedule.getShiftStart().getMinute())).getHour() * 60) + (defaultSchedule.getShiftStop().minusHours(defaultSchedule.getShiftStart().getHour()).minusMinutes(defaultSchedule.getShiftStart().getMinute())).getMinute();
    }
    
    public int getID()
    {
        return id;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public LocalTime getShiftStart()
    {
        return defaultSchedule.getShiftStart();
    }
    
    public LocalTime getShiftStop()
    {
        return defaultSchedule.getShiftStop();
    }
    
    public int getRoundInterval()
    {
        return defaultSchedule.getRoundInterval();
    }
    
    public int getGracePeriod()
    {
        return defaultSchedule.getGracePeriod();
    }
    
    public int getDockPenalty()
    {
        return defaultSchedule.getDockPenalty();
    }
    
    public LocalTime getLunchStart()
    {
        return defaultSchedule.getLunchStart();
    }
    
    public LocalTime getLunchStop()
    {
        return defaultSchedule.getLunchStop();
    }
    
    public int getLunchThreshold()
    {
        return defaultSchedule.getLunchThreshold();
    }
    
    public DailySchedule getDefaultSchedule()
    {
        return defaultSchedule;
    }
    
    public DailySchedule getDailySchedule(DayOfWeek day)
    {
        return scheduleMap.get(day.getValue());
    }
    
    public HashMap<Integer, DailySchedule> getScheduleMap() {
        return scheduleMap;
    }
    
    public void setScheduleMap(HashMap<Integer, DailySchedule> scheduleMap) {
        this.scheduleMap = scheduleMap;
    }
    
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        
        s.append(description).append(": ");
        s.append(defaultSchedule.getShiftStart()).append(" - ").append(defaultSchedule.getShiftStop()).append(" (");
        s.append(shiftDuration);
        s.append(" minutes); ");
        s.append("Lunch: ").append(defaultSchedule.getLunchStart()).append(" - ").append(defaultSchedule.getLunchStop());
        s.append(" (").append(lunchDuration);
        s.append(" minutes)");
        return s.toString();
    }
}
