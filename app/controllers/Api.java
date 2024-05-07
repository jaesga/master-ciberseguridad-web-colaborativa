package controllers;

import com.google.gson.JsonObject;
import play.mvc.Controller;
import models.User;

public class Api extends Controller {

    public static void removeAllUsers() {
        User.removeAll();
        renderJSON(new JsonObject());
    }
}
