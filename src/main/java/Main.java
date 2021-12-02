import Backend.MainServer;
import Backend.Servlets.EchoServlet;
import Backend.Servlets.TestServlet;
import Config.ConfigurationManager;
import DB.DBConnection;
import DB.SQLQuery;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main (String[] args){
        String filePath = "/home/shubham/IdeaProjects/project4-shubham0831/configuration.json";
        ConfigurationManager config = new ConfigurationManager(filePath);

        try {
            Connection connection = new DBConnection(config.getDBName(), config.getDBUser(), config.getDBPassword()).getDBConnection();
            SQLQuery query = new SQLQuery(connection);
//            ResultSet resultSet = query.getUserId("Shubham", "Pareek", "spareek@dons.usfca.edu");
//            ResultSet resultSet = query.getEventId("Event-A", "Singapore", "20220210");

            query.insertEvent("Event-I", "Bahrain", 0, 1000, 2, "22-08-28");
//            while (resultSet.next()){
//                System.out.println("id is : " + resultSet.getInt("id"));
//            }

//            MainServer server = new MainServer(8080);
//            server.addServlet(EchoServlet.class, "/echo");
//            server.addServlet(TestServlet.class, "/test");
//            server.setDBConnection("db", query);
//            server.startServer();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
