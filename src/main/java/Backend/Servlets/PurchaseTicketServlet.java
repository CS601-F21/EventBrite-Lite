/**
 * Author : Shubham Pareek
 * Purpose : Call to this URI is made when the user clicks on purchase ticket
 */
package Backend.Servlets;

import DB.SQLQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.sql.SQLException;

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
            boolean successful = db.purchaseTicket(userId, eventId);
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
}
