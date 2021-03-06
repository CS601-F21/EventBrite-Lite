/**
 * Author : Shubham Pareek
 * Purpose : API Call to this servlet returns a JSON of all the events created by user
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

public class EventsByUserServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(EventsByUserServlet.class);

    /**
     * Response is a JSON object with all events created by a particular user
     * The uri will contain the userId parameter, which we will then extract and query the database with
     * Sample URI :
     *      GET /eventsbyuser?sessionid={id}
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
            //checking if the user is authenticated
            if (!ResponseUtils.userAuthenticated(user)){
                ResponseUtils.send200OkResponse(false, "User not authenticated", resp);
                return;
            }

            //sql query
            ResultSet resultSet = db.getEventsByUser(user.getId());
            //helper method to send resultSet as a json object
            ResponseUtils.sendJsonResponse(resultSet, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
