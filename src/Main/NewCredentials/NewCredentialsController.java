package Main.NewCredentials;

import Main.DatabaseConnection;
import Main.NeutralFunctions;
import com.jfoenix.controls.JFXCheckBox;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
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

public class NewCredentialsController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField newUsername;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField confirmNewPassword;
    @FXML
    private TextField field;
    @FXML
    private TextField confirmField;
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
                field.setText(newPassword.getText());
                field.setVisible(true);
                newPassword.setVisible(false);
            } else {
                newPassword.setText(field.getText());
                newPassword.setVisible(true);
                field.setVisible(false);
            }
        });

        showConfirm.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old, Boolean newV) -> {
            if (showConfirm.isSelected()) {
                confirmField.setText(confirmNewPassword.getText());
                confirmField.setVisible(true);
                confirmNewPassword.setVisible(false);
            } else {
                confirmNewPassword.setText(confirmField.getText());
                confirmNewPassword.setVisible(true);
                confirmField.setVisible(false);
            }
        });
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void save() throws SQLException, IOException, NoSuchAlgorithmException {
        if (validate()) {
            DatabaseConnection database = new DatabaseConnection();
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();

            String password;
            if (!show.isSelected()) {
                password = newPassword.getText();
            } else {
                password = field.getText();
            }

            String update = "UPDATE users SET username = '" + NeutralFunctions.performHash(newUsername.getText()) + "', password = '"
                    + NeutralFunctions.performHash(password) + "' WHERE id = '" + Main.Data.ResetData.getId() + "'";
            statement.execute(update);
            NeutralFunctions.changeWindow("Main/Completed/completed.fxml", window);
        }
    }

    private boolean validate() throws SQLException, NoSuchAlgorithmException {
        boolean create = false;

        if (NeutralFunctions.usernameExists(newUsername, errorLabel)) {
            if (newUsername.getText().length() > 3) {
                if (NeutralFunctions.passwordMatch(newPassword, field, confirmNewPassword, confirmField)) {
                    if (newPassword.getText().length() > 5 || field.getText().length() > 5) {
                        create = true;
                    } else {
                        errorLabel.setText("Password not long enough.");
                    }
                } else {
                    errorLabel.setText("Password doesn't match.");
                }
            } else {
                errorLabel.setText("Username not long enough.");
            }
        }

        return create;
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
