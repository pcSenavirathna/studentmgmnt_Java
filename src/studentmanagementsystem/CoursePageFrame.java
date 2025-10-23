package StudentManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CoursePageFrame extends JFrame {
    private JTextField courseIdField, courseNameField, creditsField, deptIdField, semesterField;
    private DefaultTableModel tableModel;
    private CourseDAO courseDAO = new CourseDAO();

    public CoursePageFrame() {
        setTitle("Student Information Management System - Course");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1070, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        // Top bar with title and back arrow
        JPanel topPanel = new JPanel(null);
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBounds(0, 0, 1070, 60);

        JButton backButton = new JButton();
        backButton.setBounds(10, 10, 40, 40);
        backButton.setBackground(new Color(255, 0, 0));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setIcon(new ImageIcon(new ImageIcon("src/studentmanagementsystem/images/back_arrow.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
        backButton.addActionListener(e -> {
            dispose();
            new HomePageFrame();
        });

        JLabel titleLabel = new JLabel("Student Information Management Svstem", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(60, 10, 950, 40);

        topPanel.add(backButton);
        topPanel.add(titleLabel);

        // Left form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(210, 225, 245));
        formPanel.setBounds(0, 60, 420, 500);

        courseIdField = new JTextField();
        courseNameField = new JTextField();
        creditsField = new JTextField();
        deptIdField = new JTextField();
        semesterField = new JTextField();

        String[] labels = {"Course ID", "Course Name", "Credits", "Department ID", "Semester"};
        JTextField[] fields = {courseIdField, courseNameField, creditsField, deptIdField, semesterField};
        int y = 30;
        for (int i = 0; i < labels.length; i++) {
            JButton lblBtn = new JButton(labels[i]);
            lblBtn.setBounds(20, y, 150, 35);
            lblBtn.setBackground(new Color(70, 130, 180));
            lblBtn.setForeground(Color.WHITE);
            lblBtn.setFont(new Font("Arial", Font.BOLD, 14));
            lblBtn.setFocusPainted(false);
            lblBtn.setEnabled(false);
            formPanel.add(lblBtn);

            fields[i].setBounds(180, y, 210, 35);
            fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
            formPanel.add(fields[i]);
            y += 45;
        }

        // Action buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(12, 370, 90, 40);
        addBtn.setBackground(new Color(25, 25, 112));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(112, 370, 90, 40);
        updateBtn.setBackground(new Color(25, 25, 112));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(updateBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(212, 370, 90, 40);
        clearBtn.setBackground(new Color(25, 25, 112));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(clearBtn);

        // Search bar
        JLabel searchByLabel = new JLabel("Search By");
        searchByLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchByLabel.setBounds(450, 70, 80, 30);

        JTextField searchField = new JTextField();
        searchField.setBounds(540, 70, 200, 30);

        JButton searchBtn = new JButton("Search");
        searchBtn.setBounds(750, 70, 90, 30);
        searchBtn.setBackground(new Color(25, 25, 112));
        searchBtn.setForeground(Color.WHITE);

        JButton clearSearchBtn = new JButton("Clear");
        clearSearchBtn.setBounds(850, 70, 90, 30);
        clearSearchBtn.setBackground(new Color(25, 25, 112));
        clearSearchBtn.setForeground(Color.WHITE);

        // Table
        String[] tableHeaders = {"Course ID", "Course Name", "Credits", "Department ID", "Semester"};
        tableModel = new DefaultTableModel(tableHeaders, 0);
        JTable courseTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(courseTable);
        tableScroll.setBounds(450, 110, 590, 450);

        // Add components to frame
        add(topPanel);
        add(formPanel);
        add(searchByLabel);
        add(searchField);
        add(searchBtn);
        add(clearSearchBtn);
        add(tableScroll);

        // Add button logic
        addBtn.addActionListener(e -> {
            try {
                int courseId = Integer.parseInt(courseIdField.getText().trim());
                String courseName = courseNameField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                int deptId = Integer.parseInt(deptIdField.getText().trim());
                String semester = semesterField.getText().trim();

                if (courseName.isEmpty() || semester.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Course course = new Course(courseId, courseName, credits, deptId, semester);
                boolean success = courseDAO.addCourse(course);

                if (success) {
                    JOptionPane.showMessageDialog(this, "Course added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadCourses();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add course!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs and credits must be numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            // Implement update logic here
        });


        clearBtn.addActionListener(e -> clearFields());

        setVisible(true);
    }

    private void loadCourses() {
        tableModel.setRowCount(0);
        for (Course course : courseDAO.getAllCourses()) {
            tableModel.addRow(new Object[]{
                course.getCourseId(),
                course.getCourseName(),
                course.getCredits(),
                course.getDeptId(),
                course.getSemester()
            });
        }
    }

    private void clearFields() {
        courseIdField.setText("");
        courseNameField.setText("");
        creditsField.setText("");
        deptIdField.setText("");
        semesterField.setText("");
    }
}