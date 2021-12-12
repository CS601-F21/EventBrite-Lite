/**
 * Author : Shubham Pareek
 * Email : spareek@dons.usfca.edu
 * Class Purpose : Query the database
 */
package DB;

import Backend.Servlets.RequestBodyObjects.NewEventBody;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains all the queries the user will make to the db
 */
public class SQLQuery {
    //connection which will be then be used to make queries to the db
    private Connection con;
    //logger for logging purposes
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
    /** Method to get the userinfo from the database */
    public ResultSet getUserInfo (int id) throws SQLException {
        String query = "SELECT * FROM Users WHERE id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, id);
        return statement.executeQuery();
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

    /** Method to search for events by location*/
    public ResultSet searchByLocation (String location) throws SQLException {
        String query = "SELECT * FROM Events WHERE Location = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, location);
        return statement.executeQuery();
    }

    /** Method to search for events by price and location */
    public ResultSet searchByPriceAndLocation (String location, int price) throws SQLException {
        String query = "SELECT * FROM Events WHERE Location = ? AND Price <= ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, location);
        statement.setInt(2, price);
        return statement.executeQuery();
    }

    /**
     *  Method to search for events by word
     *  reference ==> https://stackoverflow.com/a/17322336/13311516
     * */
    public ResultSet searchByWord (String word) throws SQLException {
        String query = "SELECT * FROM Events WHERE NAME LIKE ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, word + "%");
        return statement.executeQuery();
    }

    /**
     * Method to search for events by price and word
     * @param word
     * @param price
     * @return
     * @throws SQLException
     */
    public ResultSet searchByPriceAndWord (String word, int price) throws SQLException {
        String query = "SELECT * FROM Events WHERE NAME LIKE ? AND Price <= ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, word + "%");
        statement.setInt(2, price);
        return statement.executeQuery();
    }

    /**
     * Method to search for events by price
     * @param price
     * @return
     * @throws SQLException
     */
    public ResultSet searchByPrice (int price) throws SQLException {
        String query = "SELECT * FROM Events WHERE Price <= ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, price);
        return statement.executeQuery();
    }


    /**
     * Method to search for events by price, location and word
     * reference ==> https://stackoverflow.com/a/29049440/13311516
     * @param price
     * @param location
     * @param word
     * @return
     * @throws SQLException
     */
    public ResultSet searchByPriceLocationWord (int price, String location, String word) throws SQLException {
        String query = "SELECT * FROM Events WHERE Price <= ? AND Location = ? AND Name Like ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, price);
        statement.setString(2, location);
        statement.setString(3, word + "%");
        return statement.executeQuery();
    }

    /**
     * Method to insert a new Event
     * Returns a boolean whether the insertion was successful or not
     * @param event
     * @throws SQLException
     */
    public boolean insertEvent(NewEventBody event) throws SQLException{
        String insertCommand = "INSERT INTO Events (Name, Location, Attending, Capacity, Organizer, Date, Price) VALUES (?, ?, ?, ?, ?, ?, ?);";

        int attending = 0;
        /**
         * If the user is attending the event they created, we set the initial attendance of that event to 1
         */
        if (event.getAttending()) {
            attending += 1;
        }
        PreparedStatement statement = con.prepareStatement(insertCommand);
        statement.setString(1, event.getName());
        statement.setString(2, event.getLocation());
        statement.setInt(3, attending);
        statement.setInt(4, event.getCapacity());
        statement.setInt(5, event.getOrganizer());

        //jdbc parses the string into a date format on its own, we do not have to do that
        statement.setString(6, event.getDate());
        statement.setInt(7, event.getPrice());
        int i = statement.executeUpdate();

        /**
         * If the user is attending the event they created, we have to update the attending table as well
         */
        if (event.getAttending()) {
            ResultSet resultSet = getEventId(event.getName(), event.getLocation(), event.getDate());
            ArrayList<HashMap<String, String>> result = resultSetToArraylist(resultSet);
            int eventId = Integer.parseInt(result.get(0).get("id"));
            insertEventAttendance(eventId, event.getOrganizer());
        }

        /** i is only greater than 0 if the insertion was successful **/
        return i > 0;
    }

    /** Method to get event details **/
    public ResultSet getEventDetails (int id) throws SQLException {
        String query = "SELECT * FROM Events WHERE Events.id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        return resultSet;
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
        int updatedAttendance = getEventAttendance(eventId) + num;
        boolean success = updateAttendance(updatedAttendance, eventId);
        return success;
    }

    /** Method to update the attendance of an event */
    public boolean updateAttendance (int num, int eventId) throws SQLException {
        String query = "UPDATE Events SET Attending = ? WHERE id = ? ";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, num);
        statement.setInt(2, eventId);
        return statement.executeUpdate() > 0;
    }

    /** Method to get the attendance of an event */
    public int getEventAttendance (int eventId) throws SQLException {
        String query = "SELECT Attending FROM Events WHERE id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, eventId);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<HashMap<String, String>> allRows = resultSetToArraylist(resultSet);
        int count = Integer.parseInt(allRows.get(0).get("Attending"));
        return count;
    }

