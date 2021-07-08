package Main.Data;

import Main.DatabaseConnection;
import Main.NeutralFunctions;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;

public class TableData {

    private static int response = 0;
    private static ObservableList<Passwords> data;
    private static TableView<Passwords> table;
    private static Passwords row;

    public static void refresh() {
        data.clear();
        try {
            ResultSet results = LoggedData.retrievePasswords();
            while (results.next()) {
                data.add(new Passwords(results.getString("id"), NeutralFunctions.decrypt(results.getString("website")),
                        NeutralFunctions.decrypt(results.getString("username")),
                        NeutralFunctions.decrypt(results.getString("password")),
                        NeutralFunctions.hideText(NeutralFunctions.decrypt(results.getString("password")))));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        table.setItems(data);
    }

    public static void add(String website, String username, String password) throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.getConnection();

        String insertField = "INSERT INTO passwords(user_id, website, username, password) VALUES ('";
        String insertValues = LoggedData.getId() + "', '" + NeutralFunctions.encrypt(website) + "', '" +
                NeutralFunctions.encrypt(username) + "', '" + NeutralFunctions.encrypt(password) + "')";
        String insert = insertField + insertValues;

        Statement insertStatement = connection.createStatement();
        insertStatement.executeUpdate(insert);
    }

    public static void amend(String newWebsite, String newUsername, String newPassword) throws SQLException {
        DatabaseConnection database = new DatabaseConnection();
        Connection connection = database.getConnection();

        Statement statement = connection.createStatement();
        String amend = "UPDATE passwords SET website = '" + NeutralFunctions.encrypt(newWebsite)
                + "', username = '" + NeutralFunctions.encrypt(newUsername) + "', password = '" +
                NeutralFunctions.encrypt(newPassword) + "' WHERE id = " + row.getID();
        statement.execute(amend);
        refresh();
    }

    public static void delete() throws SQLException {
        if (response == 1) {
            DatabaseConnection database = new DatabaseConnection();
            Connection connection = database.getConnection();

            Statement statement = connection.createStatement();
            String delete = "DELETE FROM `passwords` WHERE id = '" + row.getID() + "'";
            statement.execute(delete);
            refresh();
        }
    }

    public static void setResponse(int response) {
        TableData.response = response;
    }

    public static void setData(ObservableList<Passwords> data) {
        TableData.data = data;
    }

    public static ObservableList<Passwords> getData() {
        return data;
    }

    public static void setTable(TableView<Passwords> table) {
        TableData.table = table;
    }

    public static void setRow(Passwords row) {
        TableData.row = row;
    }

    public static Passwords getRow() {
        return row;
    }
}
