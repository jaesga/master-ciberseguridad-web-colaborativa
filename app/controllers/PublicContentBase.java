package controllers;


import helpers.HashUtils;
import models.Constants;
import models.User;
import play.mvc.Controller;
import play.data.validation.Validation;
import java.util.regex.Matcher; 
import java.util.regex.Pattern; 

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

    public static void processRegister(String username, String password, String passwordCheck, String type){
        // OLIVERCG: The script checks that the fields are not empty and that both passwords are equal, but we must check it
        validation.required(username);
        validation.required(password);
        validation.required(passwordCheck);
        validation.required(type);

        // OLIVERCG: Check if the username has the correct format of an email as suggested as other classmate
        // OLIVERCG: Let's use the regex proposal by OWASP
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern emailPattern = Pattern.compile(emailRegex); 
        if (!emailPattern.matcher(username).matches()){
            validation.addError("username", "The username " + username + " is not a valid email address.");
            register();
            // OLIVERCG: Called return to be sure the execution stops
            return;
        }

        // OLIVERCG: First check if the user exists
        if (User.loadUser(username) != null){
            validation.addError("username", "The username " + username + " already exists.");
            register();
            // OLIVERCG: Called return to be sure the execution stops
            return;
        }

        if (password != passwordCheck){
            validation.addError("password", "The passwords are not equal.");
            register();
            // OLIVERCG: Called return to be sure the execution stops
            return;
        }

        // OLIVERCG: Check if the password meets the minimum requirements

        password = password.trim();
        int len = password.length();
        if (len < Constants.User.MIN_PASSWORD_LENGTH || len > Constants.User.MAX_PASSWORD_LENGTH) {
            validation.addError("password","Password wrong size, it must have at least 8 characters and less than 20.");
            return;
        }

        char[] aC = password.toCharArray();
        boolean containUpper = false;
        boolean containLower = false;
        boolean containDigit = false;
        boolean containSpecialChar = false;
        for(char c : aC) {
            if (containUpper && containLower && containDigit && containSpecialChar){
                break;
            }
            if (Character.isUpperCase(c)) {
                containUpper = true;
            } else
            if (Character.isLowerCase(c)) {
                containLower = true;
            } else
            if (Character.isDigit(c)) {
                containDigit = true;
            } else
            if (Constants.User.SPECIAL_CHARACTERS.indexOf(String.valueOf(c)) >= 0) {
                containSpecialChar = true;
            } else {
                validation.addError("password","The password contains an invalid character: " + c + ".");
                return;
            }
        }
        if (!containUpper && !containLower && !containDigit && !containSpecialChar){
            validation.addError("password","Password doesn't meet the minimum requirements: between 8 and 20 characters; 1 uppercase letter, 1 lowercase letter, 1 digit and 1 special character (" + Constants.User.SPECIAL_CHARACTERS + ").");
            return;
        }

        // OLIVERCG: Check the user type, just in case it's not valid
        if (type == null || type.isEmpty()){
            validation.addError("type", "You must select one of the user types.");
            register();
            // OLIVERCG: Called return to be sure the execution stops
            return;
        }

        // OLIVERCG: Check if the type is not equal to any of the user types
        if (!type.equals(Constants.User.TEACHER) && !type.equals(Constants.User.STUDENT)){
            validation.addError("type", "Invalid user type.");
            register();
            // OLIVERCG: Called return to be sure the execution stops
            return;
        }

        User u = new User(username, HashUtils.getMd5(password), type, -1);
        u.save();
        registerComplete();
    }

    public static void registerComplete(){
        render();
    }
}
