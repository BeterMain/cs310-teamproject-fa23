package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalTime;
import java.util.HashMap;

/**
 *
 * @author cmunc
 */
public class Shift
{
    LocalTime shiftStart = null, shiftStop = null, lunchStart = null, lunchStop = null;

    int id, roundInterval, gracePeriod, dockPenalty, lunchThreshold;
    String description;
    int shiftLength, lunchLength;
    boolean Bool = false;
    
    HashMap<String, String> shiftMap = null;
    
    public Shift(int id, String description, LocalTime shiftStart, LocalTime shiftStop, int roundInterval, int gracePeriod, int dockPenalty, LocalTime lunchStart, LocalTime lunchStop, int lunchThreshold)
    {
        this.id = id;
        this.description = description;
        this.shiftStart = shiftStart;
        this.shiftStop = shiftStop;
        this.roundInterval = roundInterval;
        this.gracePeriod = gracePeriod;
        this.dockPenalty = dockPenalty;
        this.lunchStart = lunchStart;
        this.lunchStop = lunchStop;
        this.lunchThreshold = lunchThreshold;
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
