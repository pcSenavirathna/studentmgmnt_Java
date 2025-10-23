package StudentManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class StudentDAO {
    
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
                    rs.getString("nic"),         // <-- Add this line for NIC
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
                    rs.getString("nic"),         // <-- Add this line for NIC
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
    
    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM student WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, studentId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return false;
        }
    }
    
    public boolean updateStudent(Student student) {
        String sql = "UPDATE student SET name=?, nic=?, dob=?, gender=?, email=?, dept_id=? WHERE student_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, student.getName());
            pstmt.setString(2, student.getNic());
            pstmt.setString(3, student.getDob());
            pstmt.setString(4, student.getGender());
            pstmt.setString(5, student.getEmail());
            pstmt.setInt(6, student.getDeptId());
            pstmt.setInt(7, student.getStudentId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return false;
        }
    }
    
    public List<Student> searchStudents(String keyword) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM student WHERE student_id LIKE ? OR name LIKE ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + keyword + "%");
            pstmt.setString(2, "%" + keyword + "%");
            ResultSet rs = pstmt.executeQuery();
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
            System.out.println("Error searching students: " + e.getMessage());
        }
        return students;
    }
}