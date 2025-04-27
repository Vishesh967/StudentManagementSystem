package students;

import database.DBConnection;

import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddStudent extends JFrame {
    JTextField rollField, nameField, courseField, branchField, yearField, semesterField;
    JButton addButton;

    public AddStudent() {
        setTitle("Add Student");
        setSize(400, 400);
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel rollLabel = new JLabel("Roll No:");
        rollLabel.setBounds(30, 30, 100, 25);
        add(rollLabel);

        rollField = new JTextField();
        rollField.setBounds(150, 30, 150, 25);
        add(rollField);

        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setBounds(30, 70, 100, 25);
        add(nameLabel);

        nameField = new JTextField();
        nameField.setBounds(150, 70, 150, 25);
        add(nameField);

        JLabel courseLabel = new JLabel("Course:");
        courseLabel.setBounds(30, 110, 100, 25);
        add(courseLabel);

        courseField = new JTextField();
        courseField.setBounds(150, 110, 150, 25);
        add(courseField);

        JLabel branchLabel = new JLabel("Branch:");
        branchLabel.setBounds(30, 150, 100, 25);
        add(branchLabel);

        branchField = new JTextField();
        branchField.setBounds(150, 150, 150, 25);
        add(branchField);

        JLabel yearLabel = new JLabel("Year:");
        yearLabel.setBounds(30, 190, 100, 25);
        add(yearLabel);

        yearField = new JTextField();
        yearField.setBounds(150, 190, 150, 25);
        add(yearField);

        JLabel semLabel = new JLabel("Semester:");
        semLabel.setBounds(30, 230, 100, 25);
        add(semLabel);

        semesterField = new JTextField();
        semesterField.setBounds(150, 230, 150, 25);
        add(semesterField);

        addButton = new JButton("Add Student");
        addButton.setBounds(100, 280, 150, 30);
        add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addStudent();
            }
        });

        setVisible(true);
    }

    private void addStudent() {
        try (Connection conn = DBConnection.getConnection()) {
            String query = "INSERT INTO students (roll_no, name, course, branch, year, semester) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, rollField.getText());
            stmt.setString(2, nameField.getText());
            stmt.setString(3, courseField.getText());
            stmt.setString(4, branchField.getText());
            stmt.setInt(5, Integer.parseInt(yearField.getText()));
            stmt.setInt(6, Integer.parseInt(semesterField.getText()));

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Student added successfully!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
