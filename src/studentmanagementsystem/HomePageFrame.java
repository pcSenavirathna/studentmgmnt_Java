package StudentManagementSystem;

import javax.swing.*;
import java.awt.*;

public class HomePageFrame extends JFrame {
    public HomePageFrame() {
        setTitle("Student Information Management System - Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(1, 2));

        // Left Panel
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(new Color(70, 130, 180));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 20, 40, 20));

        // Logo placeholder
        JPanel logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(200, 200));
        logoPanel.setMaximumSize(new Dimension(200, 200));
        logoPanel.setBackground(Color.WHITE);

        // Load logo image
        ImageIcon logoIcon = new ImageIcon("src/studentmanagementsystem/images/logo.png"); // Correct relative path
        Image logoImg = logoIcon.getImage().getScaledInstance(180, 180, Image.SCALE_SMOOTH);
        JLabel logoLabel = new JLabel(new ImageIcon(logoImg), JLabel.CENTER);

        logoPanel.add(logoLabel);

        // System name files
        JLabel systemLabel = new JLabel("<html><center>Student Information<br>Management System</center></html>");
        systemLabel.setFont(new Font("Arial", Font.BOLD, 22));
        systemLabel.setForeground(Color.WHITE);
        systemLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Log Out button
        JButton logoutButton = new JButton("Log Out");
        logoutButton.setBackground(new Color(25, 25, 112));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Arial", Font.BOLD, 16));
        logoutButton.setMaximumSize(new Dimension(120, 40));
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        logoutButton.addActionListener(e -> {
            dispose(); // Close HomePageFrame
            new LoginFrame(); // Open LoginFrame
        });

        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(logoPanel);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(systemLabel);
        leftPanel.add(Box.createVerticalStrut(30));
        leftPanel.add(logoutButton);
        leftPanel.add(Box.createVerticalGlue());

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setLayout(null);

        JLabel welcomeLabel = new JLabel("<html><center>Welcome to<br><b>Student Information Management System</b></center></html>", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
        welcomeLabel.setBounds(40, 20, 400, 80);
        rightPanel.add(welcomeLabel);

        String[] features = {"Student", "Course", "Department", "Enrollment"};
        int y = 120;
        for (String feature : features) {
            JPanel circlePanel = new JPanel() {
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(new Color(70, 130, 180));
                    g.fillOval(0, 0, 40, 40);
                }
            };
            circlePanel.setBounds(120, y, 40, 40);

            JButton featureButton = new JButton(feature);
            featureButton.setFont(new Font("Arial", Font.BOLD, 18));
            featureButton.setBackground(new Color(70, 130, 180));
            featureButton.setForeground(Color.WHITE);
            featureButton.setFocusPainted(false);
            featureButton.setBounds(180, y, 220, 40);

            // Add navigation for Student button
            if (feature.equals("Student")) {
                featureButton.addActionListener(e -> {
                    dispose(); // Close HomePageFrame
                    new StudentPageFrame(); // Open StudentPageFrame
                });
            }

            // Add navigation for Course button
            if (feature.equals("Course")) {
                featureButton.addActionListener(e -> {
                    dispose();
                    new CoursePageFrame();
                });
            }

            // Add navigation for Department button
            if (feature.equals("Department")) {
                featureButton.addActionListener(e -> {
                    dispose(); // Close HomePageFrame
                    new DepartmentPageFrame(); // Open DepartmentPageFrame
                });
            }

            // Add navigation for Enrollment button
            if (feature.equals("Enrollment")) {
                featureButton.addActionListener(e -> {
                    dispose();
                    new EnrollmentPageFrame();
                });
            }

            rightPanel.add(circlePanel);
            rightPanel.add(featureButton);

            y += 70;
        }

        add(leftPanel);
        add(rightPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HomePageFrame::new);
    }
}