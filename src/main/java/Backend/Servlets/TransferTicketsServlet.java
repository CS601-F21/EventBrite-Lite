/**
 * Author : Shubham Pareek
 * Purpose : API Call to this transfers the tickets between users
 */
package Backend.Servlets;

import Backend.Servlets.RequestBodyObjects.TransferTicketBody;
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

public class TransferTicketsServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(TransferTicketsServlet.class);

    /**
     * What this api call does is that it transfers the ticket from user A to user B
     * The parameters will be passed from the body and not in the uri itself, since for user B
     * we will only have their email address
     * Sample URI /transferticket
     *          {
     *              from : {user id of the user transferring the ticket}
     *              to : {email address of the user who the ticket is being transferred to}
     *              eventId : {id of the event}
     *          }
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            String requestStr = IOUtils.toString(req.getInputStream());
            Gson gson = new Gson();
            TransferTicketBody body = gson.fromJson(requestStr, TransferTicketBody.class);

            LOGGER.info("Transfer ticket recieved ");
            LOGGER.info(body.toString());
            boolean userHasTicket = db.checkUserHasTicket(body.getFrom(), body.getEventId());
            boolean receiverExists = db.checkUserExist(body.getEmail());

            if (!userHasTicket) {
                ResponseUtils.send200OkResponse(false, "User does not own this ticket", resp);
                return;
            }

            if (!receiverExists){
                ResponseUtils.send200OkResponse(false, "Receiver does not exist", resp);
                return;
            }
            int receiverId = db.getUserIdFromEmail(body.getEmail());
            boolean success  = db.transferTicket(body.getFrom(), receiverId, body.getEventId());
            if (success){
                ResponseUtils.send200OkResponse(true, null, resp);
            } else {
                ResponseUtils.send200OkResponse(false, "unable to update sql", resp);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                ResponseUtils.send200OkResponse(false, "unable to update sql", resp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
