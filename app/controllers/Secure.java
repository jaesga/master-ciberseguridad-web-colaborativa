package controllers;


import helpers.HashUtils;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;

public class Secure extends Controller {

    public static void login(){
        render();
    }

    public static void logout(){
        //Se incluye una eliminación generar de las variables de sesión
		session.clear();
        login();
    }

    public static void authenticate(String username, String password){
        User u = User.loadUser(username);
		//Se cambia el método de comprobación por hash al nuevo PBKDF2
        if (u != null && HashUtils.checkPBKDF2Hash(password, u.getPassword(), u.getSalt())){

            session.put("username", username);
            //Se elimina la inclusión de la contraseña dentro de las variables de sesión
            Application.index();
        }else{

            flash.put("error", Messages.get("Public.login.error.credentials"));
            login();
        }

    }
}
