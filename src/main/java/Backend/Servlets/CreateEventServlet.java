/**
 * Author : Shubham Pareek
 * Class Purpose : Call to this URI creates and stores a new event
 */
package Backend.Servlets;

import Backend.JWT.TokenUtils;
import Backend.Servlets.RequestBodyObjects.NewEventBody;
import Backend.Servlets.RequestBodyObjects.User;
import Backend.Servlets.Utilities.ResponseUtils;
import DB.SQLQuery;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.HashMap;

public class CreateEventServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(CreateEventServlet.class);

    /**
     * Since this uri will create a new event, we will require a bunch of information about the event and the
     * user who is actually creating the event.
     * So in this case it makes more sense if the POST request has a body with the required parameters
     *
     * The request body should have the following info :
     *      1) name
     *      2) location
     *      3) attending //attending will be a boolean
     *      4) capacity
     *      5) price
     *      6) date
     *
     * Sample URI call is
     *      /createevent/sessionid={sessionid}
     *      //the token is used to get the id of the user creating the event
     *      with the rest of the details in the POST Body
     *
     * https://stackoverflow.com/questions/1548782/retrieving-json-object-literal-from-httpservletrequest was used
     * to know how to process the request body of an HttpServletRequest
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HashMap<String, String> responseMap = new HashMap<>();
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            User user = ResponseUtils.getUser(req);
            if (!ResponseUtils.userAuthenticated(user)){
                ResponseUtils.send200OkResponse(false, "User not authenticated", resp);
                return;
            }
            req.getInputStream();
            int organizerId = db.getUserIdFromEmail(user.getEmail());
            Gson gson = new Gson();
            String requestStr = IOUtils.toString(req.getInputStream());
            NewEventBody newEventBody = gson.fromJson(requestStr, NewEventBody.class);
            newEventBody.setOrganizer(organizerId);
            boolean success = db.insertEvent(newEventBody);
            ResponseUtils.send200OkResponse(success, null, resp);
        } catch (IOException e) {
            LOGGER.info("IO Exception");
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                ResponseUtils.send200OkResponse(false, "SQL ERROR", resp);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
