package attendance;

import database.DBConnection;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AttendanceEntry extends JFrame {
    JTextField studentIdField;
    JComboBox<String> statusBox;
    JButton saveButton;

    public AttendanceEntry() {
        setTitle("Attendance Entry");
        setSize(300, 250);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(30, 30, 100, 25);
        add(idLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(130, 30, 100, 25);
        add(studentIdField);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setBounds(30, 80, 100, 25);
        add(statusLabel);

        String[] statusOptions = {"Present", "Absent"};
        statusBox = new JComboBox<>(statusOptions);
        statusBox.setBounds(130, 80, 100, 25);
        add(statusBox);

        saveButton = new JButton("Save");
        saveButton.setBounds(90, 140, 100, 30);
        add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveAttendance();
            }
        });

        setVisible(true);
    }

    private void saveAttendance() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO attendance (student_id, date, status) VALUES (?, CURRENT_DATE(), ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(studentIdField.getText()));
            stmt.setString(2, (String) statusBox.getSelectedItem());

            int inserted = stmt.executeUpdate();
            if (inserted > 0) {
                JOptionPane.showMessageDialog(this, "Attendance recorded successfully!");
                dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
