package controllers;

import models.Constants;
import models.User;
import play.mvc.*;

import java.util.List;
import java.util.Objects;

public class Application extends Controller {

    private static void checkTeacher(){
        checkUser();

        User u = (User) renderArgs.get("user");
        if (!u.getType().equals(Constants.User.TEACHER)){
            Secure.login();
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


    public static void setMark(String studentName) {
        checkTeacher();
        User user = User.loadUser(session.get("username"));

        User student = User.loadUser(studentName);
        render(student, user);
    }

    public static void doSetMark(String studentName, Integer mark) {
        checkTeacher();

        User student = User.loadUser(studentName);
        if(student != null){
            student.setMark(mark);
            student.save();
        }
        index();
    }
}