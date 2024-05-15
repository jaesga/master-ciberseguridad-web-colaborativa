package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
       // En el caso de que no este vacio y no sea nulo el tipo de usuario se guardara
        if(type != null && !type.isEmpty()){

            User u = new User(username, HashUtils.getMd5(password), type, -1);
            u.save();
            registerComplete();
        } else{
             // En el caso contrario mostrara un error
            flash.error("Debe seleccionar el tipo de usuario.");
            register();

        }
    }

    public static void registerComplete(){
        render();
    }
}
