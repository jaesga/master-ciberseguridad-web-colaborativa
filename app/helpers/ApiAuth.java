package helpers;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.Base64.Encoder;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.User;
import models.Constants;
import play.mvc.Http;
import play.mvc.Scope.Session;
import play.Play;

public class ApiAuth {
    
    static String key = Play.configuration.get("tokenKey").toString();
    static String hmacAlg = Play.configuration.get("tokenAlg").toString();
    static Integer daystoExpire = Integer.parseInt( Play.configuration.get("tokenTime").toString() );
    
    private static String createSign(String algorithm, String data, String key) throws NoSuchAlgorithmException, InvalidKeyException {
        
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
        Mac mac = Mac.getInstance(algorithm);
        mac.init(secretKeySpec);
        Encoder e = Base64.getUrlEncoder().withoutPadding();
        
        return e.encodeToString( mac.doFinal( data.getBytes() ) );
    }
    
    
    private static String buildData() {  // Create a Base64encoded string from Header and Payload parts
        
        Session session = Session.current(); /// Get current session
        Encoder enc = Base64.getUrlEncoder().withoutPadding(); // Coder without Base64 padding '='
        Date iat = new Date();
        JsonObject header = new JsonObject(); // Building header
            header.addProperty("typ", "JWT");
            header.addProperty("alg", "HS256");
        JsonObject payload = new JsonObject(); // Building Payload
            payload.addProperty("username", session.get("username"));
            payload.addProperty("iat", iat.getTime() ); // Creation time. Used to verify if token is expired against current time.

        return enc.encodeToString( header.toString().getBytes() ) + "." + enc.encodeToString( payload.toString().getBytes() );
    }

    private static Boolean expiredToken( String payload ) { // Check if a valid token is expired
        
        try {
            String payloadDecoded = new String( Base64.getUrlDecoder().decode(payload) ); // Decode payload part to extract iatString
            JsonObject payloadObj = new JsonParser().parse( payloadDecoded ).getAsJsonObject();
            Long iat = Long.parseLong( payloadObj.get("iat").getAsString() );
            Long maxTime = iat + (daystoExpire * 24 * 60 * 60 * 1000); 
            Long now = (new Date()).getTime();
            
            return maxTime < now;
        } catch (Exception e) {
            
            return false;
        }
    }

    private static Boolean validToken( String[] splitToken ) { // Check if a token is right signed
        
        String originToken = String.join(".",splitToken);
        String headerAndPayload = splitToken[0]+"."+splitToken[1];

        try {
            String sign = createSign(hmacAlg, headerAndPayload, key); // Create a sign from parts received
            String finalToken = headerAndPayload+"."+sign;
            return originToken == finalToken;
        } catch (Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }   
    }

    private static String validateJWT( Http.Cookie cookie ) { // return an empty String if everything goes right
        
        try {
            String[] splitToken = cookie.value.split("\\."); // Extract Header and Payload                    
            if (validToken(splitToken)) 
                return "Invalid token"; 
            if (expiredToken(splitToken[1])) 
                return "Token expired"; 
        } catch (Exception e) {
            return "Error validating token : "+e.getMessage();
        }
        
        return "";
    }

    private static String generateJWT( String type ) { // generate pieces for final JWT
        
        String jwt = "";
        
        // Only tokens for allowed profile (teacher)
        User u = User.loadUser( Session.current().get("username") );
        if (u == null || !u.getType().equals( Constants.User.TEACHER ) )
            return "";
        
        String data = buildData(); // Create Header & Payload with current session data
        try {
            String sign = createSign(hmacAlg, data, key);
            jwt = data+"."+sign;
        } catch (Exception e) {
            System.err.println( e.getMessage() );
        }   
        return jwt;     
    }

    public static void createCookie( String type ) {
        
        Http.Response response = Http.Response.current(); // Access to Response flow
        String token = generateJWT( type );
        if (token != "") 
            response.setCookie("apiCookie", token, String.format("%sd", daystoExpire.toString()) ); // Rewrite Cookie if exists
        else
            response.removeCookie("apiCookie"); // Privacy - Remove posible existing cookie if any error or logged profile is not allowed
    }


    public static String checkCookie() {
        
        Http.Request request  = Http.Request.current(); // Access to Request flow
        Http.Cookie sessionCookie = request.cookies.get("apiCookie");
        return validateJWT(sessionCookie);
    }

}