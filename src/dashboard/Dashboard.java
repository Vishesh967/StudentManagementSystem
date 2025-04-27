package dashboard;

import students.AddStudent;
import students.ViewStudents;
import attendance.AttendanceEntry;
import marks.MarksEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Dashboard extends JFrame {
    String role;
    JPanel gradientPanel; // Moved here to access inside createButton

    public Dashboard(String role) {
        this.role = role;

        setTitle("Dashboard - " + role.toUpperCase());
        setSize(600, 500); // Increased size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== Gradient Background Panel =====
        gradientPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                int w = getWidth();
                int h = getHeight();

                Color color1 = new Color(135, 206, 250); // Light Sky Blue
                Color color2 = new Color(25, 25, 112);   // Midnight Blue
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        gradientPanel.setLayout(null);
        setContentPane(gradientPanel);

        // Create and add buttons
        JButton addStudentButton = createButton("Add Student", 80);
        JButton viewStudentsButton = createButton("View Students", 150);
        JButton attendanceButton = createButton("Attendance Entry", 220);
        JButton marksButton = createButton("Marks Entry", 290);

        // Button Actions
        addStudentButton.addActionListener(e -> new AddStudent());
        viewStudentsButton.addActionListener(e -> new ViewStudents());
        attendanceButton.addActionListener(e -> new AttendanceEntry());
        marksButton.addActionListener(e -> new MarksEntry());

        setVisible(true);
    }

    // ======= Create Button Method (OUTSIDE constructor) =======
    private JButton createButton(String text, int y) {
        Color buttonColor = new Color(255, 255, 255); // White button
        Color hoverColor = new Color(100, 149, 237);  // Cornflower Blue
        Font buttonFont = new Font("Arial", Font.BOLD, 18);

        JButton button = new JButton(text);
        button.setBounds(200, y, 200, 50);
        button.setBackground(buttonColor);
        button.setFocusPainted(false);
        button.setFont(buttonFont);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));

        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setForeground(Color.WHITE);
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(buttonColor);
                button.setForeground(Color.BLACK);
            }
        });

        gradientPanel.add(button);
        return button;
    }
}
