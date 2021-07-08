package Main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    public Connection getConnection() throws SQLException {
        String database = "credlock";
        String username = "root";
        String password = "oof";
        String url = "jdbc:mysql://localhost:3306/" + database;

        return DriverManager.getConnection(url, username, password);
    }

}
