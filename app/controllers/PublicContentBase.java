package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static final int MIN_PASSWORD_LENGTH = 10;

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
        if (isPasswordValid(password)){
            User u = new User(username, HashUtils.getMd5(password), type, -1);
            u.save();
            registerComplete();
        } else {
            flash.error("Password should be at least 10 chars.");
            register();
        }
    }

    private static boolean isPasswordValid(String password){
        return password.length() >= MIN_PASSWORD_LENGTH;
    }
    
    public static void registerComplete(){
        render();
    }
}
