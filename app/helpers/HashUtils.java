package helpers;


import java.math.BigInteger;
import java.security.MessageDigest;

public class HashUtils {

    public static String getSHA_256(String s){
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            m.reset();
            m.update(s.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            while(hashtext.length() < 32){
                hashtext = "0" + hashtext;
            }
        } catch (Exception e) {}
        return s;
    }
}
