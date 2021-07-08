package Main.ResetCredentials;

import Main.DatabaseConnection;
import Main.NeutralFunctions;
import com.email.durgesh.Email;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javax.mail.MessagingException;
import javax.swing.*;

public class ResetCredentialsController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField email;
    @FXML
    private TextField authorization;
    @FXML
    private ImageView authIcon;
    @FXML
    private ImageView emailIcon;
    @FXML
    private ImageView exitIcon;
    @FXML
    private JFXButton send;
    @FXML
    private JFXButton reset;
    @FXML
    private Label errorLabel;
    @FXML
    private Label emailError;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private ImageView closeIcon;

    private long time = 0;
    private int code;
    private SimpleDateFormat dateFormat;
    private String credEmail;
    private Date date;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        timer.start();

        authIcon.setImage(new Image(new File("images\\authentication.png").toURI().toString()));
        emailIcon.setImage(new Image(new File("images\\email.png").toURI().toString()));
        minimizeIcon.setImage(new Image(new File("images\\minimize.png").toURI().toString()));
        closeIcon.setImage(new Image(new File("images\\grey-cross.png").toURI().toString()));
        exitIcon.setImage(new Image(new File("images\\back.png").toURI().toString()));
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void sendCode() throws UnsupportedEncodingException, MessagingException, SQLException, NoSuchAlgorithmException {
        if (time == 0) {
            if (NeutralFunctions.verifyEmail(email.getText())) {
                if (emailExists()) {
                    credEmail = email.getText();
                    Random random = new Random();
                    code = random.nextInt(99999 - 10000) + 10000;

                    Email sender = new Email("credlock.test@gmail.com", "Credlock_12345");
                    sender.setFrom("credlock.test@gmail.com", "CredLock");
                    sender.setSubject("Verification code.");
                    sender.setContent("This is your verification code: " + code, "text/html");
                    sender.addRecipient(email.getText());
                    sender.send();

                    reset.setDisable(false);
                    send.setText("Re-send Code");
                    time = 60000;
                    timer.start();
                }
            } else {
                emailError.setText("Invalid Email.");
            }
        } else {
            errorLabel.setText("Retry in " + dateFormat.format(date) + '.');
        }
    }

    public void proceed() throws SQLException, IOException, NoSuchAlgorithmException {
        if (Integer.parseInt(authorization.getText()) == code) {
            DatabaseConnection database = new DatabaseConnection();
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String find = "select id from users where `email` = '" + NeutralFunctions.performHash(credEmail) + "'";
            ResultSet results = statement.executeQuery(find);

            while(results.next()) {
                Main.Data.ResetData.setId(results.getInt("id"));
            }
            NeutralFunctions.changeWindow("Main/NewCredentials/credentials.fxml", window);
        } else {
            errorLabel.setText("Incorrect code.");
        }
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
        if (!found) {
            emailError.setText("A user with this email doesn't exists.");
        }

        return found;
    }

    private final Timer timer = new Timer(1000, e -> {
        if (time != 0) {
            time -= 1000;
            date = new Date(time);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    errorLabel.setText("Retry in " + dateFormat.format(date) + '.');
                }
            });
        } else {
            stop();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    errorLabel.setText("");
                }
            });
        }
    });

    private void stop() {
        timer.stop();
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
