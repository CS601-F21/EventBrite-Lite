/**
 * Author : Shubham Pareek
 * Purpose : API Call to this responds back with a json body with information about the user
 */
package Backend.Servlets;

import Backend.JWT.TokenUtils;
import Backend.Servlets.RequestBodyObjects.User;
import Backend.Servlets.Utilities.ResponseUtils;
import DB.SQLQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class UserInformationServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(UserInformationServlet.class);

    /**
     * This API will be called when the client wants to get information about a particular user
     * Sample URI
     *      /userinfo?sessionid={sessionid}
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            User user = ResponseUtils.getUser(req);
            resp.setHeader("Access-Control-Allow-Origin", "*");
            if (!ResponseUtils.userAuthenticated(user)){
                ResponseUtils.send200OkResponse(false, "User not authenticated", resp);
                return;
            }
            user.setId(0);
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(user);
            LOGGER.info("Response is ===> " + json);
            ResponseUtils.send200JsonResponse(json, resp);
//            ResponseUtils.sendJsonResponse(result, resp);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
