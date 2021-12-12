/**
 * Author : Shubham Pareek
 * Class Purpose : Call to this URI creates and stores a new event
 */
package Backend.Servlets;

import Backend.Servlets.RequestBodyObjects.NewEventBody;
import Backend.Servlets.RequestBodyObjects.User;
import Backend.Servlets.Utilities.ResponseUtils;

import DB.SQLQuery;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

public class CreateEventServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(CreateEventServlet.class);

    /**
     * Since this uri will create a new event, we will require a bunch of information about the event and the
     * user who is actually creating the event.
     * So in this case it makes more sense if the POST request has a body with the required parameters
     * The user has to be authenticated to create an event
     * The request body should have the following info :
     *      1) name
     *      2) location
     *      3) attending //attending will be a boolean
     *      4) capacity
     *      5) price
     *      6) date
     *
     * Sample URI call is
     *      /createevent/sessionid={sessionid}
     *      //the token is used to get the id of the user creating the event
     *      with the rest of the details in the POST Body
     *
     * https://stackoverflow.com/questions/1548782/retrieving-json-object-literal-from-httpservletrequest was used
     * to know how to process the request body of an HttpServletRequest
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            //getting the query object
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            //getting the user info from the request
            User user = ResponseUtils.getUser(req);
            LOGGER.info("The USER Object is the following");
            LOGGER.info(user.toString());
            //if the user is not authenticated, we send this response
            if (!ResponseUtils.userAuthenticated(user)){
                ResponseUtils.send200OkResponse(false, "User not authenticated", resp);
                return;
            }
            /**
             * If the user is authenticated, we carry on with creating the event
             * First we get the body from the organizers' id from the user object
             */
//            req.getInputStream();
//            int organizerId = db.getUserIdFromEmail(user.getEmail());
            int organizerId = user.getId();
            Gson gson = new Gson();
            //convert the request body to a string
            String requestStr = IOUtils.toString(req.getInputStream());
            //convert the request body string to a newEventBody object
            NewEventBody newEventBody = gson.fromJson(requestStr, NewEventBody.class);
            newEventBody.setOrganizer(organizerId);
            //insert the new event into the db
            boolean success = db.insertEvent(newEventBody);
            resp.setHeader("Access-Control-Allow-Origin", "*");
            ResponseUtils.send200OkResponse(success, null, resp);
        } catch (IOException e) {
            LOGGER.info("IO Exception");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                ResponseUtils.send200OkResponse(false, "SQL ERROR", resp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
