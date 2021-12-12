/**
 * Author Name : Shubham Pareek
 * Class Purpose : Main Class
 */

import Backend.MainServer;
import Backend.Servlets.*;
import Backend.Servlets.Authentication.GetSessionIDServlet;
import Backend.Servlets.Authentication.LandingServlet;
import Backend.Servlets.Authentication.LoginServlet;
import Backend.Servlets.Authentication.LogoutServlet;
import Config.ConfigurationManager;
import DB.DBConnection;
import DB.SQLQuery;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * This is the class where we create the server and add the various mappings to it, allowing the user to make the various api calls
 */
public class Main {
    //logger for loggin purposes
    private static final Logger LOGGER = LogManager.getLogger(Main.class);
    public static void main (String[] args){
        //config file path
        String filePath = "/home/shubham/IdeaProjects/project4-shubham0831/configuration.json";
        /*
            The config manager takes as input the config file and lets the user use the getters to get the various
            configs
         */
        ConfigurationManager config = new ConfigurationManager(filePath);

        //configuring logger
        BasicConfigurator.configure();

        try {
            /**
             * We first create a DBConnection and pass it the required params and the get the connection object from it, the connection object is then
             * passed to the SQLQuery object which is the object using which the user will make the queries to the DB
             */
            Connection connection = new DBConnection(config.getDBName(), config.getDBUser(), config.getDBPassword()).getDBConnection();
            //the SQLQuery object
            SQLQuery query = new SQLQuery(connection);

            //declaring the main server
            MainServer server = new MainServer(8080);
            //adding the database connection as an attribute to the context
            server.setDBConnection("db", query);

            //home page
            server.addServlet(LandingServlet.class, "/"); //home page
            //login servlet, called by the front-end end usually with the code param to authenticate the user
            server.addServlet(LoginServlet.class, "/login");
            //logout server, used by the user to logout
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
            //response will be a json object informing the front-end whether the event creation was successful or not
            //servlet will only handle POST Request, details about the request in the class
            server.addServlet(UserInformationServlet.class, "/userinfo");
            //response will be a json object informing the front-end whether the event creation was successful or not
            //servlet will only handle POST Request, details about the request in the class
            server.addServlet(SearchEventServlet.class, "/search");
            //response will be a json object informing the front-end whether the event creation was successful or not
            //servlet will only handle POST Request, details about the request in the class
            server.addServlet(GetSessionIDServlet.class, "/sessionid");

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
