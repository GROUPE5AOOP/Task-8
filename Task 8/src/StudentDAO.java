import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;

public class StudentDAO {

    // Load all students into table model
    public void loadStudents(DefaultTableModel model) {
        model.setRowCount(0);
        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Insert new student
    public void addStudent(String name, String course) {
        try (Connection conn = Database.connect();
             Statement st = conn.createStatement()) {
            st.executeUpdate("INSERT INTO students(name, course) VALUES('" + name + "', '" + course + "')");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Update
    public void editStudent(int id, String name, String course) {
        try (Connection conn = Database.connect();
             Statement st = conn.createStatement()) {
            st.executeUpdate("UPDATE students SET name='" + name + "', course='" + course + "' WHERE id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void deleteStudent(int id) {
        try (Connection conn = Database.connect();
             Statement st = conn.createStatement()) {
            st.executeUpdate("DELETE FROM students WHERE id=" + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Search
    public void searchStudent(DefaultTableModel model, String key) {
        model.setRowCount(0);
        try (Connection conn = Database.connect();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM students WHERE name LIKE '%" + key + "%'")) {

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
