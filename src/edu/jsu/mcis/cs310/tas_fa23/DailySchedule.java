package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalTime;
import java.util.HashMap;

public class DailySchedule 
{
    LocalTime shiftStart = null, shiftStop = null, lunchStart = null, lunchStop = null;

    private int id, roundInterval, gracePeriod, dockPenalty, lunchThreshold;
    private int shiftLength, lunchLength;
    private boolean Bool = false;
    
    DailySchedule(HashMap<String,Object> hashmap)
    {
        this.id = (int) hashmap.get("id");
        java.sql.Time shiftStartTime = (java.sql.Time) hashmap.get("shiftstart");
        this.shiftStart = shiftStartTime.toLocalTime();
        java.sql.Time time = (java.sql.Time) hashmap.get("shiftstop");
        this.shiftStop = time.toLocalTime();
        this.roundInterval = (int) hashmap.get("roundinterval");
        this.gracePeriod = (int) hashmap.get("graceperiod");
        this.dockPenalty = (int) hashmap.get("dockpenalty");
        java.sql.Time lunchStartTime = (java.sql.Time) hashmap.get("lunchstart");
        this.lunchStart = lunchStartTime.toLocalTime();
        java.sql.Time lunchStopTime = (java.sql.Time) hashmap.get("lunchstop");
        this.lunchStop = lunchStopTime.toLocalTime();
        this.lunchThreshold = (int) hashmap.get("lunchthreshold");
    }
    
    public int getID()
    {
        return this.id;
    }
    
    public LocalTime getShiftStart()
    {
        return this.shiftStart;
    }
    
    public LocalTime getShiftStop()
    {
        return this.shiftStop;
    }
    
    public int getRoundInterval()
    {
        return this.roundInterval;
    }
    
    public int getGracePeriod()
    {
        return this.gracePeriod;
    }
    
    public int getDockPenalty()
    {
        return this.dockPenalty;
    }
    
    public LocalTime getLunchStart()
    {
        return this.lunchStart;
    }
    
    
    public LocalTime getLunchStop()
    {
        return this.lunchStop;
    }
    
    public int getLunchThreshold()
    {
        return this.lunchThreshold;
    }
}
