package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
        if(!userExists(username)) {
            User u = new User(username, HashUtils.getMd5(password), type, -1);
            u.save();
            registerComplete();
        }else{
            renderText("Nombre de usuario ya registrado. Inserte otro nombre de usuario. Navegue hacia atrás (<-) e inténtelo de nuevo.");
        }
    }
    public static boolean userExists(String username){
        User u2 = User.loadUser(username);
        if(u2 == null) {
            return false;
        }else {
            return true;
        }
    }

    public static void registerComplete(){
        render();
    }
}

