package Main.Register;

import Main.DatabaseConnection;
import Main.NeutralFunctions;
import Main.Data.RegisteredData;
import com.jfoenix.controls.JFXCheckBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javax.mail.MessagingException;

public class RegisterController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField email;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField confirmPassword;
    @FXML
    private TextField field;
    @FXML
    private TextField confirmField;
    @FXML
    private ImageView emailIcon;
    @FXML
    private ImageView usernameIcon;
    @FXML
    private ImageView passwordIcon;
    @FXML
    private ImageView confirmIcon;
    @FXML
    private ImageView exitIcon;
    @FXML
    private JFXCheckBox show;
    @FXML
    private JFXCheckBox showConfirm;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private ImageView closeIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        emailIcon.setImage(new Image(new File("images\\email.png").toURI().toString()));
        usernameIcon.setImage(new Image(new File("images\\username.png").toURI().toString()));
        passwordIcon.setImage(new Image(new File("images\\password.png").toURI().toString()));
        confirmIcon.setImage(new Image(new File("images\\confirm.png").toURI().toString()));
        minimizeIcon.setImage(new Image(new File("images\\minimize.png").toURI().toString()));
        closeIcon.setImage(new Image(new File("images\\grey-cross.png").toURI().toString()));
        exitIcon.setImage(new Image(new File("images\\back.png").toURI().toString()));

        field.setVisible(false);
        confirmField.setVisible(false);

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

        showConfirm.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old, Boolean newV) -> {
            if (showConfirm.isSelected()) {
                confirmField.setText(confirmPassword.getText());
                confirmField.setVisible(true);
                confirmPassword.setVisible(false);
            } else {
                confirmPassword.setText(confirmField.getText());
                confirmPassword.setVisible(true);
                confirmField.setVisible(false);
            }
        });
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void registerUser() throws SQLException, IOException, MessagingException, NoSuchAlgorithmException {
        if (validate()) {
            RegisteredData data = new RegisteredData();
            RegisteredData.setUsername(username.getText());
            RegisteredData.setEmail(email.getText());

            if (!show.isSelected()) {
                RegisteredData.setPassword(password.getText());
            } else {
                RegisteredData.setPassword(field.getText());
            }

            data.sendCode();
            NeutralFunctions.changeWindow("Main/Verify/verify.fxml", window);
        }
    }

    private boolean validate() throws SQLException, NoSuchAlgorithmException {
        boolean create = false;

        if (!emailExists()) {
            if (NeutralFunctions.usernameExists(username, errorLabel)) {
                if (NeutralFunctions.verifyEmail(email.getText())) {
                    if (username.getText().length() > 3) {
                        if (email.getText().contains("@")) {
                            if (NeutralFunctions.passwordMatch(password, field, confirmPassword, confirmField)) {
                                if (password.getText().length() > 5 || field.getText().length() > 5) {
                                    create = true;
                                } else {
                                    errorLabel.setText("Password not long enough.");
                                }
                            } else {
                                errorLabel.setText("Password doesn't match.");
                            }
                        } else {
                            errorLabel.setText("Email in incorrect format.");
                        }
                    } else {
                        errorLabel.setText("Username not long enough.");
                    }
                } else {
                    errorLabel.setText("Email doesn't exist.");
                }
            } else {
                errorLabel.setText("Username already used.");
            }
        } else {
            errorLabel.setText("Email already used.");
        }

        return create;
    }

    private boolean emailExists() throws SQLException, NoSuchAlgorithmException {
        boolean found = false;
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.getConnection();

        Statement statement = connection.createStatement();
        String find = "SELECT COUNT(*) FROM users WHERE email = '"
                + NeutralFunctions.performHash(email.getText()) + "'";
        ResultSet results = statement.executeQuery(find);

        while(results.next()) {
            if (results.getInt(1) == 1) {
                found = true;
            }
        }

        return found;
    }

    public void minimize() {
        NeutralFunctions.minimize(window);
    }

    public void back() throws IOException {
        NeutralFunctions.changeWindow("Main/Login/login.fxml", window);
    }

    public void exitApplication() {
        System.exit(1);
    }

}
