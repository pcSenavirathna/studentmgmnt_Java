package StudentManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CoursePageFrame extends JFrame {
    private JTextField courseIdField, courseNameField, creditsField, semesterField;
    private JComboBox<Department> deptCombo;
    private DefaultTableModel tableModel;
    private CourseDAO courseDAO = new CourseDAO();
    private DepartmentDAO departmentDAO = new DepartmentDAO();

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
        backButton.setBounds(10, 10, 30, 30);
        backButton.setBackground(new Color(255, 0, 0));
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setIcon(new ImageIcon(
                new ImageIcon("src/images/back_arrow.png").getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
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
        semesterField = new JTextField();

        // Department combo populated from DB (stores Department objects, shows "id -
        // name")
        deptCombo = new JComboBox<>();
        java.util.List<Department> deps = departmentDAO.getAllDepartments();
        for (Department d : deps)
            deptCombo.addItem(d);
        deptCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Department) {
                    Department dep = (Department) value;
                    setText(dep.getDeptId() + " - " + dep.getDeptName());
                } else {
                    setText(value == null ? "" : value.toString());
                }
                return this;
            }
        });
        deptCombo.setMaximumRowCount(10); // vertical popup size

        String[] labels = {"Course ID", "Course Name", "Credits", "Department ID", "Semester"};
        JTextField[] fields = { courseIdField, courseNameField, creditsField, null, semesterField };
        int y = 30;
        for (int i = 0; i < labels.length; i++) {
            JLabel lbl = new JLabel(labels[i], SwingConstants.LEFT);
            lbl.setBounds(20, y, 150, 35);
            lbl.setOpaque(true);
            lbl.setBackground(new Color(70, 130, 180));
            lbl.setForeground(Color.WHITE);
            lbl.setFont(new Font("Arial", Font.BOLD, 14));
            lbl.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 0));
            formPanel.add(lbl);

            if (labels[i].equals("Department ID")) {
                deptCombo.setBounds(180, y, 210, 35);
                formPanel.add(deptCombo);
            } else {
                fields[i].setBounds(180, y, 210, 35);
                fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
                formPanel.add(fields[i]);
            }
            y += 45;
        }

        // Action buttons
        JButton addBtn = new JButton("Add");
        addBtn.setBounds(50, 300, 90, 40);
        addBtn.setBackground(new Color(25, 25, 112));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(addBtn);

        JButton updateBtn = new JButton("Update");
        updateBtn.setBounds(160, 300, 90, 40);
        updateBtn.setBackground(new Color(25, 25, 112));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFont(new Font("Arial", Font.BOLD, 14));
        formPanel.add(updateBtn);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setBounds(270, 300, 90, 40);
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
        add(tableScroll);

        // ---------- Add popup menu for right-click row actions ----------
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem updateItem = new JMenuItem("Update");
        popupMenu.add(deleteItem);
        popupMenu.add(updateItem);

        courseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = courseTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    courseTable.getSelectionModel().setSelectionInterval(row, row);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.show(courseTable, e.getX(), e.getY());
                    }
                }
            }
        });

        // Delete action - calls CourseDAO.deleteCourse(int id)
        deleteItem.addActionListener(e -> {
            int row = courseTable.getSelectedRow();
            if (row >= 0) {
                int courseId = (int) tableModel.getValueAt(row, 0); // adjust column index if needed
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this course?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = courseDAO.deleteCourse(courseId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Course deleted successfully!", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        loadCourses();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete course!", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Update action - populate form fields for editing
        updateItem.addActionListener(e -> {
            int row = courseTable.getSelectedRow();
            if (row >= 0) {
                courseIdField.setText(tableModel.getValueAt(row, 0).toString());
                courseNameField.setText(tableModel.getValueAt(row, 1).toString());
                creditsField.setText(tableModel.getValueAt(row, 2).toString());
                semesterField.setText(tableModel.getValueAt(row, 4).toString());
                try {
                    int did = Integer.parseInt(tableModel.getValueAt(row, 3).toString());
                    boolean found = false;
                    for (int i = 0; i < deptCombo.getItemCount(); i++) {
                        Department d = deptCombo.getItemAt(i);
                        if (d != null && d.getDeptId() == did) {
                            deptCombo.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        deptCombo.setSelectedIndex(-1);
                } catch (Exception ex) {
                    deptCombo.setSelectedIndex(-1);
                }
                courseIdField.setEditable(false);
            }
        });

        // Add components to frame
        add(topPanel);
        add(formPanel);
        add(searchByLabel);
        add(searchField);
        add(searchBtn);
        add(clearSearchBtn);

        // Add button logic
        addBtn.addActionListener(e -> {
            try {
                int courseId = Integer.parseInt(courseIdField.getText().trim());
                String courseName = courseNameField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                Department selDept = (Department) deptCombo.getSelectedItem();
                if (selDept == null) {
                    JOptionPane.showMessageDialog(this, "Please select a Department.", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }
                int deptId = selDept.getDeptId();
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
            try {
                int courseId = Integer.parseInt(courseIdField.getText().trim());
                String courseName = courseNameField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                int deptId = ((Department) deptCombo.getSelectedItem()).getDeptId();
                String semester = semesterField.getText().trim();

                if (courseName.isEmpty() || semester.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Course course = new Course(courseId, courseName, credits, deptId, semester);
                boolean success = courseDAO.updateCourse(course);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Course updated successfully!", "Success",
                            JOptionPane.INFORMATION_MESSAGE);
                    loadCourses();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update course!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "IDs and credits must be numbers.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Search button logic
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                loadCourses(courseDAO.searchCourses(keyword));
            }
        });

        // Clear search button logic
        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadCourses(courseDAO.getAllCourses());
        });

        clearBtn.addActionListener(e -> clearFields());

        loadCourses();
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

    // NEW: overload to accept a list (used by search)
    private void loadCourses(java.util.List<Course> courses) {
        tableModel.setRowCount(0);
        for (Course course : courses) {
            tableModel.addRow(new Object[] {
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
        semesterField.setText("");
        deptCombo.setSelectedIndex(-1);
    }
}