package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/student_management?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // change if your username is different
    private static final String PASSWORD = "Vishesh@123"; // change to your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            // Explicitly load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found.", e);
        }

        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
