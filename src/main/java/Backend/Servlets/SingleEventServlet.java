/**
 * Author : Shubham Pareek
 * Purpose : API Call to this servlet returns a JSON of details about a single event
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

public class SingleEventServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(SingleEventServlet.class);

    /**
     * Response is a json of details about a particular event
     * The uri will contain the eventId parameter
     * Sample URI
     *      GET /event?id={id}
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            //getting the db
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            //getting the event id
            Integer id = Integer.parseInt(req.getParameter("id"));
            //making the query
            ResultSet resultSet = db.getEventDetails(id);
            resp.setHeader("Access-Control-Allow-Origin", "*");
            //sending the response
            ResponseUtils.sendJsonResponse(resultSet, resp);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
