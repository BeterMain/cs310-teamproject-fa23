package edu.jsu.mcis.cs310.tas_fa23;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.DayOfWeek;

public class Punch {
    
    /* Initialize Vars */
    
    private final int terminalId;
    private Integer id = null;
    private LocalDateTime originalTimestamp = null;
    private LocalDateTime adjustedTimestamp = null;
    private final EventType punchType;
    private final Badge badge;
    private PunchAdjustmentType adjustmentType = PunchAdjustmentType.NONE;
    
    /* Create Constructors */
    
    public Punch(int terminalid, Badge badge, EventType punchType) {
        this.terminalId = terminalid;
        this.badge = badge;
        this.punchType = punchType;
        this.originalTimestamp = LocalDateTime.now().withNano(0);
    }
    
    public Punch(int id, int terminalId, Badge badge, LocalDateTime originalTimestamp, EventType punchType) {
        this.id = id;
        this.terminalId = terminalId;
        this.badge = badge;
        this.originalTimestamp = originalTimestamp;
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
    public LocalDateTime getOriginaltimestamp() {
        return this.originalTimestamp;
    }
    
    /**
     *
     * @return a LocalDataTime object of the adjusted timestamp
     */
    public LocalDateTime getAdjustedtimestamp() {
        return this.adjustedTimestamp;
    }
    
    /**
     *
     * @return an EventType object
     */
    public EventType getPunchType() {
        return this.punchType;
    }
    
    public PunchAdjustmentType getAdjustmentType() {
        return this.adjustmentType;
    }
    
    /* Create Adjust Method */
    
    public void adjust(Shift s){
        
        Boolean weekend = false;
        DayOfWeek day = originalTimestamp.getDayOfWeek();
        LocalTime punchTime = originalTimestamp.toLocalTime();
        
        if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY){
            weekend = true;
        }
        
        // Get all vars from shift parameter
        Integer dock = s.getDockPenalty();
        int interval = s.getRoundInterval();
        Integer grace = s.getGracePeriod();
        
        LocalTime sStart = s.getShiftStart();
        LocalTime lStart = s.getLunchStart();
        LocalTime sStop = s.getShiftStop();
        LocalTime lStop = s.getLunchStop();
        
        /* Main Logic */
        // Rule for dock penalty
        boolean isDock = punchTime.isAfter(sStart) && punchTime.isBefore(sStart.plusMinutes(dock));
        
        // (If) If the punch type isn't a time out or if we aren't on a weekend, then we procede (The rest will be an if statement)
        if (punchType != EventType.TIME_OUT && !weekend) {
            
            // (If) If the punch time is within the lunch break
            if (punchTime.isAfter(lStart) && punchTime.isBefore(lStop)) {
                
                // Assign adjustment variable to "Lunch Start" 
                if (punchTime.getMinute() - lStart.getMinute() < lStop.getMinute() - punchTime.getMinute()) {
                    adjustmentType = PunchAdjustmentType.LUNCH_START;
                    punchTime = lStart;
                }
                else {
                    adjustmentType = PunchAdjustmentType.LUNCH_STOP;
                    punchTime = lStop; 
                }
            }
            
            // If the punch type is "Clock In" then procede
            else if (punchType == EventType.CLOCK_IN) {
                
                // If the punch time within the Round interval and is before Shift start
                if (punchTime.isBefore(sStart) && punchTime.isAfter(sStart.minusMinutes(interval))) {
                    
                    // Set the punch time to shift start 
                    punchTime = sStart;
                    
                    // Assign adjustment variable to "Shift Start"
                    adjustmentType = PunchAdjustmentType.SHIFT_START;
                    
                }
                
                // If the punch time after the "Shift start" and is within the grace period 
                else if (punchTime.isAfter(sStart) && punchTime.isBefore(sStart.plusMinutes(grace))) {
                    
                    // Set the punch time to shift start 
                    punchTime = sStart;
                    
                    // Assign adjustment variable to "Shift Start" 
                    adjustmentType = PunchAdjustmentType.SHIFT_START;
                    
                }
                
                // (Else if) If the punch time is outside of the dock interval and grace period brefore and after the shift
                else if (punchTime.isAfter(sStart) && punchTime.getMinute() % interval != 0 && !isDock) { 

                    // Set the adjustment variable to "Interval Round"
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;

                    // Round the time up
                    int remainder = punchTime.getMinute() % interval;
                    if (remainder > interval/2) {
                        remainder = interval - remainder;
                        punchTime = punchTime.plusMinutes(remainder);
                    }

                    // Find the middle of the interval and round the time up if above 30 seconds
                    else if (remainder == 7 && punchTime.getSecond() > 30) {
                        remainder = interval - remainder;
                        punchTime = punchTime.plusMinutes(remainder);
                    }

                    // Round the time down
                    else {
                        punchTime = punchTime.minusMinutes(remainder);
                    }

                    // Reset seconds to zero
                    punchTime = punchTime.withSecond(0);

                }
                
                else {
                    
                    // If the punch time divided by the round interval is == 0 
                    if (punchTime.getMinute() % interval == 0) {
                        
                        // If the punch time after the "Shift start" is within the Dock period 
                        if (punchTime.isAfter(sStart) && punchTime.isBefore(sStart.plusMinutes(dock+1))) {
                            
                            // Set the punch time to shift start plus the Dock penalty  
                            punchTime = sStart.plusMinutes(dock);
                            
                            // Assign adjustment variable to "Shift Dock" 
                            adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                            
                        }
                        else {
                            
                            // Set adjustement type to "None" 
                            adjustmentType = PunchAdjustmentType.NONE;
                            
                            // Reset seconds to zero 
                            punchTime = punchTime.withSecond(0);
                            punchTime = punchTime.withNano(0);
                            
                        }
                    }
                    
                    // If the punch time after the "Shift start" is within the Dock period
                    else {
                        
                        // Set the punch time to shift start plus the Dock penalty  
                        punchTime = sStart.plusMinutes(dock);
                        
                        // Assign adjustment variable to "Shift Dock" 
                        adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                        
                    }
                }
            }
            
            
            /* If it is "Clock Out" then procede */
            else {
                
                // If the punch time within the Round interval and is after Shift stop
                if (punchTime.isAfter(sStop) && punchTime.isBefore(sStop.plusMinutes(interval))) {
                    
                    // Set the punch time to shift stop 
                    punchTime = sStop;
                    
                    // Assign adjustment variable to "Shift Stop" 
                    adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                    
                }

                // If the punch time after the "Shift start" is within the grace period 
                else if (punchTime.isBefore(sStop) && punchTime.isAfter(sStop.minusMinutes(grace))) {
                    // Set the punch time to shift stop 
                    punchTime = sStop;
                    // Assign adjustment variable to "Shift Stop"
                    adjustmentType = PunchAdjustmentType.SHIFT_STOP;
                }
                
                // (Else if) If the punch time is outside of the dock interval and grace period brefore and after the shift
                else if (punchTime.isAfter(sStart) && punchTime.getMinute() % interval != 0 && !isDock) { 

                    // Set the adjustment variable to "Interval Round"
                    adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;

                    // Round the time up
                    int remainder = punchTime.getMinute() % interval;
                    if (remainder > interval/2) {
                        remainder = interval - remainder;
                        punchTime = punchTime.plusMinutes(remainder);
                    }

                    // Find the middle of the interval and round the time up if above 30 seconds
                    else if (remainder == 7 && punchTime.getSecond() > 30) {
                        remainder = interval - remainder;
                        punchTime = punchTime.plusMinutes(remainder);
                    }

                    // Round the time down
                    else {
                        punchTime = punchTime.minusMinutes(remainder);
                    }

                    // Reset seconds to zero
                    punchTime = punchTime.withSecond(0);

                }
                
                else {
                    
                    // If the punch time divided by the round interval is == 0 
                    if (punchTime.getMinute() % interval == 0) {
                        
                        // If the punch time after the "Shift start" is within the Dock period 
                        if (punchTime.isBefore(sStop) && punchTime.isAfter(sStop.minusMinutes(dock+1))) {
                            
                            // Set the punch time to shift stop minus the Dock penalty
                            punchTime = sStop.minusMinutes(dock);
                            
                            // Assign adjustment variable to "Shift Dock" 
                            adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                            
                        }
                        
                        else {
                            
                            // Set adjustement type to "None" 
                            adjustmentType = PunchAdjustmentType.NONE;
                            
                            // Reset seconds to zero 
                            punchTime = punchTime.withSecond(0);
                            punchTime = punchTime.withNano(0);
                            
                        }
                    }
                    
                    // If the punch time after the "Shift start" is within the Dock period
                    else {
                        // Set the punch time to shift stop minus the Dock penalty
                        punchTime = sStop.minusMinutes(dock);
                        // Assign adjustment variable to "Shift Dock" 
                        adjustmentType = PunchAdjustmentType.SHIFT_DOCK;
                    }
                }
            }
        }
        
        else {
            
            // Set the adjustment variable to "Interval Round"
            adjustmentType = PunchAdjustmentType.INTERVAL_ROUND;
            
            // Round the time up 
            int remainder = punchTime.getMinute() % interval;
            if (remainder > interval/2) {
                remainder = interval - remainder;
                punchTime = punchTime.plusMinutes(remainder);
            }
            
            // Find the middle of the interval and round the time up if above 30 seconds
            else if (remainder == 7 && punchTime.getSecond() > 30) {
                remainder = interval - remainder;
                punchTime = punchTime.plusMinutes(remainder);
            }
            
            // Round the time down
            else {
                punchTime = punchTime.minusMinutes(remainder);
            }
            
            // Reset seconds to zero
            punchTime = punchTime.withSecond(0);
        }
        
        // set adjustedTimestamp to the new time
        adjustedTimestamp = LocalDateTime.of(originalTimestamp.toLocalDate(), punchTime);
    
    } 
    
