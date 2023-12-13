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
        //Se elimina la gestión de las sesiones con el valor "password" en el navegador y se implementa que haga un borrado completo de todas las variables
		session.clear();
        login();
    }

    public static void authenticate(String username, String password){
        User u = User.loadUser(username);
		
        if (u != null && HashUtils.checkPBKDF2Hash(password, u.getPassword(), u.getSalt())){

            session.put("username", username);
            //Se elimina inclusión de la contraseña en la sesión del navegador
            Application.index();
        }else{

            flash.put("error", Messages.get("Public.login.error.credentials"));
            login();
        }

    }
}
