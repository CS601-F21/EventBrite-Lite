package Backend.JWT;

import Backend.Servlets.RequestBodyObjects.User;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.codehaus.jackson.map.ObjectMapper;

import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.Key;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
import java.time.Instant;

//https://www.viralpatel.net/java-create-validate-jwt-token/ to learn how to generate and validate tokens
public class TokenUtils {
    private static String secret = "brehjbfvhjerncgmiujt4nrsgiu6742t3764t2768126437x4bgt7638xq34";

    public static String generateToken (int id, String fName, String lName, String preferredName, String email){

        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());

        String token = Jwts.builder()
                .claim("id", id)
                .claim("firstName", fName)
                .claim("lastName", lName)
                .claim("preferredName", preferredName)
                .claim("email", email)
                .setSubject("User Info")
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .signWith(hmacKey)
//                .setExpiration(Date.from(Instant.now().plus(5l, ChronoUnit.HOURS)))
                .compact();
        return token;
    }

    public static Jws<Claims> decodeToken (String token){
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS256.getJcaName());
        Jws<Claims> jwt = Jwts.parserBuilder()
                .setSigningKey(hmacKey)
                .build()
                .parseClaimsJws(token);
//        System.out.println(jwt);
        return jwt;
    }

    public static HashMap<String, String> jwsToMap(Jws<Claims> jws) {
        HashMap<String, String> decoded = new HashMap<>();
        for (String key : jws.getBody().keySet()){
            decoded.put(key, String.valueOf(jws.getBody().get(key)));
        }
        return decoded;
    }

    public static User tokenToUser (String token) throws IOException {
        Jws<Claims> jws = decodeToken(token);
        HashMap<String, String> map = jwsToMap(jws);
        ObjectMapper objectMapper = new ObjectMapper();
        String jwsToString = objectMapper.writeValueAsString(map);
        Gson gson = new Gson();
        User user = gson.fromJson(jwsToString, User.class);
        return user;
    }
}
