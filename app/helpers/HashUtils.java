package helpers;


import java.math.BigInteger;
import java.security.MessageDigest;

public class HashUtils {

    public static String getMd5(String s){
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(s.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            while(hashtext.length() < 32){
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate MD5 hash", e);
        }
    }
}