package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
        User u = new User(username, HashUtils.getSHA(password), type, -1);
        if (u.save()) registerComplete();
        else registerNotCompleted();
    }

    public static void registerComplete(){
        render();
    }

    public static void registerNotCompleted(){
        render();
    }
}
