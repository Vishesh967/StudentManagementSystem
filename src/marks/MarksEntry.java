package marks;

import database.DBConnection;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MarksEntry extends JFrame {
    JTextField studentIdField, subjectField, marksField;
    JButton saveButton;

    public MarksEntry() {
        setTitle("Marks Entry");
        setSize(300, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel idLabel = new JLabel("Student ID:");
        idLabel.setBounds(30, 30, 100, 25);
        add(idLabel);

        studentIdField = new JTextField();
        studentIdField.setBounds(130, 30, 100, 25);
        add(studentIdField);

        JLabel subjectLabel = new JLabel("Subject:");
        subjectLabel.setBounds(30, 80, 100, 25);
        add(subjectLabel);

        subjectField = new JTextField();
        subjectField.setBounds(130, 80, 100, 25);
        add(subjectField);

        JLabel marksLabel = new JLabel("Marks:");
        marksLabel.setBounds(30, 130, 100, 25);
        add(marksLabel);

        marksField = new JTextField();
        marksField.setBounds(130, 130, 100, 25);
        add(marksField);

        saveButton = new JButton("Save");
        saveButton.setBounds(90, 190, 100, 30);
        add(saveButton);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveMarks();
            }
        });

        setVisible(true);
    }

    private void saveMarks() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO marks (student_id, subject, marks) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, Integer.parseInt(studentIdField.getText()));
            stmt.setString(2, subjectField.getText());
            stmt.setInt(3, Integer.parseInt(marksField.getText()));

            int inserted = stmt.executeUpdate();
            if (inserted > 0) {
                JOptionPane.showMessageDialog(this, "Marks recorded successfully!");
                dispose();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
