package Backend;

import DB.SQLQuery;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.sql.Connection;

public class MainServer {
    private int port;
    private Server server;
    private ServletContextHandler context;

    public MainServer (int port) {
        this.port = port;
        server = new Server(this.port);
        context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
    }

    public void startServer () throws Exception {
        server.start();
    }

    public void setDBConnection (String name, SQLQuery db){
        context.setAttribute(name, db);
    }

    public void addServlet (Class handler, String path){
        context.addServlet(handler, path);
    }
}
