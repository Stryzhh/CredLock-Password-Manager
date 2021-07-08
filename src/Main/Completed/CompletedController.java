package Main.Completed;

import Main.NeutralFunctions;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class CompletedController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private ImageView closeIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        minimizeIcon.setImage(new Image(new File("images\\minimize.png").toURI().toString()));
        closeIcon.setImage(new Image(new File("images\\grey-cross.png").toURI().toString()));
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void back() throws IOException {
        NeutralFunctions.changeWindow("Main/Login/login.fxml", window);
    }

    public void minimize() {
        NeutralFunctions.minimize(window);
    }

    public void exitApplication() {
        System.exit(1);
    }

}
