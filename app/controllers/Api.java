package controllers;

import com.google.gson.JsonObject;
import models.User;
import play.mvc.Controller;
import models.Constants;
import models.User;

public class Api extends Controller {

    public void removeAllUsers(){
        if(accesoPermitido()){
            User.removeAll();
        } else {
            render("errors/403.html");
        }
    }
    public boolean accesoPermitido (){
        if (session.contains("username")){
            User a = User.loadUser(session.get("username"));
            if (a != null){
                renderArgs.put("user", a);
                User u = (User) renderArgs.get("user");
                if (u.getType().equals(Constants.User.TEACHER)){
                    return true;
                }
            }
        }
        return false;
    }
}
