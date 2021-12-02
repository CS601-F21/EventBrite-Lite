package DB;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    private BasicDataSource dataSource;
    private String dbName;
    private String username;
    private String password;

    public DBConnection (String dbName, String username, String password){
        this.dbName = dbName;
        this.username = username;
        this.password = password;
        instantiateDataSource();
    }

    //The following method is from :
    //https://examples.javacodegeeks.com/core-java/apache/commons/dbcp/dbcp-connection-pooling-example/
    private BasicDataSource instantiateDataSource() {
        if (dataSource == null){
            BasicDataSource ds = new BasicDataSource();
            ds.setUrl("jdbc:mysql://localhost/"+dbName);
            ds.setUsername(username);
            ds.setPassword(password);


//            ds.setMinIdle(5);
//            ds.setMaxIdle(10);
            ds.setMaxOpenPreparedStatements(100);

            dataSource = ds;
        }
        return dataSource;
    }

    public Connection getDBConnection () throws SQLException {
        return dataSource.getConnection();
    }

//    public static void main(String[] args){
//        try (
//                BasicDataSource dataSource = DBConnection.getDataSource();
//                Connection con = dataSource.getConnection();
//        ){
//            executeSelectAndPrint(con);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}
