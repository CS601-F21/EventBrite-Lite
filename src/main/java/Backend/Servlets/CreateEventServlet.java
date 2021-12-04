/**
 * Author : Shubham Pareek
 * Class Purpose : Call to this URI creates and stores a new event
 */
package Backend.Servlets;

import Backend.Servlets.RequestBodyObjects.NewEvent;
import DB.SQLQuery;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;

public class CreateEventServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(CreateEventServlet.class);

    /**
     * Since this uri will create a new event, we will require a bunch of information about the event and the
     * user who is actually creating the event.
     * So in this case it makes more sense if the POST request has a body with the required parameters
     *
     * The request body should have the following info :
     *      1) Event_Name
     *      2) Event_Location
     *      3)
     *
     * https://stackoverflow.com/questions/1548782/retrieving-json-object-literal-from-httpservletrequest was used
     * to know how to process the request body of an HttpServletRequest
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            req.getInputStream();
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            Gson gson = new Gson();
            NewEvent newEventDetails = gson.fromJson(req.getReader(), NewEvent.class);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
