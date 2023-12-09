package controllers;


import helpers.HashUtils;
import helpers.ValidateUtils;
import models.User;
import play.i18n.Messages;
import play.mvc.Controller;
import java.util.regex.Pattern;

public class Secure extends Controller {

    public static void login(){
        render();
    }

    public static void logout(){
        session.remove("password");
        login();
    }

    public static void authenticate(String username, String password){
        boolean validationUsername = ValidateUtils.validateCharacters(username);
        boolean validationPassword = ValidateUtils.validateCharacters(password);
        boolean validation = validationUsername && validationPassword;
        if(!validation){
            flash.put("error", Messages.get("Public.login.error.characters"));
            login();
        }
        User u = User.loadUser(username);
        System.out.println(ValidateUtils.validateCharacters(username));
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
