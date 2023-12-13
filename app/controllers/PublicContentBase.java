package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    //Se modifica el método de registro para adecuarse al algoritmo PBKDF2
    public static void processRegister(String username, String password, String passwordCheck, String type) {

            // Genera un salt aleatorio
            String salt = HashUtils.generateRandomSalt();
            // Crea el usuario con el hash de la contraseña y el salt aleatorio
            String hashedPassword = HashUtils.getPBKDF2Hash(password, salt);
            User u = new User(username, hashedPassword, salt, type, -1);
        u.save();
   
        registerComplete();
    }

    public static void registerComplete(){
        render();
    }
}
