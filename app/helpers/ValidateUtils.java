package helpers;


import java.util.regex.Pattern;

public class ValidateUtils {
    //Función valida si tiene carácteres especiales
    public static boolean validateCharacters(String str) {
        Pattern regexp = Pattern.compile("^[a-zA-Z0-9\\s@.]+$");
        return regexp.matcher(str).matches();
    }
}