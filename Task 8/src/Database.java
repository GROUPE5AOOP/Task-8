import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    public static Connection connect() {
        String url = "jdbc:mysql://localhost:3306/studentsinfo";
        String user = "root"; // change if your MySQL uses another username
        String pass = "king";     // add password if your MySQL has one

        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            System.out.println("Database connection failed");
            e.printStackTrace();
            return null;
        }
    }
}
