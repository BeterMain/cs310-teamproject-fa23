package edu.jsu.mcis.cs310.tas_fa23.dao;

import edu.jsu.mcis.cs310.tas_fa23.Badge;
import edu.jsu.mcis.cs310.tas_fa23.Punch;
import edu.jsu.mcis.cs310.tas_fa23.EventType;
import java.sql.*;
import java.time.LocalDateTime;

public class PunchDAO {
    private static final String QUERY_FIND = "SELECT * FROM event WHERE id = ?";

    private final DAOFactory daoFactory;

    PunchDAO(DAOFactory daoFactory) {

        this.daoFactory = daoFactory;

    }

    /**
     *  Searches through the "event" database table, looking for the integer "id"
     * @param id First column in database
     * @return Punch object
     */
    public Punch find(int id) {

        Punch punch = null;

        PreparedStatement ps = null;
        ResultSet rs = null;

        try {

            Connection conn = daoFactory.getConnection();

            if (conn.isValid(0)) {

                ps = conn.prepareStatement(QUERY_FIND);
                ps.setInt(1, id);

                boolean hasresults = ps.execute();

                if (hasresults) {

                    rs = ps.getResultSet();

                    while (rs.next()) {
                        
                        // Convert the timestamp in the database to LocalDateTime in java
                        LocalDateTime timestamp = rs.getTimestamp("timestamp").toLocalDateTime();
                        
                        // Get the terminal id from the database
                        int terminalId = rs.getInt("terminalid");
                        
                        // Create a new Badge object that has the required badge id
                        Badge badge = new Badge(rs.getString("badgeid"),null);
                        
                        // Check the "eventtypeid" to set the EventType in the final constructor
                        EventType event = null;
                        switch (rs.getInt("eventtypeid")) {
                            case 0:
                                event = EventType.CLOCK_OUT;
                                break;
                            case 1:
                                event = EventType.CLOCK_IN;
                                break;
                            case 2:
                                event = EventType.TIME_OUT;
                                break;
                        }
                        
                        // Set the return variable
                        punch = new Punch(id, terminalId, badge, timestamp, event);
                    }

                }

            }

        } catch (SQLException e) {

            throw new DAOException(e.getMessage());

        } finally {

            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    throw new DAOException(e.getMessage());
                }
            }

        }

        return punch;

    }
}
