package Main.InspectPassword;

import Main.Data.TableData;
import Main.NeutralFunctions;
import com.jfoenix.controls.JFXCheckBox;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class InspectController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TextField website;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private JFXCheckBox show;
    @FXML
    private TextField field;
    @FXML
    private ImageView websiteIcon;
    @FXML
    private ImageView usernameIcon;
    @FXML
    private ImageView passwordIcon;
    @FXML
    private ImageView closeIcon;
    @FXML
    private ImageView minimizeIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTextFields();
        websiteIcon.setImage(new Image(new File("images\\website.png").toURI().toString()));
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

    private void loadTextFields() {
        website.setText(TableData.getRow().getWebsite());
        username.setText(TableData.getRow().getUsername());
        password.setText(TableData.getRow().getActualPassword());
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void minimize() {
        NeutralFunctions.minimize(window);
    }

    public void close() {
        Stage thisWindow = (Stage) window.getScene().getWindow();
        thisWindow.close();
    }

}
