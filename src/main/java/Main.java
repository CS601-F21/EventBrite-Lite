import Backend.MainServer;
import Backend.Servlets.*;
import Config.ConfigurationManager;
import DB.DBConnection;
import DB.SQLQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class Main {
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static void main (String[] args){
        String filePath = "/home/shubham/IdeaProjects/project4-shubham0831/configuration.json";
        ConfigurationManager config = new ConfigurationManager(filePath);
        HashMap<String, String> test = config.getSlackConfig();

        BasicConfigurator.configure(); //configuring logger

        try {
            Connection connection = new DBConnection(config.getDBName(), config.getDBUser(), config.getDBPassword()).getDBConnection();
            SQLQuery query = new SQLQuery(connection);
//            ResultSet resultSet = query.getUserId("Shubham", "Pareek", "spareek@dons.usfca.edu");
//            query.checkUserExist("Naman", "Lashkari", "nlashkar@gmail.com");
//            ResultSet resultSet = query.getEventId("Event-A", "Singapore", "20220210");

//            query.insertEvent("Event-I", "Bahrain", 0, 1000, 2, "22-08-28");
//            while (resultSet.next()){
//                System.out.println("id is : " + resultSet.getInt("id"));
//            }

            MainServer server = new MainServer(8080);
            //home page, not actually that important, remove it later once login is done from the front-end
            server.addServlet(LandingServlet.class, "/"); //home page
            //login servlet, might be removed once login is done from the front-end
            server.addServlet(LoginServlet.class, "/login");
            //logout server, same as earlier
            server.addServlet(LogoutServlet.class, "/logout");
            //response will be a json object with the details of all the events
            server.addServlet(AllEventsServlet.class, "/allevents");
            //response will be a json object with the details of all the event the particular user is going to
            //servlet will only handle GET Request, details about the request in the class
            server.addServlet(EventsUserAttending.class, "/userevents");
            //response will be a json object with the event details of all the event hosted by a particular user
            //servlet will only handle GET Request, details about the request in the class
            server.addServlet(EventsByUserServlet.class, "/eventsbyuser");
            //response will be a json object which will let the front-end know whether the transfer was successful or not
            //servlet will only handle PUT Request, details about the request in the class
            server.addServlet(TransferTicketsServlet.class, "/transferticket");
            //response will be a json object with all the details of events which match the search query the user has given
            //servlet will only handle GET Request, details about the request in the class
            server.addServlet(SearchEventServlet.class, "/searchevent");
            //response will be a json object which will let the front-end know whether the updation was successful or nt
            //servlet will only handle PUT Request, details about the request in the class
            server.addServlet(UpdatePreferredNameServlet.class, "/updateusername");
            //response will be a json object containing details about a single event
            //servlet will only handle GET Request, details about the request in the class
            server.addServlet(SingleEventServlet.class, "/event");
            //response will be a json object informing the front-end whether the purchase was successful or not
            //servlet will only handle POST Request, details about the request in the class
            server.addServlet(PurchaseTicketServlet.class, "/purchaseticket");
            //response will be a json object informing the front-end whether the event creation was successful or not
            //servlet will only handle POST Request, details about the request in the class
            server.addServlet(CreateEventServlet.class, "/createevent");

            //adding the database connection as an attribute to the context
            server.setDBConnection("db", query);

            //adding login information as an attribute to the context
            server.setSlackConfig("slackAuthentication", config.getSlackConfig());
            server.startServer();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
