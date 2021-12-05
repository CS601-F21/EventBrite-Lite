/**
 * Author : Shubham Pareek
 * Purpose : API Call to this updates the preferred name of the user
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
import java.sql.SQLException;

public class UpdatePreferredNameServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(UpdatePreferredNameServlet.class);

    /**
     * API to update the preferred name of the user, since we only have two params and both are safe
     * we can pass them in the url itself
     * Sample api :
     *          /updateusername?id={userId}&pref={newname}
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            int userId = Integer.parseInt(req.getParameter("id"));
            String pref = req.getParameter("pref");

            boolean success = db.updateUserName(userId, pref);

            if (success) {
                ResponseUtils.send200OkResponse(true, null, resp);
            } else {
                ResponseUtils.send200OkResponse(false, "unable to update the name", resp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
