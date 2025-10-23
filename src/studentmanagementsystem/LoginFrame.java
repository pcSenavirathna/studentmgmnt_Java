package StudentManagementSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private AuthService authService;

    public LoginFrame() {
        authService = new AuthService();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Student Information System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null); // Center window
        setResizable(false);

        // Create main panel with background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 245, 250));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(240, 245, 250));
        JLabel titleLabel = new JLabel("Student Information System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(70, 130, 180));
        titlePanel.add(titleLabel);

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.ipady = 8;
        formPanel.add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.ipady = 8;
        formPanel.add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 12));
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(passwordField, gbc);

        // Login Button
        loginButton = new JButton("🔐 Login"); // Set initial text to match reset
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.ipady = 12;
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        // Test Credentials Panel
        JPanel testPanel = new JPanel();
        testPanel.setBackground(new Color(255, 250, 240));
        testPanel.setBorder(BorderFactory.createTitledBorder("Test Credentials"));
        testPanel.setLayout(new GridLayout(2, 1, 5, 5));
        
        JLabel adminLabel = new JLabel("Admin: admin / admin123");
        JLabel studentLabel = new JLabel("Student: student1 / student123");
        
        adminLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        studentLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        adminLabel.setForeground(Color.DARK_GRAY);
        studentLabel.setForeground(Color.DARK_GRAY);
        
        testPanel.add(adminLabel);
        testPanel.add(studentLabel);

        // Add panels to main panel
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(testPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        // Add action listeners
        loginButton.addActionListener(new LoginAction());
        
        // Enter key support for both fields
        usernameField.addActionListener(new LoginAction());
        passwordField.addActionListener(new LoginAction());
        
        // Set focus to username field
        usernameField.requestFocusInWindow();
        
        setVisible(true);
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                showErrorMessage("Please enter both username and password!");
                return;
            }

            // Show loading state
            loginButton.setText("Logging in...");
            loginButton.setEnabled(false);

            // Simulate some processing time
            Timer timer = new Timer(1000, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evt) {
                    performLogin(username, password);
                }
            });
            timer.setRepeats(false);
            timer.start();
        }

        private void performLogin(String username, String password) {
            User user = authService.authenticate(username, password);

            if (user != null) {
                // Login successful
                showSuccessMessage("Login successful! Welcome " + user.getUsername());

                // Open main dashboard
                SwingUtilities.invokeLater(() -> {
                    new HomePageFrame();
                    dispose(); // Close login window
                });

            } else {
                // Login failed
                showErrorMessage("Invalid username or password!");
                passwordField.setText(""); // Clear password field
                passwordField.requestFocusInWindow();

                // Reset button
                loginButton.setText("🔐 Login");
                loginButton.setEnabled(true);
            }
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Login Error", 
            JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, 
            message, 
            "Login Successful", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    // Main method for testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame();
        });
    }
}