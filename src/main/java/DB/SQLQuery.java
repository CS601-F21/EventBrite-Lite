package DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SQLQuery {
    Connection con;

    public SQLQuery (Connection con){
        this.con = con;
    }

    /**
     * A method to demonstrate using a PrepareStatement to execute a database select
     * @throws SQLException
     */
    public ResultSet executeSelectAndPrint() throws SQLException {
        String selectAllContactsSql = "SELECT * FROM Events;";
        PreparedStatement selectAllContactsStmt = con.prepareStatement(selectAllContactsSql);
        ResultSet results = selectAllContactsStmt.executeQuery();
        while(results.next()) {
            System.out.printf("Name: %s\n", results.getString("Name"));
            System.out.printf("Location: %s\n", results.getString("Location"));
            System.out.printf("Date: %s\n", results.getDate("Date"));
            System.out.printf("Organizer: %s\n", results.getString("Organizer"));
            System.out.println("---------------------");
        }
        return results;
    }
}
