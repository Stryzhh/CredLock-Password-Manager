package Main.Passwords;

import Main.Data.TableData;
import Main.Data.Passwords;
import Main.NeutralFunctions;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.json.simple.JSONObject;

public class PasswordsController implements Initializable {

    @FXML
    private AnchorPane window;
    @FXML
    private TableView<Passwords> table;
    @FXML
    private TableColumn<Passwords, String> ID;
    @FXML
    private TableColumn<Passwords, String> website;
    @FXML
    private TableColumn<Passwords, String> username;
    @FXML
    private TableColumn<Passwords, String> password;
    @FXML
    private ImageView visible;
    @FXML
    private ImageView addIcon;
    @FXML
    private ImageView deleteIcon;
    @FXML
    private ImageView manageIcon;
    @FXML
    private ImageView exitIcon;
    @FXML
    private ImageView minimizeIcon;
    @FXML
    private ImageView closeIcon;
    private final ObservableList<Passwords> data = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        table.getStylesheets().add(getClass().getResource("header.css").toString());
        try {
            ResultSet results = Main.Data.LoggedData.retrievePasswords();
            while (results.next()) {
                data.add(new Passwords(results.getString("id"), NeutralFunctions.decrypt(results.getString("website")),
                        NeutralFunctions.decrypt(results.getString("username")),
                        NeutralFunctions.decrypt(results.getString("password")),
                        NeutralFunctions.hideText(NeutralFunctions.decrypt(results.getString("password")))));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        ID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        website.setCellValueFactory(new PropertyValueFactory<>("website"));
        username.setCellValueFactory(new PropertyValueFactory<>("username"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));

        setCellColours(ID);
        setCellColours(website);
        setCellColours(username);
        setCellColours(password);
        table.setItems(data);

        visible.setImage(new Image(new File("images\\visible.png").toURI().toString()));
        addIcon.setImage(new Image(new File("images\\add.png").toURI().toString()));
        deleteIcon.setImage(new Image(new File("images\\delete.png").toURI().toString()));
        manageIcon.setImage(new Image(new File("images\\manage.png").toURI().toString()));
        minimizeIcon.setImage(new Image(new File("images\\minimize.png").toURI().toString()));
        closeIcon.setImage(new Image(new File("images\\grey-cross.png").toURI().toString()));
        exitIcon.setImage(new Image(new File("images\\back.png").toURI().toString()));

        table.getSelectionModel().selectedIndexProperty().addListener(e -> {
            setOtherCells(ID);
            setOtherCells(website);
            setOtherCells(username);
            setOtherCells(password);
        });
    }

    public void drag() {
        NeutralFunctions.dragWindow(window);
    }

    public void inspect() throws IOException {
        if (table.getSelectionModel().getSelectedItem() != null) {
            TableData.setData(data);
            TableData.setTable(table);
            TableData.setRow(table.getSelectionModel().getSelectedItem());
            showDialog("Main/InspectPassword/inspect.fxml", "Inspect Dialog");
        }
    }

    public void add() throws IOException {
        TableData.setData(data);
        TableData.setTable(table);
        showDialog("Main/AddPassword/add.fxml", "Add Dialog");
    }

    public void amend() throws IOException {
        if (table.getSelectionModel().getSelectedItem() != null) {
            TableData.setData(data);
            TableData.setTable(table);
            TableData.setRow(table.getSelectionModel().getSelectedItem());
            showDialog("Main/AmendPassword/amend.fxml", "Amend Dialog");
        }
    }

    public void remove() throws IOException {
        if (table.getSelectionModel().getSelectedItem() != null) {
            TableData.setData(data);
            TableData.setTable(table);
            TableData.setRow(table.getSelectionModel().getSelectedItem());
            showDialog("Main/ConfirmDialog/confirm.fxml", "Confirm Dialog");
        }
    }

    private void showDialog(String fxml, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().
                getResource(fxml));
        Parent window = fxmlLoader.load();
        Stage newWindow = new Stage();

        newWindow.setAlwaysOnTop(true);
        newWindow.initStyle(StageStyle.UNDECORATED);
        newWindow.setTitle(title);
        newWindow.setScene(new Scene(window));
        newWindow.show();
    }

    public void logout() throws IOException {
        Map<String, Integer> map = new HashMap<>();
        map.put("id", null);
        JSONObject json = new JSONObject(map);
        Files.write(Paths.get("src/Main/Data/remember-me.json"),
                json.toJSONString().getBytes());

        NeutralFunctions.changeWindow("Main/Login/login.fxml", window);
    }

    public void setCellColours(TableColumn<Passwords, String> column) {
        column.setStyle("-fx-background-color: #2D3447;");
        column.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Passwords, String> call(TableColumn param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setTextFill(Color.DARKGREY);
                        setStyle("-fx-background-color: #2D3447; -fx-border-color: #2D3447;");
                        setText(item);
                    }
                };
            }
        });
    }

    public void setOtherCells(TableColumn<Passwords, String> column) {
        column.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Passwords, String> call(TableColumn param) {
                return new TableCell<>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        setTextFill(Color.DARKGREY);
                        setStyle("-fx-background-color: #2D3447; -fx-border-color: #2D3447;");
                        setText(item);
                    }
                };
            }
        });
    }

    public void minimize() {
        NeutralFunctions.minimize(window);
    }

    public void exitApplication() {
        System.exit(1);
    }

}
