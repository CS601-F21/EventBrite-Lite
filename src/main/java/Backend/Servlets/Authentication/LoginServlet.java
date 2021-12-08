/**
 * Author : Sami Rollins
 * Modified By : Shubham Pareek
 * Purpose : Handles the Login
 */

package Backend.Servlets.Authentication;

import Backend.JWT.TokenUtils;
import Backend.Servlets.RequestBodyObjects.User;
import Backend.Servlets.Utilities.*;
import DB.SQLQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.eclipse.jetty.servlet.ServletContextHandler;


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
        String code  = req.getParameter("code");
        LOGGER.info("Code received from ui is " + code);

//        LOGGER.info("Session id in the Login Page servlet is " + sessionId);

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
        LOGGER.info("Response is ");
        LOGGER.info(response);
        Map<String, Object> clientInfo = LoginUtilities.decodeIdTokenPayload((String)response.get("id_token"));

        LOGGER.info("Response converted to map the keys are " + clientInfo.keySet());
//        for (String keys : clientInfo.keySet()){
//            LOGGER.info(keys);
//            LOGGER.info(clientInfo.get(keys));
//            LOGGER.info("---------------");
//        }


        if(clientInfo == null) {
            resp.setStatus(HttpStatus.OK_200);
            resp.getWriter().println(LoginServerConstants.PAGE_HEADER);
            resp.getWriter().println("<h1>login unsuccessful, try again</h1>");
            resp.getWriter().println(LoginServerConstants.PAGE_FOOTER);
        } else {
            try {
                SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
                String firstName = (String)clientInfo.get("given_name");
                String lastName = (String)clientInfo.get("family_name");
                String email = (String)clientInfo.get("email");

                if (!db.userExists(firstName, lastName, email)) {
                    //the cols in the table are the firstName, lastName, preferredName, email
                    //preferred name is initially the same as the firstName
                    db.insertUser(firstName, lastName, firstName, email);
                }

                int userId = db.getUserIdFromEmail(email);

                //getting the user info
                ResultSet resultSet = db.getUserInfo(userId);
                ArrayList<HashMap<String, String>> jsonList = ResponseUtils.resultSetToJson(resultSet);
                HashMap<String, String> userInfo = jsonList.get(0);
//                String JWTToken = TokenUtils.generateToken(userId, firstName, lastName, userInfo.get("Preferred_Name"), email);
//                //inserting the JWT token into the body as well, this will then be set as the browser cookie in the front-end server
                String sessionId = req.getSession(true).getId();
//                LOGGER.info("Setting session attribute " + sessionId);
                userInfo.put("sessionid", sessionId);
                req.getServletContext().setAttribute(sessionId, userInfo);

//                resp.setHeader("Set-Cookie", "jwt=" + JWTToken + "; Secure; HttpOnly; SameSite=None; Path=/login; Max-Age=99999999;");
//                LOGGER.info("Generated token " + JWTToken);
//                LOGGER.info("Reverse is  " + TokenUtils.decodeToken(JWTToken));
                resp.setHeader("Access-Control-Allow-Origin", "*");

                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(userInfo);
                LOGGER.info("Object mapped is ");
                LOGGER.info(json);
                ResponseUtils.send200JsonResponse(json, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}