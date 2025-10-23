package StudentManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Student {
    private int studentId;
    private String name;
    private String dob;
    private String gender;
    private String email;
    private int deptId;
    private String nic;
    
    public Student(int studentId, String name, String nic, String email, String dob, String gender, int deptId) {
        this.studentId = studentId;
        this.name = name;
        this.nic = nic;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.deptId = deptId;
    }
    
    // Getters
    public int getStudentId() { return studentId; }
    public String getName() { return name; }
    public String getDob() { return dob; }
    public String getGender() { return gender; }
    public String getEmail() { return email; }
    public int getDeptId() { return deptId; }
    public String getNic() { return nic; }
    
    // Setters for update
    public void setName(String name) { this.name = name; }
    public void setDob(String dob) { this.dob = dob; }
    public void setGender(String gender) { this.gender = gender; }
    public void setEmail(String email) { this.email = email; }
    public void setDeptId(int deptId) { this.deptId = deptId; }
    public void setNic(String nic) { this.nic = nic; }
    
    public void displayInfo() {
        System.out.printf("ID: %d | Name: %s | Email: %s | DOB: %s | Gender: %s | NIC: %s%n", 
                         studentId, name, email, dob, gender, nic);
    }
    
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO student (name, nic, dob, gender, email, dept_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getNic());
            pstmt.setString(3, student.getDob());
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getEmail());
            pstmt.setInt(6, student.getDeptId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding student: " + e.getMessage());
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("nic"),
                    rs.getString("email"),
                    rs.getString("dob"),
                    rs.getString("gender"),
                    rs.getInt("dept_id")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            System.out.println("Error getting students: " + e.getMessage());
        }
        return students;
    }

    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM student WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Student(
                    rs.getInt("student_id"),
                    rs.getString("name"),
                    rs.getString("nic"),
                    rs.getString("email"),
                    rs.getString("dob"),
                    rs.getString("gender"),
                    rs.getInt("dept_id")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error getting student: " + e.getMessage());
        }
        return null;
    }
}