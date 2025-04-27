package students;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentDetails extends JFrame {
    private JTable tableAttendance;
    private JTable tableMarks;
    private DefaultTableModel modelAttendance;
    private DefaultTableModel modelMarks;

    public StudentDetails(int studentId) {
        setTitle("Student Details");
        setSize(800, 400);
        setLayout(new GridLayout(2, 1));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Initialize models
        modelAttendance = new DefaultTableModel();
        modelMarks      = new DefaultTableModel();

        // Set up tables
        tableAttendance = new JTable(modelAttendance);
        tableMarks      = new JTable(modelMarks);

        add(new JScrollPane(tableAttendance));
        add(new JScrollPane(tableMarks));

        // Define columns
        modelAttendance.addColumn("Date");
        modelAttendance.addColumn("Status");
        modelMarks.addColumn     ("Subject");
        modelMarks.addColumn     ("Marks");

        // Load the data
        loadStudentDetails(studentId);

        setVisible(true);
    }

    private void loadStudentDetails(int studentId) {
        try (Connection conn = DBConnection.getConnection()) {
            // Attendance history
            String sqlAtt = "SELECT date, status FROM attendance "
                    + "WHERE student_id = ? ORDER BY date DESC";
            PreparedStatement psA = conn.prepareStatement(sqlAtt);
            psA.setInt(1, studentId);
            ResultSet rsA = psA.executeQuery();
            while (rsA.next()) {
                modelAttendance.addRow(new Object[]{
                        rsA.getDate("date"),
                        rsA.getString("status")
                });
            }

            // Marks history
            String sqlMk = "SELECT subject, marks FROM marks "
                    + "WHERE student_id = ?";
            PreparedStatement psM = conn.prepareStatement(sqlMk);
            psM.setInt(1, studentId);
            ResultSet rsM = psM.executeQuery();
            while (rsM.next()) {
                modelMarks.addRow(new Object[]{
                        rsM.getString("subject"),
                        rsM.getInt("marks")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
