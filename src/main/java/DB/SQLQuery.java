/**
 * Author : Shubham Pareek
 * Email : spareek@dons.usfca.edu
 * Class Purpose : Query the database
 */
package DB;

import Backend.Servlets.CreateEventServlet;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLQuery {
    private Connection con;
    private static final Logger LOGGER = LogManager.getLogger(SQLQuery.class);

    /**
     * Constructor, takes in a connection to the database as the input
     * @param con
     */
    public SQLQuery (Connection con){
        this.con = con;
    }

    /**
     * Method to get a ResultSet of all events
     * Returns ResultSet
     * @throws SQLException
     */
    public ResultSet getAllEvents() throws SQLException {
        String query = "SELECT * FROM Events;";
        return executeFetchQuery(query);
    }

    /**
     * Method to get a ResultSet of all users
     * Returns ResultSet
     * @throws SQLException
     */
    public ResultSet getAllUsers () throws SQLException {
        String query = "SELECT * FROM Users;";
        return executeFetchQuery(query);
    }

    /**
     * Method to get a ResultSet of all events and which users
     * are attending the event
     * Returns ResultSet
     * @throws SQLException
     */
    public ResultSet getAllAttendance () throws SQLException {
        String query = "SELECT * FROM Event_Attendance;";
        return executeFetchQuery(query);
    }

    /**
     * Method to insert a new Event
     * @param name
     * @param location
     * @param attending
     * @param capacity
     * @param organizer
     * @param date
     * @throws SQLException
     */
    public boolean insertEvent (String name, String location, int attending, int capacity, int organizer, String date) throws SQLException{
        String insertCommand = "INSERT INTO Events (Name, Location, Attending, Capacity, Organizer, Date) VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement statement = con.prepareStatement(insertCommand);
        statement.setString(1, name);
        statement.setString(2, location);
        statement.setInt(3, attending);
        statement.setInt(4, capacity);
        statement.setInt(5, organizer);

        //jdbc parses the string into a date format on its own, we do not have to do that
        statement.setString(6, date);
        int i = statement.executeUpdate();
        return i > 0; //i is only greater than 0 if the insertion was successful
    }

    /**
     * Method to insert a new user
     * @param firstName
     * @param lastName
     * @param preferredName
     * @param email
     * @throws SQLException
     */
    public boolean insertUser (String firstName, String lastName, String preferredName , String email) throws SQLException {
        String insertCommand = "INSERT INTO Users (First_Name, Last_Name, Preferred_Name, Email) VALUES (?, ?, ?, ?);";

        PreparedStatement statement = con.prepareStatement(insertCommand);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, preferredName);
        statement.setString(4, email);
        int i  = statement.executeUpdate();
        return i > 0; //i is only greater than 0 if the insertion was successful
    }

    /**
     * Method to insert into the EventAttendance table
     * @param eventId
     * @param userId
     * @throws SQLException
     */
    public void insertEventAttendance (int eventId, int userId) throws SQLException {
        String insertCommand = "INSERT INTO Event_Attendance (Event_id, User_id) VALUES (?, ?);";
        PreparedStatement statement = con.prepareStatement(insertCommand);
        statement.setInt(1, eventId);
        statement.setInt(2, userId);
        statement.executeUpdate();
    }

    /**
     * Method to get the id of a particular user. The combination of the input parameters will result in
     * a unique user hence we can return the id of the user
     * @param firstName
     * @param lastName
     * @param email
     * @return
     * @throws SQLException
     */
    public ResultSet getUserId (String firstName, String lastName, String email) throws SQLException {
        String query = "SELECT id FROM Users Where First_Name = ? AND Last_Name = ? AND Email = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);

        ResultSet result = statement.executeQuery();
        return result;
    }

    /**
     * Method to update the attending col of the event in the events table
     * @param num
     * @return
     */
    public boolean updateEventAttending (int num, int eventId) throws SQLException {
        String query = "SELECT Attending FROM Events WHERE id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, eventId);
        ResultSet resultSet = statement.executeQuery();
        /**
         * TODO, GET COUNT OF ATTENDANCE FROM THE Events TABLE
         */
        while (resultSet.next()){
            LOGGER.info(resultSet.getMetaData().getColumnName(1));
        }
        int currentAttendance = resultSet.getRow();
        LOGGER.info("Current Attendance is " + currentAttendance);
        return true;
//        query = "UPDATE Events SET Attending = ? WHERE id = ? ";
//        statement = con.prepareStatement(query);
//        statement.setInt(1, currentAttendance+num);
//        statement.setInt(2, eventId);
//        return statement.executeUpdate() > 0;
    }

    /**
     * Method to get the id of an event
     * @param name
     * @param location
     * @param date
     * @return
     * @throws SQLException
     */
    public ResultSet getEventId (String name, String location, String date) throws SQLException {
        String query = "SELECT id FROM Events WHERE Name = ? AND Location = ? AND Date = ?";
        PreparedStatement statement = con.prepareStatement(query);

        statement.setString(1, name);
        statement.setString(2, location);

        //jdbc parses the string into a date format on its own, we do not have to do that
        statement.setString(3, date);

        ResultSet result = statement.executeQuery();
        return result;
    }

    public ResultSet getEventsUserAttending (int id) throws SQLException {
        String query = "SELECT * FROM Events WHERE Events.id in (" +
                    "SELECT Event_id FROM Event_Attendance WHERE User_id = ?)";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, id);
        return statement.executeQuery();
    }

    public ResultSet getEventsByUser (int id) throws SQLException {
        String query = "SELECT * FROM Events WHERE Events.Organizer = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, id);
        return statement.executeQuery();
    }

    public boolean purchaseTicket (int userId, int eventId) throws SQLException{
        String query = "INSERT INTO Event_Attendance (Event_id, User_id) VALUES (?, ?);";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, eventId);
        statement.setInt(2, userId);
        int i = statement.executeUpdate();
        return i > 0; //i is only greater than 0 if the update was successful
    }

    public boolean userExists (String firstName, String lastName, String email) throws SQLException {
        String query = "SELECT * FROM Users WHERE First_Name = ? AND Last_Name = ? AND Email = ?";

        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setString(3, email);

        ResultSet result = statement.executeQuery();

        //if we find a row, then the user exists
        return result.next();
    }


    /**
     * Method where we actually execute the query and get the ResultSet
     * Used only if user wants to get data from the database, different
     * method for inserting into db.
     * Returns ResultSet
     * @throws SQLException
     */
    private ResultSet executeFetchQuery (String query) throws SQLException {
        PreparedStatement statement = con.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        return results;
    }
}

//        while(results.next()) {
//            System.out.printf("Name: %s\n", results.getString("Name"));
//            System.out.printf("Location: %s\n", results.getString("Location"));
//            System.out.printf("Date: %s\n", results.getDate("Date"));
//            System.out.printf("Organizer: %s\n", results.getString("Organizer"));
//            System.out.println("---------------------");
//        }