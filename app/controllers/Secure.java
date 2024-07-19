package controllers;


import helpers.HashUtils;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;

public class Secure extends Controller {

    public static void login(){
        removeUserSession();
        render();
    }

    public static void logout(){
        removeUserSession();
        login();
    }

    private static void removeUserSession() {
        session.remove("password");
        session.remove("username");
    }

    public static boolean isUserLoggedIn() {
        return (session.contains("username") && session.contains("password"));
    }

    public static void authenticate(String username, String password){
        User u = User.loadUser(username);
        if (u != null && u.getPassword().equals(HashUtils.getMd5(password))){
            session.put("username", username);
            session.put("password", password);
            Application.index();
        }else{
            flash.put("error", Messages.get("Public.login.error.credentials"));
            login();
        }

    }
}
