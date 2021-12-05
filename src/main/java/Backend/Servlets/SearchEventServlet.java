/**
 * Author : Shubham Pareek
 * Purpose : API Call to this responds event details matching a particular search
 */
package Backend.Servlets;

import Backend.Servlets.RequestBodyObjects.SearchBody;
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
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchEventServlet extends HttpServlet {
    private static final Logger LOGGER = LogManager.getLogger(SearchEventServlet.class);

    /**
     * API call to this sends a response matching the search parameters, the request body will be of the following type
     *      GET /search
     *          {
     *              containsExactWord : {title should have this exact word}
     *              priceGreaterThan : {price should be greater than this}
     *              priceLessThan : {price should be less than this}
     *              location : {location of the desired event}\
     *          }
     *
     *       All of the params in the body are allowed to be null, the user does not have to provide all the above details when
     *       performing a search
     * @param req
     * @param resp
     * @throws ServletException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            String requestStr = IOUtils.toString(req.getInputStream());
            Gson gson = new Gson();
            SearchBody body = gson.fromJson(requestStr, SearchBody.class);

            //if no search params were given
            if (body.getContainsExactWord() == null && body.getPriceGreaterThan() == 0
                && body.getPriceLessThan() == 0 && body.getLocation() == null){
                /**
                 * Search
                 */
                ResultSet resultSet = db.getAllEvents();
                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            //if user wants to search only by location
            if (body.getContainsExactWord() == null && body.getPriceGreaterThan() == 0
                    && body.getPriceLessThan() == 0 && body.getLocation() != null){
                /**
                 * Search
                 */
                ResultSet resultSet = db.searchByLocation(body.getLocation());
                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            //if user wants to search only by location
            if (body.getContainsExactWord() != null && body.getPriceGreaterThan() == 0
                    && body.getPriceLessThan() == 0 && body.getLocation() == null){
                /**
                 * Search
                 */
                ResultSet resultSet = db.searchByWord(body.getContainsExactWord());
                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            //if user has given both a search term and location
            if (body.getContainsExactWord() == null && body.getLocation() == null){
                int maxPossiblePrice = 1000000000;
                //if no price less than param, we just search for all event where price is less than
                if (body.getPriceLessThan() == 0){
                    body.setPriceLessThan(maxPossiblePrice);
                }

                //min price is 0 by default so we do not explicitly change that
                ResultSet resultSet = db.searchByPrice(body.getPriceLessThan(), body.getPriceGreaterThan());
                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            //if user has given both a search term and location
            if (body.getContainsExactWord() != null && body.getLocation() != null){
                int maxPossiblePrice = 1000000000;
                //if no price less than param, we just search for all event where price is less than
                if (body.getPriceLessThan() == 0){
                    body.setPriceLessThan(maxPossiblePrice);
                }

                //min price is 0 by default so we do not explicitly change that
                ResultSet resultSet = db.searchByWordLocationAndPrice(body.getContainsExactWord(), body.getLocation(), body.getPriceLessThan(), body.getPriceGreaterThan());
                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }




        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
