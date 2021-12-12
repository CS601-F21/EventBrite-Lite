/**
 * Author : Sami Rollins
 * Modified By : Shubham Pareek
 * Purpose : Log the user out
 */
package Backend.Servlets.Authentication;

import Backend.Servlets.Utilities.LoginServerConstants;
import Backend.Servlets.Utilities.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Handles a request to sign out
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // log out by invalidating the session
        req.getSession().invalidate();
        ResponseUtils.send200OkResponse(true, null, resp);
    }
}
