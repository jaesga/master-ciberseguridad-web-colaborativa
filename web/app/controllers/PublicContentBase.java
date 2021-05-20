package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type) {
    	
    		if(type == null || type.isEmpty() || (!type.contains("teacher") && !type.contains("student")  )) {
    			
    			registerError();
    		}
    		
    		User u = new User(username, HashUtils.getMd5(password), type, -1);
            u.save();
            registerComplete();
    	
        
    }

    public static void registerComplete(){
        render();
    }
    
    public static void registerError() {
    	render();
    }
}