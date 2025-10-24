package StudentManagementSystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;

public class ExamePageFrame extends JFrame {
    private JTextField examIdField, examDateField, gradeField;
    private JComboBox<Student> studentCombo;
    private JComboBox<Course> courseCombo;
    private DefaultTableModel tableModel;
    private ExamDAO examDAO = new ExamDAO();
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();

    public ExamePageFrame() {
        setTitle("Student Information Management System - Exams");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1070, 600);
        setLocationRelativeTo(null);
        setLayout(null);

        // Top bar
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
        JLabel titleLabel = new JLabel("Student Information Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBounds(60, 10, 950, 40);
        topPanel.add(backButton);
        topPanel.add(titleLabel);

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(null);
        formPanel.setBackground(new Color(210, 225, 245));
        formPanel.setBounds(0, 60, 420, 500);

        examIdField = new JTextField();
        examIdField.setToolTipText("Exam ID (primary key)");
        examDateField = new JTextField();
        examDateField.setEditable(false);
        examDateField.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        examDateField.setToolTipText("Click to select date");
        examDateField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Window win = SwingUtilities.getWindowAncestor(examDateField);
                Frame owner = (win instanceof Frame) ? (Frame) win : null;
                Date initial = null;
                String txt = examDateField.getText().trim();
                if (!txt.isEmpty()) {
                    try {
                        initial = new SimpleDateFormat("yyyy-MM-dd").parse(txt);
                    } catch (ParseException ex) {
                        initial = new Date();
                    }
                } else {
                    initial = new Date();
                }
                DatePickerDialog picker = new DatePickerDialog(owner, initial);
                picker.setLocationRelativeTo(examDateField);
                picker.setVisible(true);
                Date picked = picker.getSelectedDate();
                if (picked != null) {
                    examDateField.setText(new SimpleDateFormat("yyyy-MM-dd").format(picked));
                }
            }
        });

        gradeField = new JTextField();

        // Student combo
        studentCombo = new JComboBox<>();
        for (Student s : studentDAO.getAllStudents())
            studentCombo.addItem(s);
        studentCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Student) {
                    Student st = (Student) value;
                    setText(st.getStudentId() + " - " + st.getName());
                } else {
                    setText(value == null ? "" : value.toString());
                }
                return this;
            }
        });
        studentCombo.setMaximumRowCount(10);

        // Course combo
        courseCombo = new JComboBox<>();
        for (Course c : courseDAO.getAllCourses())
            courseCombo.addItem(c);
        courseCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Course) {
                    Course co = (Course) value;
                    setText(co.getCourseId() + " - " + co.getCourseName());
                } else {
                    setText(value == null ? "" : value.toString());
                }
                return this;
            }
        });
        courseCombo.setMaximumRowCount(10);

        String[] labels = { "Exam ID", "Student ID", "Course ID", "Exam Date", "Grade" };
        JTextField[] fields = { examIdField, null, null, examDateField, gradeField };
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

            if (labels[i].equals("Student ID")) {
                studentCombo.setBounds(180, y, 210, 35);
                formPanel.add(studentCombo);
            } else if (labels[i].equals("Course ID")) {
                courseCombo.setBounds(180, y, 210, 35);
                formPanel.add(courseCombo);
            } else {
                fields[i].setBounds(180, y, 210, 35);
                fields[i].setFont(new Font("Arial", Font.PLAIN, 14));
                formPanel.add(fields[i]);
            }
            y += 45;
        }

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

        // Search
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
        String[] tableHeaders = { "Exam ID", "Student ID", "Course ID", "Exam Date", "Grade" };
        tableModel = new DefaultTableModel(tableHeaders, 0);
        JTable examTable = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(examTable);
        tableScroll.setBounds(450, 110, 590, 450);

        add(topPanel);
        add(formPanel);
        add(searchByLabel);
        add(searchField);
        add(searchBtn);
        add(clearSearchBtn);
        add(tableScroll);

        // Actions
        addBtn.addActionListener(e -> {
            String examId = examIdField.getText().trim();
            if (examId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Exam ID is required.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student selStudent = (Student) studentCombo.getSelectedItem();
            Course selCourse = (Course) courseCombo.getSelectedItem();
            if (selStudent == null || selCourse == null) {
                JOptionPane.showMessageDialog(this, "Select student and course.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String examDate = examDateField.getText().trim();
            String grade = gradeField.getText().trim();
            if (examDate.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!examDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Exam Date must be in YYYY-MM-DD format.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int studentId = selStudent.getStudentId();
            int courseId = selCourse.getCourseId();
            Exam exam = new Exam(examId, studentId, courseId, examDate, grade);
            boolean ok = examDAO.addExam(exam);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Exam added: " + examId, "Success",
                        JOptionPane.INFORMATION_MESSAGE);
                loadExams();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add exam", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        updateBtn.addActionListener(e -> {
            String examId = examIdField.getText().trim();
            if (examId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Select an exam ID to update.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            Student selStudent = (Student) studentCombo.getSelectedItem();
            Course selCourse = (Course) courseCombo.getSelectedItem();
            if (selStudent == null || selCourse == null) {
                JOptionPane.showMessageDialog(this, "Select student and course.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            String examDate = examDateField.getText().trim();
            String grade = gradeField.getText().trim();
            if (examDate.isEmpty() || grade.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!examDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                JOptionPane.showMessageDialog(this, "Exam Date must be in YYYY-MM-DD format.", "Input Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
            int studentId = selStudent.getStudentId();
            int courseId = selCourse.getCourseId();
            Exam exam = new Exam(examId, studentId, courseId, examDate, grade);
            boolean ok = examDAO.updateExam(exam);
            if (ok) {
                JOptionPane.showMessageDialog(this, "Exam updated", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadExams();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        clearBtn.addActionListener(e -> clearFields());

        searchBtn.addActionListener(e -> {
            String keyword = searchField.getText().trim();
            tableModel.setRowCount(0);
            List<Exam> results = examDAO.searchExams(keyword);
            for (Exam ex : results) {
                tableModel.addRow(new Object[] {
                        ex.getExamId(),
                        ex.getStudentId(),
                        ex.getCourseId(),
                        ex.getExamDate(),
                        ex.getGrade()
                });
            }
        });

        clearSearchBtn.addActionListener(e -> {
            searchField.setText("");
            loadExams();
        });

        // popup menu
        JPopupMenu popup = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem editItem = new JMenuItem("Edit");
        popup.add(deleteItem);
        popup.add(editItem);

        examTable.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                int row = examTable.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    examTable.getSelectionModel().setSelectionInterval(row, row);
                    if (SwingUtilities.isRightMouseButton(e)) {
                        popup.show(examTable, e.getX(), e.getY());
                    }
                }
            }
        });

        deleteItem.addActionListener(e -> {
            int row = examTable.getSelectedRow();
            if (row >= 0) {
                String id = tableModel.getValueAt(row, 0).toString();
                int confirm = JOptionPane.showConfirmDialog(this, "Delete this exam?", "Confirm",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean ok = examDAO.deleteExam(id);
                    if (ok) {
                        JOptionPane.showMessageDialog(this, "Deleted", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadExams();
                        clearFields();
                    } else {
                        JOptionPane.showMessageDialog(this, "Delete failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        editItem.addActionListener(e -> {
            int row = examTable.getSelectedRow();
            if (row >= 0) {
                examIdField.setText(tableModel.getValueAt(row, 0).toString());
                try {
                    int sid = Integer.parseInt(tableModel.getValueAt(row, 1).toString());
                    boolean found = false;
                    for (int i = 0; i < studentCombo.getItemCount(); i++) {
                        Student s = studentCombo.getItemAt(i);
                        if (s != null && s.getStudentId() == sid) {
                            studentCombo.setSelectedIndex(i);
                            found = true;
                            break;
                        }
                    }
                    if (!found)
                        studentCombo.setSelectedIndex(-1);
                } catch (Exception ex) {
                    studentCombo.setSelectedIndex(-1);
                }
                try {
                    int cid = Integer.parseInt(tableModel.getValueAt(row, 2).toString());
                    boolean found2 = false;
                    for (int i = 0; i < courseCombo.getItemCount(); i++) {
                        Course c = courseCombo.getItemAt(i);
                        if (c != null && c.getCourseId() == cid) {
                            courseCombo.setSelectedIndex(i);
                            found2 = true;
                            break;
                        }
                    }
                    if (!found2)
                        courseCombo.setSelectedIndex(-1);
                } catch (Exception ex) {
                    courseCombo.setSelectedIndex(-1);
                }
                examDateField.setText(tableModel.getValueAt(row, 3).toString());
                gradeField.setText(tableModel.getValueAt(row, 4).toString());
            }
        });

        loadExams();
        setVisible(true);
    }

    private void loadExams() {
        tableModel.setRowCount(0);
        List<Exam> all = examDAO.getAllExams();
        for (Exam ex : all) {
            tableModel.addRow(new Object[]{
                    ex.getExamId(),
                    ex.getStudentId(),
                    ex.getCourseId(),
                    ex.getExamDate(),
                    ex.getGrade()
            });
        }
    }

    private void clearFields() {
        examIdField.setText("");
        studentCombo.setSelectedIndex(-1);
        courseCombo.setSelectedIndex(-1);
        examDateField.setText("");
        gradeField.setText("");
    }
}