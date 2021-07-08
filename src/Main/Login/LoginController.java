package Main.Login;

import Main.DatabaseConnection;
import Main.NeutralFunctions;
import com.jfoenix.controls.JFXCheckBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView companyIcon;
    @FXML
    private ImageView usernameIcon;
    @FXML
    private ImageView passwordIcon;
    @FXML
    private JFXCheckBox show;
    @FXML
    private JFXCheckBox remember;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField field;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private ImageView closeIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        companyIcon.setImage(new Image(new File("images\\icon.png").toURI().toString()));
        usernameIcon.setImage(new Image(new File("images\\username.png").toURI().toString()));
        passwordIcon.setImage(new Image(new File("images\\password.png").toURI().toString()));
        minimizeIcon.setImage(new Image(new File("images\\minimize.png").toURI().toString()));
        closeIcon.setImage(new Image(new File("images\\grey-cross.png").toURI().toString()));

        field.setVisible(false);
        show.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old, Boolean newV) -> {
            if (show.isSelected()) {
                field.setText(password.getText());
                field.setVisible(true);
                password.setVisible(false);
            } else {
                password.setText(field.getText());
                password.setVisible(true);
                field.setVisible(false);
            }
        });
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void login() throws SQLException, IOException, NoSuchAlgorithmException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.getConnection();

        String hashedUsername = NeutralFunctions.performHash(username.getText());
        String hashedPassword;
        if (!show.isSelected()) {
            hashedPassword = NeutralFunctions.performHash(password.getText());
        } else {
            hashedPassword = NeutralFunctions.performHash(field.getText());
        }

        Statement statement = connection.createStatement();
        String retrieve = "SELECT count(1) FROM users WHERE username = '" +
                hashedUsername + "' AND password = '" + hashedPassword + "'";
        ResultSet results = statement.executeQuery(retrieve);

        while (results.next()) {
            if (results.getInt(1) == 1) {
                statement = connection.createStatement();

                retrieve = "SELECT id FROM users WHERE username = '" + hashedUsername
                        + "' AND password = '" + hashedPassword + "'";
                results = statement.executeQuery(retrieve);

                while (results.next()) {
                    Main.Data.LoggedData.setId(results.getInt("id"));

                    if (remember.isSelected()) {
                        Map<String, Integer> map = new HashMap<>();
                        map.put("id", Main.Data.LoggedData.getId());
                        JSONObject json = new JSONObject(map);

                        Files.write(Paths.get("src/Main/Data/remember-me.json"),
                                json.toJSONString().getBytes());
                    }
                }
                NeutralFunctions.changeWindow("Main/Passwords/passwords.fxml", window);
            } else {
                errorLabel.setText("Details incorrect or email not verified.");
            }
        }
    }

    public void register() throws IOException {
        NeutralFunctions.changeWindow("Main/Register/register.fxml", window);
    }

    public void forgot() throws IOException {
        NeutralFunctions.changeWindow("Main/ResetCredentials/reset.fxml", window);
    }

    public void minimize() {
        NeutralFunctions.minimize(window);
    }

    public void exitApplication() {
        System.exit(1);
    }

}
