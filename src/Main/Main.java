package Main;

import Main.Data.LoggedData;
import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {
        Parent nextWindow = null;
        File rememberJSON = new File("src/Main/Data/remember-me.json");

        if (rememberJSON.exists() && !rememberJSON.isDirectory()) {
            FileReader reader = new FileReader("src/Main/Data/remember-me.json");
            JSONParser jsonParser = new JSONParser();
            JSONObject object = (JSONObject) jsonParser.parse(reader);

            if (object.get("id") != null) {
                long id = (long) object.get("id");

                DatabaseConnection database = new DatabaseConnection();
                Connection connection = database.getConnection();
                Statement statement = connection.createStatement();
                String retrieve = "SELECT count(1) FROM users WHERE id = '" + id + "'";
                ResultSet results = statement.executeQuery(retrieve);

                while (results.next()) {
                    if (results.getInt(1) == 1) {
                        LoggedData.setId((int) id);
                        nextWindow = FXMLLoader.load(Objects.requireNonNull(getClass()
                                .getClassLoader().getResource("Main/Passwords/passwords.fxml")));
                    } else {
                        nextWindow = FXMLLoader.load(Objects.requireNonNull(getClass()
                                .getClassLoader().getResource("Main/Login/login.fxml")));
                    }
                }
            } else {
                nextWindow = FXMLLoader.load(Objects.requireNonNull(getClass()
                        .getClassLoader().getResource("Main/Login/login.fxml")));
            }

            //adds tray icon
            PopupMenu popupMenu = new PopupMenu();
            ImageIcon logo = new ImageIcon("src/icon.png");
            Image image = logo.getImage();

            SystemTray tray = SystemTray.getSystemTray();
            Image trayImage = image.getScaledInstance(tray.getTrayIconSize().width,
                    tray.getTrayIconSize().height, java.awt.Image.SCALE_SMOOTH);
            TrayIcon trayIcon = new TrayIcon(trayImage, "CredLock", popupMenu);

            MenuItem exitMenuItem = new MenuItem("Exit");
            exitMenuItem.addActionListener(e -> tray.remove(trayIcon));
            exitMenuItem.addActionListener(e -> System.exit(0));
            popupMenu.add(exitMenuItem);
            tray.add(trayIcon);

            //opens window
            assert nextWindow != null;
            window.initStyle(StageStyle.UNDECORATED);
            window.setScene(new Scene(nextWindow, 360, 570));
            window.getIcons().add(new javafx.scene.image.Image("icon.png"));
            window.show();
        }
    }

}