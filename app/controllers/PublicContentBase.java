package controllers;


import helpers.HashUtils;
import models.User;
import play.mvc.Controller;

public class PublicContentBase extends Controller {

    public static void register(){
        render();
    }

	public static void processRegister(String username, String password, String passwordCheck, String type){
       	// Verifica si alguno de los parámetros es nulo o está vacío.
        if(username == null || password == null || passwordCheck == null || type == null ||
           username.isEmpty() || password.isEmpty() || passwordCheck.isEmpty() || type.isEmpty()) {
            flash.error("Por favor, introduce un valor válido en todos los campos.");
            register();
        } else {
            // Si todos los parámetros están presentes, registra el usuario.
                User u = new User(username, HashUtils.getMd5(password), type, -1);
                u.save();
                registerComplete();
            }
        }

    public static void registerComplete(){
        render();
    }
}
