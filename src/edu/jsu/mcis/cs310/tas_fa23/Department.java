package edu.jsu.mcis.cs310.tas_fa23;

/**
 *
 * @author Connor Muncher
 */
public class Department 
{    
    
    private final String description;
    
    private final int id, terminalID;
    
    /**
     *
     * @param id - input for the object's id
     * @param description - input for the department's descriptions
     * @param terminalID - input for the department's terminalID
     * this function is used to create a new Department
     */
    public Department (int id, String description, int terminalID)
    {
        this.id = id;
        this.description = description;
        this.terminalID = terminalID;
    }
    
    /**
     *
     * @return - return's the id for the department
     */
    public int getID()
    {
        return id;
    }
    
    /**
     *
     * @return - return's the department's description
     */
    public String getDescription()
    {
        return description;
    }
    
    /**
     *
     * @return - returns the terminalID of the department
     */
    public int getTerminalID()
    {
        return terminalID;    
    }
    
    /**
     *
     * @return - return's the department's values as a string.
     */
    @Override
    public String toString()
    {
        StringBuilder sBuilder = new StringBuilder(); 
        
        sBuilder.append('#').append(id).append(" ");
        sBuilder.append('(').append( description).append(')').append(", ");
        sBuilder.append("Terminal ID: ").append(terminalID);
        
        return sBuilder.toString();
    }
}