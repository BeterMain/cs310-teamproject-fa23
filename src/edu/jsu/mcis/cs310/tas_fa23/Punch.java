package edu.jsu.mcis.cs310.tas_fa23;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

public class Punch {
    
    /* Initialize Vars */
    
    private final int terminalId;
    private int id;
    private LocalDateTime originalTimeStamp = null;
    private final LocalDateTime adjustedTimeStamp = null;
    private final EventType punchType;
    private final Badge badge;
    private PunchAdjustmentType adjustmentType = null;
    
    /* Create Constructors */
    
    public Punch(int terminalid, Badge badge, EventType punchType) {
        this.terminalId = terminalid;
        this.badge = badge;
        this.punchType = punchType;
    }
    
    public Punch(int id, int terminalId, Badge badge, LocalDateTime originalTimeStamp, EventType punchType) {
        this.id = id;
        this.terminalId = terminalId;
        this.badge = badge;
        this.originalTimeStamp = originalTimeStamp;
        this.punchType = punchType;
    }
    
    /* Create Getters */

    /**
     * 
     * @return integer Id of Punch
     */
    public int getId() {
        return this.id;
    }
    
    /**
     *  
     * @return integer Terminal Id of Punch
     */
    public int getTerminalId() {
        return this.terminalId;
    }
    
    /**
     * 
     * @return a Badge Object
     */
    public Badge getBadge() {
        return this.badge;
    }
    
    /**
     *
     * @return a LocalDataTime object of the original timestamp
     */
    public LocalDateTime getOriginalTimeStamp() {
        return this.originalTimeStamp;
    }
    
    /**
     *
     * @return an EventType object
     */
    public EventType getPunchType() {
        return this.punchType;
    }
    
    /* Output State in Srting Form */

    
    public void adjust(Shift s){
        LocalDateTime ot = originalTimeStamp;
        Boolean Weekend = false;
        DayOfWeek day = ot.getDayOfWeek();
        
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
            Weekend = true;
        }
        Integer dock = s.getDockPenalty();
        Integer Interval = s.getRoundInterval();
        Integer grace = s.getGracePeriod();
        
        LocalTime sStart = s.getShiftStart();
        LocalTime lStart = s.getLunchStart();
        LocalTime sStop = s.getShiftStop();
        LocalTime lStop = s.getLunchStop();
        
        LocalDateTime shiftStart = ot.withHour(sStart.getHour()).withMinute(sStart.getMinute());
        shiftStart = shiftStart.withSecond(0);
        
        LocalDateTime shiftStop = ot.withHour(sStop.getHour()).withMinute(sStop.getMinute());
        shiftStop = shiftStop.withSecond(0);
        
        LocalDateTime lunchStart = ot.withHour(lStart.getHour()).withMinute(lStart.getMinute());
        lunchStart = lunchStart.withSecond(0);
        
        LocalDateTime lunchStop = ot.withHour(lStop.getHour()).withMinute(lStop.getMinute());
        lunchStop = lunchStop.withSecond(0);
        
        LocalDateTime shiftStartInterval = shiftStart.minusMinutes(Interval);
        LocalDateTime shiftStartGrace = shiftStart.plusMinutes(grace);
        LocalDateTime shiftStartDock = shiftStart.plusMinutes(dock);
        
        LocalDateTime shiftStopInterval = shiftStop.minusMinutes(Interval);
        LocalDateTime shiftStopGrace = shiftStop.plusMinutes(grace);
        LocalDateTime shiftStopDock = shiftStop.plusMinutes(dock);
    
    } 
    /**
     * Returns a string in the format of "#(badgeid) (event type): (DAY mm/dd/yyyy) (HH:MM:SS)"
     * @return a String representation of the class
     */
    public String printOriginal() {
        StringBuilder s = new StringBuilder();
        
        // Append badge id
        s.append("#").append(badge.getId()).append(" ");
        
        // Append Event Type
        s.append(punchType.toString()).append(": ");
        
        // Append Time Stamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        s.append(originalTimeStamp.format(formatter).toUpperCase());
        
        // Return final string
        return s.toString();
    }
    
}
