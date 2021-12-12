/**
 * Author : Sami Rollins
 * Modified By : Shubham Pareek
 * Purpose : Authenticate the user
 */

package Backend.Servlets.Authentication;

import Backend.Servlets.Utilities.*;
import DB.SQLQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * Handles the login for the user. Once a user has successfully been logged in, we check the database for whether the user
 * exists or not, if not, we create an entry in the database for the said user.
 * We then send a json body to the front-end with the user-info and the session id.
 *
 * A valid uri is :
 *      /login?code={code from slack}
 */

public class LoginServlet extends HttpServlet {

    //logger
    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        // getting the code the user got form slack
        String code  = req.getParameter("code");
        LOGGER.info("Code received from ui is " + code);

        // retrieving the config info from the context
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


        //if the authentication is unsuccessful, we return the message as a json to the user
        if(clientInfo == null) {
            ResponseUtils.send200OkResponse(false, "Authentication unsuccessful", resp);
            return;
        } else {
            try {
                /**
                 * Else we first check for the user in the db, if they do not exist, then we create an entry for them,
                 */
                SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
                String firstName = (String)clientInfo.get("given_name");
                String lastName = (String)clientInfo.get("family_name");
                String email = (String)clientInfo.get("email");

                if (!db.userExists(firstName, lastName, email)) {
                    //the cols in the table are the firstName, lastName, preferredName, email
                    //preferred name is initially the same as the firstName
                    db.insertUser(firstName, lastName, firstName, email);
                }

                //regardless we get the user id from the database, which we then use to get the rest of the user information
                int userId = db.getUserIdFromEmail(email);

                //getting the user info
                ResultSet resultSet = db.getUserInfo(userId);
                ArrayList<HashMap<String, String>> jsonList = ResponseUtils.resultSetToJson(resultSet);
                //the user info we get from the previous line is going to be the first entry of the arraylist we received
                HashMap<String, String> userInfo = jsonList.get(0);
                //we get the session id and put it into the user info map, to be sent to the user
                String sessionId = req.getSession(true).getId();
                userInfo.put("sessionid", sessionId);
                //we create a servlet context mapping the session id with the userinfo, letting us authenticate the user with the session id
                req.getServletContext().setAttribute(sessionId, userInfo);
                //this header is for cors
                resp.setHeader("Access-Control-Allow-Origin", "*");
                //since we have to send a json request, we first need to convert the map to a json string, object mapper lets us do this
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(userInfo);
                LOGGER.info("Object mapped is ");
                LOGGER.info(json);
                //sending the response
                ResponseUtils.send200JsonResponse(json, resp);
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
    }
}