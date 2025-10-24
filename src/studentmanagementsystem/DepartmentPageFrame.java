package StudentManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.sql.ResultSet;


public class DepartmentPageFrame extends JFrame {
    private JTextField deptIdField, deptNameField, buildingField, phoneField, headField;
    private DefaultTableModel tableModel;
    private DepartmentDAO departmentDAO = new DepartmentDAO();

    public DepartmentPageFrame() {
        setTitle("Student Information Management System - Department");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1060, 700);
        setLocationRelativeTo(null);
        setLayout(null);

        // Top bar with title and back arrow
        JPanel topPanel = new JPanel(null);
        topPanel.setBackground(new Color(70, 130, 180));
        topPanel.setBounds(0, 0, 1060, 60);

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
        deptIdField = new JTextField();
        deptNameField = new JTextField();
        buildingField = new JTextField();
        phoneField = new JTextField();
        headField = new JTextField();

        String[] labels = {"Department ID", "Department Name", "Building", "Phone", "Head Of Department"};
        JTextField[] fields = {deptIdField, deptNameField, buildingField, phoneField, headField};
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

            fields[i].setBounds(180, y, 210, 35);
            fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
            formPanel.add(fields[i]);
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
        String[] tableHeaders = {"Department ID", "Department Name", "Building", "Phone", "Head Of Department"};
        tableModel = new DefaultTableModel(tableHeaders, 0);
        JTable deptTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(deptTable);
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
            String idText = deptIdField.getText().trim();
            String deptName = deptNameField.getText().trim();
            String building = buildingField.getText().trim();
            String phone = phoneField.getText().trim();
            String head = headField.getText().trim();

            // Required checks
            if (idText.isEmpty() || deptName.isEmpty() || building.isEmpty() || phone.isEmpty() || head.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Numeric ID
            int deptId;
            try {
                deptId = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Department ID must be a number.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Phone digits check (basic)
            if (!phone.matches("[\\d\\-\\+\\s]{5,20}")) {
                JOptionPane.showMessageDialog(this, "Phone must contain digits (and optional +,-,space), length 5-20.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Department dept = new Department(deptId, deptName, building, phone, head);
            boolean success = departmentDAO.addDepartment(dept);

            if (success) {
                JOptionPane.showMessageDialog(this, "Department added successfully!", "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadDepartments();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add department!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // ---------- Add popup menu for right-click row actions ----------
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem updateItem = new JMenuItem("Update");
        popupMenu.add(deleteItem);
        popupMenu.add(updateItem);

        deptTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int row = deptTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    deptTable.getSelectionModel().setSelectionInterval(row, row);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popupMenu.show(deptTable, e.getX(), e.getY());
                    }
                }
            }
        });

        // Delete action
        deleteItem.addActionListener(e -> {
            int row = deptTable.getSelectedRow();
            if (row >= 0) {
                int deptId = (int) tableModel.getValueAt(row, 0); // adjust index if ID column differs
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this department?", "Confirm Delete",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = departmentDAO.deleteDepartment(deptId);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Department deleted", "Success",
                                JOptionPane.INFORMATION_MESSAGE);
                        loadDepartments();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Update action: populate form fields so user can edit and press Update button
        updateItem.addActionListener(e -> {
            int row = deptTable.getSelectedRow();
            if (row >= 0) {
                deptIdField.setText(tableModel.getValueAt(row, 0).toString());
                deptNameField.setText(tableModel.getValueAt(row, 1).toString());
                buildingField.setText(tableModel.getValueAt(row, 2).toString());
                phoneField.setText(tableModel.getValueAt(row, 3).toString());
                headField.setText(tableModel.getValueAt(row, 4).toString());
                deptIdField.setEditable(false); // prevent changing PK while editing
            }
        });

        // ---------- Wire existing Update button to save edits ----------
        // assume updateBtn is already created earlier in the frame
        updateBtn.addActionListener(ev -> {
            String idText = deptIdField.getText().trim();
            String name = deptNameField.getText().trim();
            String building = buildingField.getText().trim();
            String phone = phoneField.getText().trim();
            String head = headField.getText().trim();

            if (idText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Department ID is required for update.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id;
            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "ID must be numeric", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill required fields", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!phone.matches("[\\d\\-\\+\\s]{5,20}")) {
                JOptionPane.showMessageDialog(this, "Phone must contain digits (and optional +,-,space), length 5-20.",
                        "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Department d = new Department(id, name, building, phone, head);
            boolean ok = departmentDAO.updateDepartment(d);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Department updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDepartments();
                clearFields();
                deptIdField.setEditable(true);
            } else {
                JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Search button logic
        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            if (!keyword.isEmpty()) {
                List<Department> results = departmentDAO.searchDepartments(keyword);
                loadDepartments(results);
            }
        });

        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadDepartments();
        });

        clearBtn.addActionListener(e -> clearFields());

        loadDepartments();
        setVisible(true);
    }

    private void loadDepartments() {
        tableModel.setRowCount(0);
        for (Department dept : departmentDAO.getAllDepartments()) {
            tableModel.addRow(new Object[]{
                dept.getDeptId(),
                dept.getDeptName(),
                dept.getBuilding(),
                dept.getPhone(),
                dept.getHead()
            });
        }
    }

    // overload used by search
    private void loadDepartments(List<Department> list) {
        tableModel.setRowCount(0);
        for (Department d : list) {
            tableModel.addRow(
                    new Object[] { d.getDeptId(), d.getDeptName(), d.getBuilding(), d.getPhone(), d.getHead() });
        }
    }

    private void clearFields() {
        deptIdField.setText("");
        deptNameField.setText("");
        buildingField.setText("");
        phoneField.setText("");
        headField.setText("");
        deptIdField.setEditable(true);
    }
    
    public List<Department> getAllDepartments() {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM department";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Department dept = new Department(
                    rs.getInt("dept_id"),
                    rs.getString("dept_name"),
                    rs.getString("building"),
                    rs.getString("phone"),
                    rs.getString("head")
                );
                departments.add(dept);
            }
        } catch (SQLException e) {
            System.out.println("Error getting departments: " + e.getMessage());
        }
        return departments;
    }
}