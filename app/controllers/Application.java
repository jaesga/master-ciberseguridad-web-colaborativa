package controllers;

import models.Constants;
import models.User;
import play.mvc.*;

import java.util.List;

public class Application extends Controller {

    private static void checkTeacher(){
        checkUser();

        User u = (User) renderArgs.get("user");
        if (!u.getType().equals(Constants.User.TEACHER)){
            return;
        }
    }

    private static void checkUser(){
        if (session.contains("username")){
            User u = User.loadUser(session.get("username"));
            if (u != null){
                renderArgs.put("user", u);
                return;
            }
        }
        Secure.login();
    }

    public static void index() {
        checkUser();

        User u = (User) renderArgs.get("user");

        if (u.getType().equals(Constants.User.TEACHER)){
            List<User> students = User.loadStudents();
            render("Application/teacher.html", u, students);
        }else{
            render("Application/student.html", u);
        }
    }


    public static void removeStudent(String student) {
        checkTeacher();

        User.remove(student);
        index();
    }


    public static void setMark(String student) {
        User u = User.loadUser(student);
        render(u);
    }

 /* public static void doSetMark(String student, Integer mark) {
        User u = User.loadUser(student);
        u.setMark(mark);
        u.save();
        index();
    }*/


    public static void doSetMark(String student, String mark) {
        checkTeacher();

        try {
            int parsedMark = Integer.parseInt(mark);
            
            if (parsedMark >= 0 && parsedMark <= 10) {
                User u = User.loadUser(student);
                if (u != null) {
                    u.setMark(parsedMark);
                    u.save();
                    index();
                } else {
                    flash("error", "Usuario no encontrado");
                    setMark(student); // Redirigir nuevamente a la vista setMark.html
                }
            } else {
                flash("error", "La nota debe estar entre 0 y 10");
                setMark(student); // Redirigir nuevamente a la vista setMark.html
            }
        } catch (NumberFormatException e) {
            flash("error", "La nota debe ser un número válido");
            setMark(student); // Redirigir nuevamente a la vista setMark.html
        }
    }

}