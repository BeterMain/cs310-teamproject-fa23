package edu.jsu.mcis.cs310.tas_fa23;

import java.time.*;
import java.util.HashMap;

/**
 *
 * @author cmunc
 */
public class Shift
{
    
    final private int id;
    final private String description;
    final private DailySchedule dailySchedule;
    
    public Shift(int id, String description, DailySchedule dailySchedule)
    {
        this.id = id;
        this.description = description;
        this.dailySchedule = dailySchedule;
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
        return dailySchedule.getShiftStart();
    }
    
    public LocalTime getShiftStop()
    {
        return dailySchedule.getShiftStop();
    }
    
    public int getRoundInterval()
    {
        return dailySchedule.getRoundInterval();
    }
    
    public int getGracePeriod()
    {
        return dailySchedule.getGracePeriod();
    }
    
    public int getDockPenalty()
    {
        return dailySchedule.getDockPenalty();
    }
    
    public LocalTime getLunchStart()
    {
        return dailySchedule.getLunchStart();
    }
    
    public LocalTime getLunchStop()
    {
        return dailySchedule.getLunchStop();
    }
    
    public int getLunchThreshold()
    {
        return dailySchedule.getLunchThreshold();
    }
    
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        
        s.append(description).append(": ");
        s.append(dailySchedule.getShiftStart()).append(" - ").append(dailySchedule.getShiftStop()).append(" (");
        s.append(((dailySchedule.getShiftStop().minusHours(dailySchedule.getShiftStart().getHour()).minusMinutes(dailySchedule.getShiftStart().getMinute())).getHour() * 60) + (dailySchedule.getShiftStop().minusHours(dailySchedule.getShiftStart().getHour()).minusMinutes(dailySchedule.getShiftStart().getMinute())).getMinute());
        s.append(" minutes); ");
        s.append("Lunch: ").append(dailySchedule.getLunchStart()).append(" - ").append(dailySchedule.getLunchStop());
        s.append(" (").append(((dailySchedule.getLunchStop().minusHours(dailySchedule.getLunchStart().getHour()).minusMinutes(dailySchedule.getLunchStart().getMinute())).getHour() * 60) + (dailySchedule.getLunchStop().minusHours(dailySchedule.getLunchStart().getHour()).minusMinutes(dailySchedule.getLunchStart().getMinute())).getMinute());
        s.append(" minutes)");
        return s.toString();
    }
}
