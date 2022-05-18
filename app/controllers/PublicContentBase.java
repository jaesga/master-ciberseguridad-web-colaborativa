package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
        User safeUser = User.loadUser(username);
        if (safeUser != null){
            registerError();
            return;
        }
        User newUser = new User(username, HashUtils.getMd5(password), type, -1);
        newUser.save();
        registerComplete();
    }

    public static void registerComplete(){
        render();
    }

    public static void registerError(){
        render();
    }
}