    /** Method to get the user id from an email */
    public int getUserIdFromEmail (String email) throws SQLException {
        String query = "SELECT id FROM Users WHERE email = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, email);
        ResultSet resultSet = statement.executeQuery();
        ArrayList<HashMap<String, String>> allRows = resultSetToArraylist(resultSet);
        LOGGER.info("receiver id is " + allRows.get(0));
        int count = Integer.parseInt(allRows.get(0).get("id"));
        return count;
    }

    /** Helper method to convert the resultset to an arraylist**/
    public ArrayList<HashMap<String, String>> resultSetToArraylist (ResultSet resultSet) throws SQLException {

        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();

        ArrayList<HashMap<String, String>> allRows = new ArrayList<>();

        while (resultSet.next()){
            HashMap<String, String> singleRow = new HashMap<>();
            for (int i = 1; i <= numColumns; i++){
                String colName = metaData.getColumnName(i);
                String colValue = String.valueOf(resultSet.getObject(i));
                singleRow.put(colName, colValue);
            }
            allRows.add(singleRow);
        }

        return allRows;
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

    /** Method to get all the events a particular user is attending */
    public ResultSet getEventsUserAttending (int id) throws SQLException {
        String query = "SELECT * FROM Events WHERE Events.id in (" +
                    "SELECT Event_id FROM Event_Attendance WHERE User_id = ?)";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, id);
        return statement.executeQuery();
    }

    /** Method to get events hosted by a particular user */
    public ResultSet getEventsByUser (int id) throws SQLException {
        String query = "SELECT * FROM Events WHERE Events.Organizer = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, id);
        return statement.executeQuery();
    }

    /** Method to update the table when the user purchases a ticket */
    public boolean purchaseTicket (int userId, int eventId) throws SQLException{
        String query = "INSERT INTO Event_Attendance (Event_id, User_id) VALUES (?, ?);";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, eventId);
        statement.setInt(2, userId);
        int i = statement.executeUpdate();
        return i > 0; //i is only greater than 0 if the update was successful
    }

    /** Method to check whether the user exists or not */
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

    /** Method to check whether the user exists or not, but this time only with the user if */
    public boolean checkUserExistWithId (int id) throws SQLException {
        String query = "SELECT * FROM Users WHERE id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();

        //if we find a row, then the user exists
        return result.next();
    }

    /** Method to check whether the user has the ticket or not */
    public boolean checkUserHasTicket (int userId, int eventId) throws SQLException {
        LOGGER.info("SQL Got user id " + userId + " and event id " + eventId);
        String query = "SELECT * FROM Event_Attendance WHERE User_id = ? AND Event_id = ?";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setInt(2, eventId);

        ResultSet result = statement.executeQuery();

        return result.next();
    }

    /** Method to check whether the user exists or not */
    public boolean checkUserExist (String email) throws SQLException {
        String query = "SELECT * FROM Users WHERE Email = ?";
        PreparedStatement statement = con.prepareStatement(query);

        statement.setString(1, email);

        ResultSet result = statement.executeQuery();

        return result.next();
    }


    /**
     * Method where we execute the queries where the user is searching for something
     * and get the ResultSet Used only if user wants to get data from the database, different
     * method for inserting into db.
     * Returns ResultSet
     * @throws SQLException
     */
    private ResultSet executeFetchQuery (String query) throws SQLException {
        PreparedStatement statement = con.prepareStatement(query);
        ResultSet results = statement.executeQuery();
        return results;
    }

    /**
     * Method to transfer tickets from one user to the other
     * @param from
     * @param receiverId
     * @param eventId
     * @return
     * @throws SQLException
     */
    public boolean transferTicket(int from, int receiverId, int eventId) throws SQLException {
        String query = "UPDATE  Event_Attendance\n" +
                "SET \n" +
                " User_id = ?\n" +
                "WHERE\n" +
                "    User_id = ? AND Event_id = ?;";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setInt(1, receiverId);
        statement.setInt(2, from);
        statement.setInt(3, eventId);
        int i = statement.executeUpdate();
        return i > 0;
    }

    /**
     * Method to update the users preferred name
     * @param id
     * @param name
     * @return
     * @throws SQLException
     */
    public boolean updateUserName (int id, String name) throws SQLException {
        String query = "UPDATE  Users\n" +
                "SET \n" +
                " Preferred_Name = ?\n" +
                "WHERE\n" +
                "    id = ?;";
        PreparedStatement statement = con.prepareStatement(query);
        statement.setString(1, name);
        statement.setInt(2, id);
        int i = statement.executeUpdate();
        return i > 0;
    }

}

//        while(results.next()) {
//            System.out.printf("Name: %s\n", results.getString("Name"));
//            System.out.printf("Location: %s\n", results.getString("Location"));
//            System.out.printf("Date: %s\n", results.getDate("Date"));
//            System.out.printf("Organizer: %s\n", results.getString("Organizer"));
//            System.out.println("---------------------");
//        }