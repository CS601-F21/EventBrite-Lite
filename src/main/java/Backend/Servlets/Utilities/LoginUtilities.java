/**
 * Author : Sami Rollins
 * Purpose : Utilities for Login
 */
package Backend.Servlets.Utilities;

import com.google.gson.Gson;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.StringReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * A utility class with several helper methods.
 */
public class LoginUtilities {

    private static final Logger LOGGER = LogManager.getLogger(LoginUtilities.class);

    // for parsing JSON
    private static final Gson gson = new Gson();

    /**
     * Hash the session ID to generate a nonce.
     * Uses Apache Commons Codec
     * See https://www.baeldung.com/sha-256-hashing-java
     * @param sessionId
     * @return
     */
    public static String generateNonce(String sessionId) {
        String sha256hex = DigestUtils.sha256Hex(sessionId);
        return sha256hex;
    }

    /**
     * Generates the URL to make the initial request to the authorize API.
     * @param clientId
     * @param state
     * @param nonce
     * @param redirectURI
     * @return
     */
    public static String generateSlackAuthorizeURL(String clientId, String state, String nonce, String redirectURI) {

        LOGGER.info("Generating authorize url, ");
        LOGGER.info("Client id received is " + clientId);
        LOGGER.info("State received is " + state);
        LOGGER.info("Nonce received is " + nonce);
        LOGGER.info("RedirectURI received is " + redirectURI);

        String url = String.format("https://%s/%s?%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s",
                LoginServerConstants.HOST,
                LoginServerConstants.AUTH_PATH,
                LoginServerConstants.RESPONSE_TYPE_KEY,
                LoginServerConstants.RESPONSE_TYPE_VALUE,
                LoginServerConstants.SCOPE_KEY,
                LoginServerConstants.SCOPE_VALUE,
                LoginServerConstants.CLIENT_ID_KEY,
                clientId,
                LoginServerConstants.STATE_KEY,
                state,
                LoginServerConstants.NONCE_KEY,
                nonce,
                LoginServerConstants.REDIRECT_URI_KEY,
                redirectURI
        );
        return url;
    }

    /**
     * Generates the URL to exchange the initial auth for a token.
     * @param clientId
     * @param clientSecret
     * @param code
     * @param redirectURI
     * @return
     */
    public static String generateSlackTokenURL(String clientId, String clientSecret, String code, String redirectURI) {

        String url = String.format("https://%s/%s?%s=%s&%s=%s&%s=%s&%s=%s",
                LoginServerConstants.HOST,
                LoginServerConstants.TOKEN_PATH,
                LoginServerConstants.CLIENT_ID_KEY,
                clientId,
                LoginServerConstants.CLIENT_SECRET_KEY,
                clientSecret,
                LoginServerConstants.CODE_KEY,
                code,
                LoginServerConstants.REDIRECT_URI_KEY,
                redirectURI
        );
        return url;
    }

    /**
     * Convert a JSON-formatted String to a Map.
     * @param jsonString
     * @return
     */
    public static Map<String, Object> jsonStrToMap(String jsonString) {
        Map<String, Object> map = gson.fromJson(new StringReader(jsonString), Map.class);
        return map;
    }

    /**
     * Verify the response from the token API.
     * If successful, returns a ClientInfo object with information about the authed client.
     * Returns null if not successful.
     * @param map
     * @param sessionId
     * @return
     */
    public static HashMap<String, String> verifyTokenResponse(Map<String, Object> map, String sessionId) {
        LOGGER.info("Payload received is");
        LOGGER.info(map);
        // verify ok: true
        if(!map.containsKey(LoginServerConstants.OK_KEY) || !(boolean)map.get(LoginServerConstants.OK_KEY)) {
            LOGGER.info("Verification : Response does not contain the 'ok' key or the 'ok' response object is false");
            return null;
        }

        // verify state is the users session cookie id
        if(!map.containsKey(LoginServerConstants.STATE_KEY) || !map.get(LoginServerConstants.STATE_KEY).equals(sessionId)) {
            LOGGER.info("Verification : Response does not contain the 'state' key or the 'state' response object is not equal to the session id");
            LOGGER.info("Session id is " + sessionId);
            LOGGER.info("State is " + map.get(LoginServerConstants.STATE_KEY));
            return null;
        }

        // retrieve and decode id_token
//        String idToken = URLDecoder.decode((String)map.get("id_token"), StandardCharsets.UTF_8);
        String idToken = (String)map.get("id_token");
        Map<String, Object> payloadMap = decodeIdTokenPayload(idToken);

        LOGGER.info("Payload map is");
        LOGGER.info(payloadMap);

        //verify nonce
        String expectedNonce = generateNonce(sessionId);
        String actualNonce = (String) payloadMap.get(LoginServerConstants.NONCE_KEY);
        if(!expectedNonce.equals(actualNonce)) {
            LOGGER.info("Expected Nonce is not equal to the actual Nonce");
            return null;
        }

        // extract name from response
        String username = (String) payloadMap.get(LoginServerConstants.NAME_KEY);

        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put("First_Name", (String) payloadMap.get("given_name"));
        userDetails.put("Last_Name", (String) payloadMap.get("family_name"));
        userDetails.put("Email", (String) payloadMap.get("email"));

        return userDetails;
    }

    /**
     *
     * From the Slack documentation:
     * id_token is a standard JSON Web Token (JWT). You can decode it with off-the-shelf libraries in any programming
     * language, and most packages that handle OpenID will handle JWT decoding.
     *
     * Method decodes the String id_token and returns a Map with the contents of the payload.
     *
     * @param idToken
     * @return
     */
    public static Map<String, Object> decodeIdTokenPayload(String idToken) {
        // Decoding process taken from:
        // https://www.baeldung.com/java-jwt-token-decode
        String[] chunks = idToken.split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();

        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        // convert the id_token payload to a map
        Map<String, Object> payloadMap = gson.fromJson(new StringReader(payload), Map.class);
        return payloadMap;
    }
}
