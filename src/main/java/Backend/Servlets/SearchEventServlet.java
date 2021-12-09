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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException {
        try {
            SQLQuery db = (SQLQuery) req.getSession().getServletContext().getAttribute("db");
            String requestStr = IOUtils.toString(req.getInputStream());
            LOGGER.info("Received following request");
            LOGGER.info(requestStr);
            Gson gson = new Gson();
            SearchBody body = gson.fromJson(requestStr, SearchBody.class);

            ResultSet resultSet; //we will use this to send out the output
            resp.setHeader("Access-Control-Allow-Origin", "*");

            //if no search params were given, we return all events (000)
            if (body.getContainsExactWord().isEmpty() && body.getPriceLessThan() == 0 && body.getLocation().isEmpty()){
                /**
                 * Search
                 */
                LOGGER.info("No search params given");
                resultSet = db.getAllEvents();
                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            //if user wants to search only by location (001)
            if (body.getContainsExactWord().isEmpty() && !body.getLocation().isEmpty()){
                /**
                 * Search
                 */
                if (body.getPriceLessThan() == 0){
                    LOGGER.info("Location is " + body.getLocation() + " and exact term search and price are empty");
                    resultSet = db.searchByLocation(body.getLocation());
                } else  { //(101)
                    LOGGER.info("Location is " + body.getLocation() + " and exact term search is empty and price is" + body.getPriceLessThan());
                    resultSet = db.searchByPriceAndLocation(body.getLocation(), body.getPriceLessThan());
                }

                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            //if user wants to search only by word (010)
            if (!body.getContainsExactWord().isEmpty() && body.getLocation().isEmpty()){
                /**
                 * Search
                 */
                if (body.getPriceLessThan() == 0){
                    LOGGER.info("Exact term is " + body.getContainsExactWord() + " and location and price are empty");
                    resultSet = db.searchByWord(body.getContainsExactWord());
                } else  { //(110)
                    LOGGER.info("Exact term is " + body.getContainsExactWord() + " and location and price is " +body.getPriceLessThan());
                    resultSet = db.searchByPriceAndWord(body.getContainsExactWord(), body.getPriceLessThan());
                }

                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            //if user wants to search by price (100)
            if (body.getContainsExactWord().isEmpty() && body.getLocation().isEmpty() && body.getPriceLessThan() > 0){
                //min price is 0 by default so we do not explicitly change that
                LOGGER.info("Only price = " + body.getPriceLessThan() + " is present");
                resultSet = db.searchByPrice(body.getPriceLessThan());
                ResponseUtils.sendJsonResponse(resultSet, resp);
                return;
            }

            LOGGER.info("Received following body");
            LOGGER.info(body.toString());

            //if none of the if conditions were satisfied till now (111) then the user has given all the word and the
            //location, we check whether they have given the price as well, if yes then fine, else we set the price to something
            //very high
            //this way we do not explicitly check for whether the user has given the price or not
            if (body.getPriceLessThan() == 0){
                body.setPriceLessThan(1000000000);
            }
            resultSet = db.searchByPriceLocationWord(body.getPriceLessThan(), body.getLocation(), body.getContainsExactWord());
            ResponseUtils.sendJsonResponse(resultSet, resp);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
