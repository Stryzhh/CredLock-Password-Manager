package Main.Data;

import javafx.beans.property.SimpleStringProperty;

public class Passwords {

    private final SimpleStringProperty id;
    private final SimpleStringProperty website;
    private final SimpleStringProperty username;
    private final SimpleStringProperty password;
    private final SimpleStringProperty actualPassword;

    public Passwords(String ID, String website, String username, String password, String actualPassword) {
        this.id = new SimpleStringProperty(ID);
        this.website = new SimpleStringProperty(website);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
        this.actualPassword = new SimpleStringProperty(actualPassword);
    }

    public String getID() {
        return id.get();
    }

    public String getWebsite() {
        return website.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getPassword() {
        return actualPassword.get();
    }

    public String getActualPassword() {
        return password.get();
    }

}
