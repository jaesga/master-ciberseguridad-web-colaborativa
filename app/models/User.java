package models;

import com.google.gson.JsonObject;
import helpers.FileUtils;
import play.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String username;
    private String password;
    private String type;
    private int mark;

    public static final Finder<Long, User> find = new Finder<>(User.class);

    public static User findByUsername(String username) {
        return find.query().where().eq("username", username).findOne();
    }

    public User(JsonObject json) {
        this.username = json.has(Constants.User.FIELD_USERNAME) ? json.get(Constants.User.FIELD_USERNAME).getAsString()
                : "";
        this.password = json.has(Constants.User.FIELD_PASSWORD) ? json.get(Constants.User.FIELD_PASSWORD).getAsString()
                : "";
        this.mark = json.has(Constants.User.FIELD_MARK) ? json.get(Constants.User.FIELD_MARK).getAsInt() : 0;
        this.type = json.has(Constants.User.FIELD_TYPE) ? json.get(Constants.User.FIELD_TYPE).getAsString() : "teacher";
    }

    public User(String username, String password, String type, Integer mark) {
        this.username = username;
        this.password = password;
        this.mark = mark;
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public File getFile() {
        return new File(Constants.User.USERS_FOLDER + "/" + username);
    }

    public void save() {
        JsonObject json = new JsonObject();
        json.addProperty(Constants.User.FIELD_USERNAME, username);
        json.addProperty(Constants.User.FIELD_PASSWORD, password);
        json.addProperty(Constants.User.FIELD_TYPE, type);
        json.addProperty(Constants.User.FIELD_MARK, mark);

        File file = getFile();
        if (file.exists()) {
            file.delete();
        }

        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(json.toString().getBytes());
        } catch (IOException e) {
            Logger.error("Error saving user: " + username);
            Logger.error(e.getMessage());
        }
    }

    public static User loadUser(String username) {
        File f = new File(Constants.User.USERS_FOLDER + "/" + username);
        if (f.exists()) {
            return new User(FileUtils.getJsonFromFile(f));
        }
        return null;
    }

    public static void removeAll() {
        File f = new File(Constants.User.USERS_FOLDER + "/");
        File[] users = f.listFiles();
        for (File u : users) {
            u.delete();
        }
    }

    public static boolean remove(String username) {
        File f = new File(Constants.User.USERS_FOLDER + "/" + username);
        if (f.exists()) {
            return f.delete();
        }
        return false;
    }

    public static List<User> loadStudents() {
        List<User> rv = new ArrayList<User>();

        File f = new File(Constants.User.USERS_FOLDER + "/");
        File[] users = f.listFiles();

        for (File u : users) {
            User user = new User(FileUtils.getJsonFromFile(u));
            if (user.getType().equals("student")) {
                rv.add(user);
            }
        }

        return rv;
    }
}