    /* Create Print Methods */
    
    /**
     * Returns a string in the format of "#(badgeid) (event type): (DAY mm/dd/yyyy) (HH:MM:SS)"
     * @return a String representation of the class using the adjusted timestamp
     */
    public String printAdjusted() {
        StringBuilder s = new StringBuilder();
        
        // Append badge id
        s.append("#").append(badge.getId()).append(" ");
        
        // Append Event Type
        s.append(punchType.toString()).append(": ");
        
        // Append Time Stamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        s.append(adjustedTimestamp.format(formatter).toUpperCase());
        
        // Append Adjustment Type
        s.append(" (").append(adjustmentType.toString()).append(")");
        
        // Return final string
        return s.toString();
    }
    
    /**
     * Returns a string in the format of "#(badgeid) (event type): (DAY mm/dd/yyyy) (HH:MM:SS)"
     * @return a String representation of the class using the original timestamp
     */
    public String printOriginal() {
        StringBuilder s = new StringBuilder();
        
        // Append badge id
        s.append("#").append(badge.getId()).append(" ");
        
        // Append Event Type
        s.append(punchType.toString()).append(": ");
        
        // Append Time Stamp
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        s.append(originalTimestamp.format(formatter).toUpperCase());
        
        // Return final string
        return s.toString();
    }
    
}
