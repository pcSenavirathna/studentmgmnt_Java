package StudentManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class EnrollmentDAO {
    public boolean addEnrollment(Enrollment enroll) {
        String sql = "INSERT INTO enrollment (enroll_id, student_id, course_id, enroll_date, grade) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enroll.getEnrollId());
            pstmt.setInt(2, enroll.getStudentId());
            pstmt.setInt(3, enroll.getCourseId());
            pstmt.setString(4, enroll.getEnrollDate());
            pstmt.setString(5, enroll.getGrade());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding enrollment: " + e.getMessage());
            return false;
        }
    }

    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollment";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Enrollment enroll = new Enrollment(
                    rs.getInt("enroll_id"),
                    rs.getInt("student_id"),
                    rs.getInt("course_id"),
                    rs.getString("enroll_date"),
                    rs.getString("grade")
                );
                enrollments.add(enroll);
            }
        } catch (SQLException e) {
            System.out.println("Error getting enrollments: " + e.getMessage());
        }
        return enrollments;
    }

    // Add update and delete methods as needed
}