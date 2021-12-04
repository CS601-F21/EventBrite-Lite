package Backend;

import DB.SQLQuery;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.sql.Connection;
import java.util.HashMap;

public class MainServer {
    private int port;
    private Server server;
    private ServletContextHandler context;

    public MainServer (int port) {
        this.port = port;
        server = new Server(this.port);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    }

    public void startServer () throws Exception {
        server.setHandler(context);
        server.start();
    }

    public void setDBConnection (String name, SQLQuery db){
        context.setAttribute(name, db);
    }

    public void setSlackConfig(String name, HashMap<String, String> slackConfig){
        context.setAttribute(name, slackConfig);
    }

    public void addServlet (Class handler, String path){
        context.addServlet(handler, path);
    }
}
