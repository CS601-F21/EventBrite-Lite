package Backend;

import org.eclipse.jetty.servlet.ServletContextHandler;

public class Server {
    private int port;
    private Server server;
    private ServletContextHandler context;

    public Server (int port) {
        this.port = port;
        server = new Server(this.port);
        this.context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    }

    public void addContext (Class handler, String path){
        context.addServlet(handler, path);
    }






}
