package StudentManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class StudentPageFrame extends JFrame {
    // Form fields
    private JTextField studentIdField, nameField, nicField, emailField, deptIdField;
    private JSpinner dobSpinner;
    private JComboBox<String> genderCombo;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO = new StudentDAO();
    private JTextField searchField;
    private JButton searchBtn, clearSearchBtn;

    public StudentPageFrame() {
        setTitle("Student Information Management System - Student");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setLocationRelativeTo(null);
        setLayout(null);

        // Top bar with title and back arrow
        JPanel topPanel = new JPanel(null);
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBounds(0, 0, 1050, 60);

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

        JLabel titleLabel = new JLabel("Student Information Management System", JLabel.CENTER);
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

        // Create fields
        studentIdField = new JTextField();
        nameField = new JTextField();
        nicField = new JTextField();
        dobSpinner = new JSpinner(new SpinnerDateModel());
        dobSpinner.setEditor(new JSpinner.DateEditor(dobSpinner, "yyyy-MM-dd"));
        genderCombo = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        emailField = new JTextField();
        deptIdField = new JTextField();


        String[] labels = {"Student ID", "Name", "NIC", "DOB", "Gender", "Email", "Department ID"};
        JTextField[] fields = {studentIdField, nameField, nicField, null, null, emailField, deptIdField};
        int y = 30;
        for (int i = 0; i < labels.length; i++) {
            JButton lblBtn = new JButton(labels[i]);
            lblBtn.setBounds(20, y, 120, 35);
            lblBtn.setBackground(new Color(70, 130, 180));
            lblBtn.setForeground(Color.WHITE);
            lblBtn.setFont(new Font("Arial", Font.BOLD, 14));
            lblBtn.setFocusPainted(false);
            lblBtn.setEnabled(false);
            formPanel.add(lblBtn);

            if (labels[i].equals("Gender")) {
                genderCombo.setBounds(150, y, 220, 35);
                formPanel.add(genderCombo);
            } else if (labels[i].equals("DOB")) {
                dobSpinner.setBounds(150, y, 220, 35);
                formPanel.add(dobSpinner);
            } else if (fields[i] != null) {
                fields[i].setBounds(150, y, 220, 35);
                fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
                formPanel.add(fields[i]);
            }
            y += 45;
        }

        // Action buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(20, 370, 90, 40);
        addBtn.setBackground(new Color(25, 25, 112));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(120, 370, 90, 40);
        updateBtn.setBackground(new Color(25, 25, 112));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(updateBtn);

//        JButton deleteBtn = new JButton("Delete");
//        deleteBtn.setBounds(220, 370, 90, 40);
//        deleteBtn.setBackground(new Color(25, 25, 112));
//        deleteBtn.setForeground(Color.WHITE);
//        deleteBtn.setFont(new Font("Arial", Font.BOLD, 14));
//        formPanel.add(deleteBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(220, 370, 90, 40);
        clearBtn.setBackground(new Color(25, 25, 112));
        clearBtn.setForeground(Color.WHITE);
        clearBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(clearBtn);

        // Search bar
        JLabel searchByLabel = new JLabel("Search By");
        searchByLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchByLabel.setBounds(450, 70, 80, 30);

        searchField = new JTextField();
        searchField.setBounds(540, 70, 200, 30);
        add(searchField);

        searchBtn = new JButton("Search");
        searchBtn.setBounds(750, 70, 90, 30);
        searchBtn.setBackground(new Color(25, 25, 112));
        searchBtn.setForeground(Color.WHITE);
        add(searchBtn);

        clearSearchBtn = new JButton("Clear");
        clearSearchBtn.setBounds(850, 70, 90, 30);
        clearSearchBtn.setBackground(new Color(25, 25, 112));
        clearSearchBtn.setForeground(Color.WHITE);
        add(clearSearchBtn);

        // Table
        String[] tableHeaders = {"Student ID", "Name", "NIC", "DOB", "Gender", "Email", "Department ID"};
        tableModel = new DefaultTableModel(tableHeaders, 0);
        JTable studentTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(studentTable);
        tableScroll.setBounds(450, 110, 490, 450);

        // Popup menu for table
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem updateItem = new JMenuItem("Update");
        popupMenu.add(deleteItem);
        popupMenu.add(updateItem);

        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = studentTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    studentTable.getSelectionModel().setSelectionInterval(row, row);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.show(studentTable, e.getX(), e.getY());
                    }
                }
            }
        });

        // Delete action
        deleteItem.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                int studentId = (int) tableModel.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = studentDAO.deleteStudent(studentId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Student deleted successfully!");
                        loadStudents();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete student!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Update action (fills text fields for editing)
        updateItem.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if (row >= 0) {
                studentIdField.setText(tableModel.getValueAt(row, 0).toString());
                nameField.setText(tableModel.getValueAt(row, 1).toString());
                nicField.setText(tableModel.getValueAt(row, 2).toString());
                try {
                    Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(tableModel.getValueAt(row, 3).toString());
                    dobSpinner.setValue(parsed);
                } catch (ParseException ex) {
                    dobSpinner.setValue(new Date());
                }
                genderCombo.setSelectedItem(tableModel.getValueAt(row, 4).toString());
                emailField.setText(tableModel.getValueAt(row, 5).toString());
                deptIdField.setText(tableModel.getValueAt(row, 6).toString());
            }
        });

        // Add components to frame
        add(topPanel);
        add(formPanel);
        add(searchByLabel);
        add(searchField);
        add(searchBtn);
        add(clearSearchBtn);
        add(tableScroll);

        // Load students initially
        loadStudents();

        // Add button logic
        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String nic = nicField.getText().trim(); // <-- Add this line
            String dob;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dob = sdf.format((Date) dobSpinner.getValue());
            String gender = (String) genderCombo.getSelectedItem();
            String email = emailField.getText().trim();
            int deptId;
            try {
                deptId = Integer.parseInt(deptIdField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Department ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (name.isEmpty() || nic.isEmpty() || dob.isEmpty() || gender.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "DOB must be in YYYY-MM-DD format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(0, name, nic, email, dob, gender, deptId); // <-- Pass NIC here
            boolean success = studentDAO.addStudent(student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add student!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Update button logic
        updateBtn.addActionListener(e -> {
            int studentId;
            try {
                studentId = Integer.parseInt(studentIdField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Student ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String name = nameField.getText().trim();
            String nic = nicField.getText().trim();
            String dob = new SimpleDateFormat("yyyy-MM-dd").format((Date) dobSpinner.getValue());
            String gender = (String) genderCombo.getSelectedItem();
            String email = emailField.getText().trim();
            int deptId;
            try {
                deptId = Integer.parseInt(deptIdField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Department ID must be a number.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (name.isEmpty() || nic.isEmpty() || dob.isEmpty() || gender.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!dob.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "DOB must be in YYYY-MM-DD format.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student student = new Student(studentId, name, nic, email, dob, gender, deptId);
            boolean success = studentDAO.updateStudent(student);
            if (success) {
                JOptionPane.showMessageDialog(this, "Student updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadStudents();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to update student!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Clear button logic
        clearBtn.addActionListener(e -> clearFields());

        // Search button logic
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                loadStudents(studentDAO.searchStudents(keyword));
            }
        });

        // Clear search button logic
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadStudents(studentDAO.getAllStudents());
        });

        setVisible(true);
    }

    private void loadStudents() {
        tableModel.setRowCount(0);
        List<Student> students = studentDAO.getAllStudents();
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getStudentId(),
                s.getName(),
                s.getNic(), // <-- Show NIC value here
                s.getDob(),
                s.getGender(),
                s.getEmail(),
                s.getDeptId()
            });
        }
    }

    private void loadStudents(List<Student> students) {
        tableModel.setRowCount(0);
        for (Student s : students) {
            tableModel.addRow(new Object[]{
                s.getStudentId(),
                s.getName(),
                s.getNic(),
                s.getDob(),
                s.getGender(),
                s.getEmail(),
                s.getDeptId()
            });
        }
    }

    private void clearFields() {
        studentIdField.setText("");
        nameField.setText("");
        nicField.setText("");
        dobSpinner.setValue(new Date());
        genderCombo.setSelectedIndex(0);
        emailField.setText("");
        deptIdField.setText("");
    }
}