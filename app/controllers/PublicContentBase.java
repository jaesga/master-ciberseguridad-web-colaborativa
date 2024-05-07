package controllers;

import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register() {
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type) {
        // Verificar unicidad de la entidad
        User existingUser = User.findByUsername(username);

        User existingUser = User.findByUsername(username);
        if (existingUser != null) {
            flash().error("El nombre de usuario ya est� en uso. Por favor, elija otro.");
            return redirect(routes.PublicContentBase.register()); // Volver al formulario de registro
        }

        // Verificar si las contrase�as coinciden
        if (!password.equals(passwordCheck)) {
            flash().error("Las contrase�as no coinciden. Por favor, int�ntelo de nuevo.");
            return redirect(routes.PublicContentBase.register()); // Volver al formulario de registro
        }

        // Verificar si la contrase�a cumple con las pol�ticas de seguridad
        if (!isValidPassword(password)) {
            flash().error(
                    "La contrase�a no cumple con los requisitos de seguridad. Por favor, elija una contrase�a m�s segura.");
            return redirect(routes.PublicContentBase.register()); // Volver al formulario de registro
        }

        // Crear usuario y guardar en la base de datos
        String hashedPassword = HashUtils.getBcrypt(password); // Utilizar bcrypt para cifrar la contrase�a
        User u = new User(username, hashedPassword, type, -1);
        u.save();
        return redirect(routes.PublicContentBase.registerComplete());

        User u = new User(username, HashUtils.getMd5(password), type, -1);
        u.save();
        registerComplete();
    }
    
    private static boolean isValidPassword(String password) {
        // Aplicar pol�ticas de contrase�as seguras
        // Por ejemplo, verificar longitud m�nima, uso de caracteres mixtos, etc.
        // Devuelve true si la contrase�a es v�lida, false de lo contrario
        return password.length() >= 8 // Longitud m�nima de 8 caracteres
                && password.matches(".*\\d.*") // Contiene al menos un d�gito
                && password.matches(".*[a-z].*") // Contiene al menos una letra min�scula
                && password.matches(".*[A-Z].*") // Contiene al menos una letra may�scula
                && password.matches(".*[!@#$%^&*()-+=\\[\\]{};:',.<>?/~_].*"); // Contiene al menos un car�cter especial
    }

    public static void registerComplete() {
        render();
    }

}
