/**
 * Author : Shubham Pareek
 * Purpose : Class contains common functions we need to send out a response
 */
package Backend.Servlets.Utilities;

import Backend.Servlets.RequestBodyObjects.User;
import DB.SQLQuery;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class ResponseUtils {
    private static final Logger LOGGER = LogManager.getLogger(ResponseUtils.class);

    /**
     * Method to get the response json
     * Takes in a list of hashmaps and returns the json format in string format
     * @param allRows
     * @return
     * @throws IOException
     */
    public static String getResponse(ArrayList<HashMap<String, String>> allRows) throws IOException {
        //JackSon Object Mapper, takes in a list of map and writes it out
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(allRows);
        return json;
    }

    public static void sendJsonResponse (ResultSet resultSet, HttpServletResponse resp) throws SQLException, IOException {
        //getting list of all rows, each row is represented as a hashmap
        ArrayList<HashMap<String, String>> allRows = ResponseUtils.resultSetToJson(resultSet);
        resp.setHeader("Access-Control-Allow-Origin", "*");
        //converting the arraylist to a json string
        String response = ResponseUtils.getResponse(allRows);
        LOGGER.info("Response is");
        LOGGER.info(response);
        //sending the response
        ResponseUtils.send200JsonResponse(response, resp);
    }

    /**
     * https://stackoverflow.com/questions/7507121/efficient-way-to-handle-resultset-in-java to get to know about
     * Takes in a resultSet as the parameter and converts it to a List of HashMap then returns the list
     * ResultSetMetaData
     * @param resultSet
     * @return
     */
    public static ArrayList<HashMap<String, String>> resultSetToJson(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int numColumns = metaData.getColumnCount();
        ArrayList<HashMap<String, String>> allRows = new ArrayList<>();

        while (resultSet.next()){
            HashMap<String, String> singleRow = new HashMap<>();
            for (int i = 1; i <= numColumns; i++){
                String colName = metaData.getColumnName(i);
                String colValue = String.valueOf(resultSet.getObject(i));
                singleRow.put(colName, colValue);
            }
            allRows.add(singleRow);
        }

        return allRows;
    }

    public static void send200OkResponse (boolean success, String message, HttpServletResponse resp) throws IOException {
        HashMap<String, String> responseMap = new HashMap<>();
        resp.setHeader("Access-Control-Allow-Origin", "*");

        if (success) {
            /**
             * Response status 200, with ok set to true
             */
            responseMap.put("ok", "true");
        } else {
            /**
             * Response status 200, with ok set to false
             */
            responseMap.put("ok", "false");
            responseMap.put("message" , message);
        }

        //making this conversion since we already have a method which takes in an arraylist
        //of map and converts it into json string
        ArrayList<HashMap<String, String>> responseList = new ArrayList<>();
        responseList.add(responseMap);
        //getting the json string
        String response =  ResponseUtils.getResponse(responseList);
        LOGGER.info("Response sent is the following");
        LOGGER.info(response);
        //sending the response
        ResponseUtils.send200JsonResponse(response, resp);
    }

    /**
     * Method to send a 200 response with a JSON Body
     * Takes in the response string and the response object and sends out the response
     * @param response
     * @param resp
     * @throws IOException
     */
    public static void send200JsonResponse (String response, HttpServletResponse resp) throws IOException {
        //setting response status
        resp.setStatus(HttpStatus.OK_200);
        //setting response type
        resp.setContentType("application/json");
        resp.setHeader("Access-Control-Allow-Origin", "*");

        //getting print writer, will send response via this
        PrintWriter out = resp.getWriter();
        //writing response
        out.write(response);
        //flushing out the writer
        out.flush();
    }

    public static boolean userAuthenticated (User user){
        //checkinf firstName instead of id, because default value of int is 0, and we may have a user with id = 0;
        try {
            if (!user.getFirstName().isEmpty()){
                return true;
            } else {
                return false;
            }
        } catch (NullPointerException e){
            return false;
        }
    }

    public static User getUser (HttpServletRequest req) throws IOException, SQLException {
        String sessionId = req.getParameter("sessionid");
        LOGGER.info("Got session id as " + sessionId);
        HashMap<String, String> userInfo =  (HashMap<String, String>) req.getServletContext().getAttribute(sessionId);
        LOGGER.info("Got user info as ");
        LOGGER.info(userInfo);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(userInfo);
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        return user;
    }


}
