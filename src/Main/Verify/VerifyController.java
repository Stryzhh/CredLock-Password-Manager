package Main.Verify;

import Main.DatabaseConnection;
import Main.NeutralFunctions;
import Main.Data.RegisteredData;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
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

public class VerifyController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField authorization;
    @FXML
    private ImageView authIcon;
    @FXML
    private ImageView exitIcon;
    @FXML
    private Label errorLabel;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private ImageView closeIcon;

    private SimpleDateFormat dateFormat;
    private long time = 60000;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFormat = new SimpleDateFormat("mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        timer.start();

        authIcon.setImage(new Image(new File("images\\authentication.png").toURI().toString()));
        minimizeIcon.setImage(new Image(new File("images\\minimize.png").toURI().toString()));
        closeIcon.setImage(new Image(new File("images\\grey-cross.png").toURI().toString()));
        exitIcon.setImage(new Image(new File("images\\back.png").toURI().toString()));
    }

    public void verify() throws SQLException, IOException, NoSuchAlgorithmException {
        if (Integer.parseInt(authorization.getText()) == RegisteredData.getCode()) {
            DatabaseConnection database = new DatabaseConnection();
            Connection connection = database.getConnection();

            String insertField = "INSERT INTO users(username, password, email) VALUES ('";
            String insertValues = NeutralFunctions.performHash(RegisteredData.getUsername()) + "', '" +
                    NeutralFunctions.performHash(RegisteredData.getPassword()) +
                    "', '" + NeutralFunctions.performHash(RegisteredData.getEmail()) + "')";
            String insert = insertField + insertValues;

            Statement insertStatement = connection.createStatement();
            insertStatement.executeUpdate(insert);
            NeutralFunctions.changeWindow("Main/Completed/completed.fxml", window);
        } else {
            errorLabel.setText("Incorrect code.");
        }
    }

    public void resend() throws MessagingException, UnsupportedEncodingException {
        RegisteredData data = new RegisteredData();
        if (time == 0) {
            data.sendCode();
            time = 60000;
            timer.start();
        }
    }

    private final Timer timer = new Timer(1000, e -> {
        if (time != 0) {
            time -= 1000;
            Date date = new Date(time);

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

    public void drag() {
        NeutralFunctions.dragWindow(window);
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
