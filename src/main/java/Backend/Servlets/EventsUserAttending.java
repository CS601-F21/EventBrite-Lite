/**
 * Author : Shubham Pareek
 * Purpose : API Call to this servlet returns a JSON of all the events the user will be attending
 */
package Backend.Servlets;

import Backend.Servlets.RequestBodyObjects.User;
import Backend.Servlets.Utilities.ResponseUtils;
import DB.SQLQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EventsUserAttending extends HttpServlet {
    //logger
    private static final Logger LOGGER = LogManager.getLogger(EventsUserAttending.class);

    /**
     * Response is a json of all events a particular user will be attending
     * The uri will contain the userId parameter, which we will then extract and query the database with
     * Sample URI :
     *      GET /userevents?sessionid={sessionid}
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            User user = ResponseUtils.getUser(req);
            resp.setHeader("Access-Control-Allow-Origin", "*");
            //checking whether the user is authenticated or not
            if (!ResponseUtils.userAuthenticated(user)){
                ResponseUtils.send200OkResponse(false, "User not authenticated", resp);
                return;
            }
            //sql query
            ResultSet resultSet = db.getEventsUserAttending(user.getId());
            //helper method to send resultSet as a json object
            ResponseUtils.sendJsonResponse(resultSet, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
