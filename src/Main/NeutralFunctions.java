package Main;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class NeutralFunctions {

    public static void dragWindow(AnchorPane window) {
        Stage thisWindow = (Stage) window.getScene().getWindow();

        window.setOnMousePressed(pressEvent -> window.setOnMouseDragged(dragEvent -> {
            thisWindow.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
            thisWindow.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
        }));
    }

    public static boolean verifyEmail(String input) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(input);
        return matcher.find();
    }

    public static void changeWindow(String fxml, AnchorPane window) throws IOException {
        Parent next = FXMLLoader.load(Objects.requireNonNull(NeutralFunctions.class
                .getClassLoader().getResource(fxml)));
        Stage thisWindow = (Stage) window.getScene().getWindow();
        Stage nextWindow = new Stage();

        nextWindow.initStyle(StageStyle.UNDECORATED);
        nextWindow.setScene(new Scene(next, 360, 570));
        nextWindow.getIcons().add(new javafx.scene.image.Image("icon.png"));
        nextWindow.show();
        thisWindow.close();
    }

    public static boolean usernameExists(TextField username, Label error) throws SQLException, NoSuchAlgorithmException {
        boolean found = false;

        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        String find = "SELECT COUNT(*) FROM users WHERE username = '" +
                performHash(username.getText()) + "'";
        ResultSet results = statement.executeQuery(find);

        while(results.next()) {
            if (results.getInt(1) == 1) {
                found = true;
                error.setText("A user with this username already exists.");
            }
        }

        return !found;
    }

    public static boolean passwordMatch(PasswordField password, TextField field, PasswordField confirmPassword, TextField confirmField) {
        boolean found = false;

        if (!password.getText().equals("")) {
            if (password.getText().equals(confirmPassword.getText())) {
                found = true;
            } else if (password.getText().equals(confirmField.getText())) {
                found = true;
            }
        } else if (!field.getText().equals("")) {
            if (field.getText().equals(confirmPassword.getText())) {
                found = true;
            } else if (field.getText().equals(confirmField.getText())) {
                found = true;
            }
        }

        return found;
    }

    public static String performHash(String data) throws NoSuchAlgorithmException {
        MessageDigest message = MessageDigest.getInstance("MD5");
        message.update(data.getBytes());

        byte[] result = message.digest();
        StringBuilder builder = new StringBuilder();
        for (byte b : result) {
            builder.append(String.format("%02x", b));
        }

        return builder.toString();
    }

    public static String encrypt(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String decrypt(String data) {
        return new String(Base64.getMimeDecoder().decode(data));
    }

    public static String hideText(String password) {
        return "*".repeat(password.length());
    }

    public static void minimize(AnchorPane window) {
        Stage thisWindow = (Stage) window.getScene().getWindow();
        thisWindow.setIconified(true);
    }

}
