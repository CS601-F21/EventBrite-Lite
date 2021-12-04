/**
 * Author : Sami Rollins
 * Modified By : Shubham Pareek
 * Purpose : Handles the Login
 */

package Backend.Servlets;

import Backend.Servlets.Utilities.ClientInfo;
import Backend.Servlets.Utilities.HTTPFetcher;
import Backend.Servlets.Utilities.LoginServerConstants;
import Backend.Servlets.Utilities.LoginUtilities;
import DB.SQLQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Handles the login for the user. Once a user has successfully been logged in, we check the database for whether the user
 * exists or not, if not, we create an entry in the database for the said user.
 * We then send a json body to the front-end with the user-info such as the user_id and so on
 */

public class LoginServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // retrieve the ID of this session
        String sessionId = req.getSession(true).getId();

        LOGGER.info("Session id in the Login Page servlet is " + sessionId);

        // determine whether the user is already authenticated
        Object clientInfoObj = req.getSession().getAttribute(LoginServerConstants.CLIENT_INFO_KEY);
        if(clientInfoObj != null) {
            // already authed, no need to log in
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>You have already been authenticated</h1>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            return;
        }

        LOGGER.info("clientInfoObj is null");;

        // retrieve the config info from the context
        HashMap<String, String> config = (HashMap<String, String>) req.getServletContext().getAttribute("slackAuthentication");

        // retrieve the code provided by Slack
        String code = req.getParameter(LoginServerConstants.CODE_KEY);

        LOGGER.info("Code received from slack is " + code);

        // generate the url to use to exchange the code for a token:
        // After the user successfully grants your app permission to access their Slack profile,
        // they'll be redirected back to your service along with the typical code that signifies
        // a temporary access code. Exchange that code for a real access token using the
        // /openid.connect.token method.
        String url = LoginUtilities.generateSlackTokenURL(config.get("clientId"), config.get("clientSecret"), code, config.get("redirectUri"));

        // Make the request to the token API
        String responseString = HTTPFetcher.doGet(url, null);
        LOGGER.info("HTTPFetcher has gotten response ");
        Map<String, Object> response = LoginUtilities.jsonStrToMap(responseString);
        LOGGER.info("Response converted to map the keys are " + response.keySet());

        /**
         * The keys of the HashMap are
         * First_Name
         * Last_Name
         * Email
         */
        HashMap<String,String> clientInfo = LoginUtilities.verifyTokenResponse(response, sessionId);

        if(clientInfo == null) {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>login unsuccessful, try again</h1>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        } else {
            try {
                SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
                String firstName = clientInfo.get("First_Name");
                String lastName = clientInfo.get("Last_Name");
                String email = clientInfo.get("Email");

                if (!db.userExists(firstName, lastName, email)) {
                    //the cols in the table are the firstName, lastName, preferredName, email
                    //preferred name is initially the same as the firstName
                    db.insertUser(firstName, lastName, firstName, email);
                }


                req.getSession().setAttribute(LoginServerConstants.CLIENT_INFO_KEY, clientInfo);
                resp.setStatus(HttpStatus.OK_200);
                resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
                resp.getWriter().println("<h1>Hello, " + clientInfo.get("First_Name") + "</h1>");
                resp.getWriter().println("<p><a href=\"/logout\">Signout</a>");
                resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}
