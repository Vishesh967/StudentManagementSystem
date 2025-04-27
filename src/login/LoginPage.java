package login;

import database.DBConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginPage extends JFrame {
    JTextField usernameField;
    JPasswordField passwordField;
    RoundedButton loginButton; // Use custom button

    public LoginPage() {
        setTitle("Login");
        setSize(700, 400); // Wider for left image + right form
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel (split into 2: left image, right form)
        JPanel mainPanel = new JPanel(new BorderLayout());

        // ==== Left: Image Panel ====
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/images/loginimage.png"));
        Image scaledImage = originalIcon.getImage().getScaledInstance(350, 400, Image.SCALE_SMOOTH); // Twice original size
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        JLabel imageLabel = new JLabel(scaledIcon);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.setBackground(Color.WHITE); // Background white around image
        mainPanel.add(imagePanel, BorderLayout.WEST);

        // ==== Right: Login Form Panel ====
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(220, 235, 245)); // Light blue background

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(userLabel, gbc);

        usernameField = new JTextField(15);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(passLabel, gbc);

        passwordField = new JPasswordField(15);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        loginButton = new RoundedButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Action listener for login
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = String.valueOf(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            String query = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String role = rs.getString("role");
                JOptionPane.showMessageDialog(this, "Login Successful as " + role);
                dispose();
                new dashboard.Dashboard(role);
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Credentials!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

// ========== RoundedButton CLASS ==========
class RoundedButton extends JButton {
    private Color normalColor = new Color(70, 130, 180);  // Steel Blue
    private Color hoverColor = new Color(100, 160, 210);  // Light Steel Blue
    private boolean hover = false;

    public RoundedButton(String text) {
        super(text);
        setContentAreaFilled(false);
        setFocusPainted(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 16));
        setCursor(new Cursor(Cursor.HAND_CURSOR)); // Hand cursor on hover

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }
            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(hover ? hoverColor : normalColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
        super.paintComponent(g2);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No visible border
    }
}
