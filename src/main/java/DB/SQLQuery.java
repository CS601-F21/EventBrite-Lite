/**
 * Author : Shubham Pareek
 * Email : spareek@dons.usfca.edu
 * Class Purpose : Query the database
 */
package DB;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class SQLQuery {
    private Connection con;

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
    public void insertEvent (String name, String location, int attending, int capacity, int organizer, String date) throws SQLException{
        String insertCommand = "INSERT INTO Events (Name, Location, Attending, Capacity, Organizer, Date) VALUES (?, ?, ?, ?, ?, ?);";

        PreparedStatement statement = con.prepareStatement(insertCommand);
        statement.setString(1, name);
        statement.setString(2, location);
        statement.setInt(3, attending);
        statement.setInt(4, capacity);
        statement.setInt(5, organizer);

        //jdbc parses the string into a date format on its own, we do not have to do that
        statement.setString(6, date);
        statement.executeUpdate();
    }

    /**
     * Method to insert a new user
     * @param firstName
     * @param lastName
     * @param contact
     * @param email
     * @throws SQLException
     */
    public void insertUser (String firstName, String lastName, int contact , String email) throws SQLException {
        String insertCommand = "INSERT INTO Users (First_Name, Last_Name, Contact, Email) VALUES (?, ?, ?, ?, ?);";

        PreparedStatement statement = con.prepareStatement(insertCommand);
        statement.setString(1, firstName);
        statement.setString(2, lastName);
        statement.setInt(3, contact);
        statement.setString(4, email);
        statement.executeUpdate();
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