package helpers;


import java.math.BigInteger;
import java.security.MessageDigest;

public class HashUtils {

    public static String getSHA(String s){
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256"); //aqui
            m.reset();
            m.update(s.getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            while(hashtext.length() < 32){
                hashtext = "0" + hashtext;
            }
        //} catch (Exception e) {}
        //return s;
            return hashtext;
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate SHA hash", e);
        }

    }
}
