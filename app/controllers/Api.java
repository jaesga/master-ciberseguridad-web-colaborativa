package controllers;

import com.google.gson.JsonObject;

import helpers.ApiAuth;
import models.User;
import play.mvc.Controller;

public class Api extends Controller {

    public static void removeAllUsers(){
        JsonObject o = new JsonObject();
        String result = ApiAuth.checkCookie();
        if (result != "" || result == null) {
            o.addProperty("message", "Not authorized : " + result);
        }
        else {
            User.removeAll();
            o.addProperty("message", "Users removed succesfully");
        }

        renderJSON( o );
    }
}
