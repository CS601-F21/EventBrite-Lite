/**
 * Author : Shubham Pareek
 * Purpose : Server class, called by the user to start the server at a specific port
 */
package Backend;

import DB.SQLQuery;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.util.HashMap;

public class MainServer {
    private int port;
    private Server server;
    private ServletContextHandler context;

    /**
     * User only has to give the port number to the constructor
     * @param port
     */
    public MainServer (int port) {
        this.port = port;
        server = new Server(this.port);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    }

    /**
     * Method the user calls when they want to start the server
     * @throws Exception
     */
    public void startServer () throws Exception {
        server.setHandler(context);
        server.start();
    }

    /**
     * Method to add the database query object to the server context, so that it can be used by different servlets
     * @param name
     * @param db
     */
    public void setDBConnection (String name, SQLQuery db){
        context.setAttribute(name, db);
    }

    /**
     * Method to add the slack config to the server context
     * @param name
     * @param slackConfig
     */
    public void setSlackConfig(String name, HashMap<String, String> slackConfig){
        context.setAttribute(name, slackConfig);
    }

    /**
     * Method to add the servlet
     * @param handler
     * @param path
     */
    public void addServlet (Class handler, String path){
        context.addServlet(handler, path);
    }
}
