package StudentManagementSystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/studentdb2";
    private static final String USER = "root";
    private static final String PASSWORD = "123456"; // your MySQL password if any

    public static Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println(" Database Connected Successfully");
        } catch (SQLException e) {
            System.out.println(" Database Connection Failed: " + e.getMessage());
        }
        return conn;
    }
}
