package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalTime;
import java.sql.Time;
import java.util.HashMap;

public class DailySchedule 
{
    LocalTime shiftStart = null, shiftStop = null, lunchStart = null, lunchStop = null;

    final private int roundInterval, gracePeriod, dockPenalty, lunchThreshold;
    
    public DailySchedule(HashMap<String,Object> hashmap)
    {
        Time shiftStartTime = (Time) hashmap.get("shiftstart");
        this.shiftStart = shiftStartTime.toLocalTime();
        Time time = (Time) hashmap.get("shiftstop");
        this.shiftStop = time.toLocalTime();
        this.roundInterval = (int) hashmap.get("roundinterval");
        this.gracePeriod = (int) hashmap.get("graceperiod");
        this.dockPenalty = (int) hashmap.get("dockpenalty");
        Time lunchStartTime = (Time) hashmap.get("lunchstart");
        this.lunchStart = lunchStartTime.toLocalTime();
        Time lunchStopTime = (Time) hashmap.get("lunchstop");
        this.lunchStop = lunchStopTime.toLocalTime();
        this.lunchThreshold = (int) hashmap.get("lunchthreshold");
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
