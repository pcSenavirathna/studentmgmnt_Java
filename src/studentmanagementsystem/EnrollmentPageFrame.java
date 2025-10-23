package StudentManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class EnrollmentPageFrame extends JFrame {
    private JTextField enrollIdField, studentIdField, courseIdField, enrollDateField, gradeField;
    private DefaultTableModel tableModel;
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();

    public EnrollmentPageFrame() {
        setTitle("Student Information Management System - Enrollment");
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

        enrollIdField = new JTextField();
        studentIdField = new JTextField();
        courseIdField = new JTextField();
        enrollDateField = new JTextField();
        gradeField = new JTextField();

        String[] labels = {"Enroll ID", "Student ID", "Course ID", "Enroll Date", "Grade"};
        JTextField[] fields = {enrollIdField, studentIdField, courseIdField, enrollDateField, gradeField};
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
        String[] tableHeaders = {"Enroll ID", "Student ID", "Course ID", "Enroll Date", "Grade"};
        tableModel = new DefaultTableModel(tableHeaders, 0);
        JTable enrollTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(enrollTable);
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
        // Add: do NOT require enrollId (DB generates it)
        addBtn.addActionListener(e -> {
            try {
                int studentId = Integer.parseInt(studentIdField.getText().trim());
                int courseId = Integer.parseInt(courseIdField.getText().trim());
                String enrollDate = enrollDateField.getText().trim();
                String grade = gradeField.getText().trim();

                if (enrollDate.isEmpty() || grade.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Enrollment enroll = new Enrollment(0, studentId, courseId, enrollDate, grade);
                boolean success = enrollmentDAO.addEnrollment(enroll);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Enrollment added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadEnrollments();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add enrollment!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Student ID and Course ID must be numbers.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update: update selected/filled enrollment row
        updateBtn.addActionListener(e -> {
            try {
                int enrollId = Integer.parseInt(enrollIdField.getText().trim());
                int studentId = Integer.parseInt(studentIdField.getText().trim());
                int courseId = Integer.parseInt(courseIdField.getText().trim());
                String enrollDate = enrollDateField.getText().trim();
                String grade = gradeField.getText().trim();

                if (enrollDate.isEmpty() || grade.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Enrollment enroll = new Enrollment(enrollId, studentId, courseId, enrollDate, grade);
                boolean ok = enrollmentDAO.updateEnrollment(enroll);
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Enrollment updated", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadEnrollments();
                    clearFields();
                    enrollIdField.setEditable(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs must be numbers.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        // Search button logic
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            tableModel.setRowCount(0);
            for (Enrollment enroll : enrollmentDAO.searchEnrollments(keyword)) {
                tableModel.addRow(new Object[] {
                        enroll.getEnrollId(),
                        enroll.getStudentId(),
                        enroll.getCourseId(),
                        enroll.getEnrollDate(),
                        enroll.getGrade()
                });
            }
        });

        // Clear search button logic
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadEnrollments();
        });

        // popup menu
        JPopupMenu popup = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem updateItem = new JMenuItem("Update");
        popup.add(deleteItem);
        popup.add(updateItem);

        enrollTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = enrollTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    enrollTable.getSelectionModel().setSelectionInterval(row, row);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popup.show(enrollTable, e.getX(), e.getY());
                    }
                }
            }
        });

        deleteItem.addActionListener(e -> {
            int row = enrollTable.getSelectedRow();
            if (row >= 0) {
                int id = (int) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this enrollment?", "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = enrollmentDAO.deleteEnrollment(id);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadEnrollments();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        updateItem.addActionListener(e -> {
            int row = enrollTable.getSelectedRow();
            if (row >= 0) {
                enrollIdField.setText(tableModel.getValueAt(row, 0).toString());
                studentIdField.setText(tableModel.getValueAt(row, 1).toString());
                courseIdField.setText(tableModel.getValueAt(row, 2).toString());
                enrollDateField.setText(tableModel.getValueAt(row, 3).toString());
                gradeField.setText(tableModel.getValueAt(row, 4).toString());
                enrollIdField.setEditable(false);
            }
        });
        loadEnrollments();
        setVisible(true);
    }

    private void loadEnrollments() {
        tableModel.setRowCount(0);
        for (Enrollment enroll : enrollmentDAO.getAllEnrollments()) {
            tableModel.addRow(new Object[]{
                enroll.getEnrollId(),
                enroll.getStudentId(),
                enroll.getCourseId(),
                enroll.getEnrollDate(),
                enroll.getGrade()
            });
        }
    }

    private void clearFields() {
        enrollIdField.setText("");
        studentIdField.setText("");
        courseIdField.setText("");
        enrollDateField.setText("");
        gradeField.setText("");
        // allow entering new record id not editable (DB will generate), reset editable
        // state
        enrollIdField.setEditable(true);
    }
}