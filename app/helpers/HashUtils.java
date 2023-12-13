package helpers;


import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKey;
import javax.crypto.spec.PBEKeySpec;

public class HashUtils {

    // Método para obtener el hash PBKDF2 con salt
    public static String getPBKDF2Hash(String password, String salt) {
        int iterations = 100000; // Número de iteraciones
        int keyLength = 256; // Longitud de la clave
        char[] passwordChars = password.toCharArray();
        byte[] saltBytes = Base64.getDecoder().decode(salt);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, iterations, keyLength);
            SecretKey key = skf.generateSecret(spec);
            byte[] hashBytes = key.getEncoded();
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para generar un salt aleatorio
    public static String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // La longitud del salt en bytes (puedes ajustar esto según tus necesidades)
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }

    // Método para verificar el hash PBKDF2 con salt
    public static boolean checkPBKDF2Hash(String password, String hashedPassword, String salt) {
        return hashedPassword.equals(getPBKDF2Hash(password, salt));
}
}
