package students;

import database.DBConnection;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.*;

public class ViewStudents extends JFrame {
    private JTable table;
    private DefaultTableModel model;

    public ViewStudents() {
        setTitle("View Students");
        setSize(800, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table setup
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(30, 30, 700, 250);
        add(scrollPane);

        // Delete button
        JButton deleteButton = new JButton("Delete Selected");
        deleteButton.setBounds(300, 300, 200, 30);
        add(deleteButton);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelected();
            }
        });

        // View Details button
        JButton viewDetailsButton = new JButton("View Details");
        viewDetailsButton.setBounds(540, 300, 150, 30);
        add(viewDetailsButton);
        viewDetailsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int studentId = (int) model.getValueAt(row, 0);
                    new StudentDetails(studentId);
                } else {
                    JOptionPane.showMessageDialog(ViewStudents.this,
                            "Please select a student first.",
                            "No Selection",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Load data
        loadStudents();

        setVisible(true);
    }

    private void loadStudents() {
        try (Connection conn = DBConnection.getConnection()) {
            String query =
                    "SELECT s.id,\n" +
                            "       s.roll_no,\n" +
                            "       s.name,\n" +
                            "       s.course,\n" +
                            "       s.branch,\n" +
                            "       s.year,\n" +
                            "       s.semester,\n" +
                            "       (SELECT a.status\n" +
                            "        FROM attendance a\n" +
                            "        WHERE a.student_id = s.id\n" +
                            "        ORDER BY a.date DESC\n" +
                            "        LIMIT 1) AS latest_attendance,\n" +
                            "       (SELECT CONCAT(m.subject, ' (', m.marks, ')')\n" +
                            "        FROM marks m\n" +
                            "        WHERE m.student_id = s.id\n" +
                            "        ORDER BY m.id DESC\n" +
                            "        LIMIT 1) AS latest_mark\n" +
                            "FROM students s";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Define table columns
            model.addColumn("ID");
            model.addColumn("Roll No");
            model.addColumn("Name");
            model.addColumn("Course");
            model.addColumn("Branch");
            model.addColumn("Year");
            model.addColumn("Semester");
            model.addColumn("Latest Attendance");
            model.addColumn("Latest Mark");

            // Populate table rows
            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("roll_no"),
                        rs.getString("name"),
                        rs.getString("course"),
                        rs.getString("branch"),
                        rs.getInt("year"),
                        rs.getInt("semester"),
                        rs.getString("latest_attendance"),
                        rs.getString("latest_mark")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteSelected() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int id = (int) model.getValueAt(row, 0);
            try (Connection conn = DBConnection.getConnection()) {
                String deleteSQL = "DELETE FROM students WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(deleteSQL);
                stmt.setInt(1, id);
                int deleted = stmt.executeUpdate();
                if (deleted > 0) {
                    model.removeRow(row);
                    JOptionPane.showMessageDialog(this,
                            "Student deleted successfully!",
                            "Deleted",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        // For quick testing
        SwingUtilities.invokeLater(ViewStudents::new);
    }
}
