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
        if(u.save())registerComplete();
    	else{
		flash.put("error", Messages.get("Public.register.validation.existingAccount"));
		register();
	}

    }

    public static void registerComplete(){
        render();
    }
}
