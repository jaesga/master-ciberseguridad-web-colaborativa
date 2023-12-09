package controllers;


import helpers.HashUtils;
import helpers.ValidateUtils;
import models.User;
import play.mvc.Controller;
import play.i18n.Messages;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
        boolean validationUsername = ValidateUtils.validateCharacters(username);
        boolean validationPassword = ValidateUtils.validateCharacters(password);
        boolean validationPasswordCheck = ValidateUtils.validateCharacters(passwordCheck);
        boolean validation = validationUsername && validationPassword && validationPasswordCheck;
        if(!validation){
            flash.put("error", Messages.get("Public.login.error.characters"));
            register();
        }
        User u = new User(username, HashUtils.getMd5(password), type, -1);
        u.save();
        registerComplete();
    }

    public static void registerComplete(){
        render();
    }
}
