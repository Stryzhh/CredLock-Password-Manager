package Main.Data;

import Main.DatabaseConnection;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LoggedData {

    private static int id;

    public static ResultSet retrievePasswords() throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        String find = "SELECT id, website, username, password FROM passwords WHERE user_id = '" + id + "'";

        return statement.executeQuery(find);
    }

    public static void setId(int id) {
        LoggedData.id = id;
    }

    public static int getId() {
        return id;
    }

}
