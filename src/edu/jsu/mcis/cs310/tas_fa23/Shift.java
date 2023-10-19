package edu.jsu.mcis.cs310.tas_fa23;

import java.time.*;
import java.util.HashMap;

/**
 *
 * @author cmunc
 */
public class Shift
{
    LocalTime shiftStart = null, shiftStop = null, lunchStart = null, lunchStop = null;

    private int id, roundInterval, gracePeriod, dockPenalty, lunchThreshold;
    private String description;
    private int shiftLength, lunchLength;
    private boolean Bool = false;
    
    public Shift(HashMap<String,Object> shiftMap)
    {
        this.id = (int) shiftMap.get("id");
        this.description = (String) shiftMap.get("description");
        java.sql.Time shiftStartTime = (java.sql.Time) shiftMap.get("shiftstart");
        this.shiftStart = shiftStartTime.toLocalTime();
        java.sql.Time time = (java.sql.Time) shiftMap.get("shiftstop");
        this.shiftStop = time.toLocalTime();
        this.roundInterval = (int) shiftMap.get("roundinterval");
        this.gracePeriod = (int) shiftMap.get("graceperiod");
        this.dockPenalty = (int) shiftMap.get("dockpenalty");
        java.sql.Time lunchStartTime = (java.sql.Time) shiftMap.get("lunchstart");
        this.lunchStart = lunchStartTime.toLocalTime();
        java.sql.Time lunchStopTime = (java.sql.Time) shiftMap.get("lunchstop");
        this.lunchStop = lunchStopTime.toLocalTime();
        this.lunchThreshold = (int) shiftMap.get("lunchthreshold");
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
        return shiftStart;
    }
    
    public LocalTime getShiftStop()
    {
        return shiftStop;
    }
    
    public int getRoundInterval()
    {
        return roundInterval;
    }
    
    public int getGracePeriod()
    {
        return gracePeriod;
    }
    
    public int getDockPenalty()
    {
        return dockPenalty;
    }
    
    public LocalTime getLunchStart()
    {
        return lunchStart;
    }
    
    public LocalTime getLunchStop()
    {
        return lunchStop;
    }
    
    public int getLunchThreshold()
    {
        return lunchThreshold;
    }
    
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        
        s.append(description).append(": ");
        s.append(shiftStart).append(" - ").append(shiftStop).append(" (");
        s.append(((shiftStop.minusHours(shiftStart.getHour()).minusMinutes(shiftStart.getMinute())).getHour() * 60) + (shiftStop.minusHours(shiftStart.getHour()).minusMinutes(shiftStart.getMinute())).getMinute());
        s.append(" minutes); ");
        s.append("Lunch: ").append(lunchStart).append(" - ").append(lunchStop);
        s.append(" (").append(((lunchStop.minusHours(lunchStart.getHour()).minusMinutes(lunchStart.getMinute())).getHour() * 60) + (lunchStop.minusHours(lunchStart.getHour()).minusMinutes(lunchStart.getMinute())).getMinute());
        s.append(" minutes)");
        return s.toString();
    }
}
