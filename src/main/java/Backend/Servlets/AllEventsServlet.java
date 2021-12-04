/**
 * Author : Shubham Pareek
 * Purpose : API Call to this returns a JSON of all the events
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
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;


public class AllEventsServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(AllEventsServlet.class);

    /**
     * A POST Request to this uri will not require any additional details in either the request body or
     * the url itself.
     * All this method will do is give a response back in json format with the details of all the events
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            //sql query
            ResultSet resultSet = db.getAllEvents();
            //helper method to send resultSet as a json object
            ResponseUtils.sendJsonResponse(resultSet, resp);


        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
