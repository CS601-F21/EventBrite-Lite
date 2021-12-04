/**
 * Author : Shubham Pareek
 * Purpose : Call to this URI is made when the user clicks on purchase ticket
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
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import static Backend.Servlets.Utilities.ResponseUtils.getResponse;

public class PurchaseTicketServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(PurchaseTicketServlet.class);

    /**
     * POST Request, URI will have 2 params the userId and the eventId
     *      Sample URI :
     *            POST /purchaseticket?userid={userId}&eventid={eventid}
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        HashMap<String, String> responseMap = new HashMap<>();
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            int userId = Integer.parseInt(req.getParameter("userid"));
            int eventId = Integer.parseInt(req.getParameter("eventid"));

            LOGGER.info("Got user id as " + userId);
            LOGGER.info("Got event id as " + userId);

            //we have to update the event attendance table as well as update the attending col in the event
            //table as well
            //both the methods will give a boolean whether the insertion/updation was successful or not
//            boolean successful = db.purchaseTicket(userId, eventId);
            boolean successful = db.updateEventAttending(1, eventId);
            LOGGER.info("success is " + successful);
            if (successful) {
                /**
                 * Response status 200, with ok set to true
                 */
                responseMap.put("ok", "true");
            } else {
                /**
                 * Response status 200, with ok set to false
                 */
                responseMap.put("ok", "false");
                responseMap.put("message" , "failed to update database");
            }

            //making this conversion since we already have a method which takes in an arraylist
            //of map and converts it into json string
            ArrayList<HashMap<String, String>> responseList = new ArrayList<>();
            responseList.add(responseMap);
            //getting the json string
            String response =  ResponseUtils.getResponse(responseList);
            //sending the response
            ResponseUtils.send200JsonResponse(response, resp);
        } catch (SQLException e) {
            try {
                PrintWriter writer = resp.getWriter();
                resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
                writer.flush();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
