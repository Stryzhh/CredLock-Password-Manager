package Main.ConfirmDialog;

import Main.Data.TableData;
import Main.NeutralFunctions;
import com.jfoenix.controls.JFXButton;
import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ConfirmController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private ImageView acceptIcon;
    @FXML
    private ImageView rejectIcon;
    @FXML
    private ImageView closeIcon;
    @FXML
    private ImageView minimizeIcon;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        acceptIcon.setImage(new Image(new File("images\\tick.png").toURI().toString()));
        rejectIcon.setImage(new Image(new File("images\\cross.png").toURI().toString()));
        minimizeIcon.setImage(new Image(new File("images\\minimize.png").toURI().toString()));
        closeIcon.setImage(new Image(new File("images\\grey-cross.png").toURI().toString()));
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void confirm() throws SQLException {
        TableData.setResponse(1);
        TableData.delete();
        Stage thisWindow = (Stage) window.getScene().getWindow();
        thisWindow.close();
    }

    public void minimize() {
        NeutralFunctions.minimize(window);
    }

    public void close() {
        TableData.setResponse(0);
        Stage thisWindow = (Stage) window.getScene().getWindow();
        thisWindow.close();
    }

}
