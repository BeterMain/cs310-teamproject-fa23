package edu.jsu.mcis.cs310.tas_fa23;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
