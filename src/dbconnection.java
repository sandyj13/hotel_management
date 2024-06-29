import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class dbconnection {
    // JDBC URL, username, and password
    static final String JDBC_URL = "jdbc:mysql://localhost:3306/hotel_management";
    static final String USERNAME = "root";
    static final String PASSWORD = "sandy@13"; // Replace 'your_password' with your actual password

    public static void main(String[] args) {
        try {
            Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Creating a statement object
            Statement statement = connection.createStatement();

            String sql = "CREATE DATABASE IF NOT EXISTS hotel_management";

            statement.executeUpdate(sql);
            System.out.println("Database created successfully!");
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add the getConnection method if needed for use in other classes
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }
}
