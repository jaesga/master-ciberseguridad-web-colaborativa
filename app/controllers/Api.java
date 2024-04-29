package controllers;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import models.User;
import models.Constants;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.With;


@With(Secure.class)
public class Api extends Controller {


   public static void removeAllUsers() {
       if (isAdminUser()) {
           User.removeAll();
           renderJSON(new JsonObject());
       } else {
           forbidden();
       }
   }
   //Verificamos si es el usuario de director
   private static boolean isAdminUser() {
       String fileLocation = Constants.User.USERS_FOLDER + "/" + "director";
       try (BufferedReader br = new BufferedReader(new FileReader(fileLocation))) {


           StringBuilder content = new StringBuilder();
           String line;
           while ((line = br.readLine()) != null) {
               content.append(line);
           }


           JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
           String username = jsonObject.get("username").getAsString();
           String password = jsonObject.get("password").getAsString();


           String sessionUsername = session.get("username");
           String sessionPassword = session.get("password");
           return username.equals(sessionUsername) && password.equals(sessionPassword);


       } catch (IOException e) {
           //en caso de fallo al abrir el archivo de director
           e.printStackTrace();
           return false;
       }
   }
  
}
