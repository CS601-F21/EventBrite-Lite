package Backend.Servlets.Authentication;

import Backend.Servlets.Utilities.ResponseUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class GetSessionIDServlet extends HttpServlet {

    private static final Logger LOGGER = LogManager.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = req.getSession(true).getId();
        LOGGER.info("Session id in the Landing Page servlet is " + sessionId);
        // retrieve the config info from the context
        ObjectMapper mapper = new ObjectMapper();

        HashMap<String, String> response = new HashMap<>();
        response.put("sessionId", sessionId);
        String json = mapper.writeValueAsString(response);
        ResponseUtils.send200JsonResponse(json, resp);
    }
}
