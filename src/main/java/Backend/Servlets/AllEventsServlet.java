/**
 * Author : Shubham Pareek
 * Purpose : API Call to this servlet returns a JSON of all the events
 */
package Backend.Servlets;

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
import java.util.ArrayList;

public class AllEventsServlet extends HttpServlet {
    //logger
    private static final Logger LOGGER = LogManager.getLogger(AllEventsServlet.class);

    /**
     * A GET Request to this uri will not require any additional details in either the request body or
     * the url itself.
     * All this method will do is give a response back in json format with the details of all the events
     * The user does not have to be authenticated to be able to make this api call
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            //retrieving the query object
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            //getting the result set from the query
            ResultSet resultSet = db.getAllEvents();
            //setting header for cors and sending the response
            resp.setHeader("Access-Control-Allow-Origin", "*");
            ResponseUtils.sendJsonResponse(resultSet, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
