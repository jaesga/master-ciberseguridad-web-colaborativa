package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
        User u = new User(username, HashUtils.getMd5(password), type, -1);
        boolean userExist = u.save();
        if(!userExist){
            registerFailed();
        }else {
            registerComplete();
        }
    }
    public static void registerFailed(){
        render();
    }

    public static void registerComplete(){
        render();
    }
}
