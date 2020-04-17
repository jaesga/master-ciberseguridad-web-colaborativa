package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
	    if(password.matches(".*[0-9]{1,}.*") && password.matches(".*[~!@#$%^&*()_-].*") && password.matches(".*[A-Z]{1,}.*") && password.matches(".*[a-z]{1,}.*") && password.length()>=8)
                    {
                        User u = new User(username, HashUtils.getMd5(password), type, -1);
       				    u.save();
        			    registerComplete();
                    }
                    else
                    {
                       	flash.put("error", Messages.get("Public.register.validation.password.complexity"));
				        register();
                    }
       }

    public static void registerComplete(){
        render();
    }
}
