package controllers;


import helpers.HashUtils;
import models.User;
import java.time.*;
import play.i18n.Messages;
import play.mvc.Controller;

public class Secure extends Controller {

    private static final int MAX_LOGIN_ATTEMPTS = 3;

    public static void login(){
        render();
    }

    public static void logout(){
        session.remove("password");
        login();
    }

    public static void authenticate(String username, String password){
        User u = User.loadUser(username);

        if (u == null) {
            flash.put("error", Messages.get("Public.login.error.credentials"));
            login();
        }
        
        if (u.getLocked()) {
            flash.put("error", Messages.get("Public.login.error.locked"));
            login();
        }

        if (u != null && u.getPassword().equals(HashUtils.getMd5(password))){
            session.put("username", username);
            session.put("password", password);

            u.setLocked(false);
            u.setAttempts(0);
            u.save();

            Application.index();
        }else{
            incrementLoginAttempts(u);
            if (isMaxLoginAttemptsExceeded(u)) {
                lockAccount(u);
                flash.put("error", Messages.get("Public.login.error.attempts"));
                login();
            } else {
                flash.put("error", Messages.get("Public.login.error.credentials"));
                login();
            }
        }
    }

    private static void incrementLoginAttempts(User u) {
        int currentAttempts = u.getAttempts();
        int newAttempts = currentAttempts + 1;

        u.setAttempts(newAttempts);
        u.save();
    }

    private static boolean isMaxLoginAttemptsExceeded(User u) {
        return u.getAttempts() >= MAX_LOGIN_ATTEMPTS;
    }

    private static void lockAccount(User u){
        u.setLocked(true);
        u.save();
    }
}
