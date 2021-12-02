import Config.ConfigurationManager;
import DB.DBConnection;
import DB.SQLQuery;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main (String[] args){
        String filePath = "/home/shubham/IdeaProjects/project4-shubham0831/configuration.json";
        ConfigurationManager config = new ConfigurationManager(filePath);

        try {
            Connection connection = new DBConnection(config.getDBName(), config.getDBUser(), config.getDBPassword()).getDBConnection();
            SQLQuery query = new SQLQuery(connection);
            query.executeSelectAndPrint();
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        System.out.println((ConfigurationManager.class).getClass());
    }
}
