package Backend.Servlets;

import DB.SQLQuery;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class UpdatePreferredNameServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(UpdatePreferredNameServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
    }
}
