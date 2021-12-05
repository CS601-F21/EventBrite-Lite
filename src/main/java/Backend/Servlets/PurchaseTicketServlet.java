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
import static Backend.Servlets.Utilities.ResponseUtils.send200OkResponse;

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

        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            int userId = Integer.parseInt(req.getParameter("userid"));
            int eventId = Integer.parseInt(req.getParameter("eventid"));

            LOGGER.info("Got user id as " + userId);
            LOGGER.info("Got event id as " + userId);

            //we have to update the event attendance table as well as update the attending col in the event
            //table as well
            //both the methods will give a boolean whether the insertion/updation was successful or not
            boolean successful = db.purchaseTicket(userId, eventId) && db.updateEventAttending(1, eventId);
            LOGGER.info("success is " + successful);
            send200OkResponse(successful, null, resp);

        } catch (SQLException e) {
            try {
                LOGGER.info("SQL Error");
                send200OkResponse(false, "SQL ERROR", resp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
