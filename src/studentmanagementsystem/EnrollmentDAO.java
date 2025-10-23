package StudentManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class EnrollmentDAO {

    private Connection getConnection() throws SQLException {
        return DBConnection.getConnection(); // uses your project's DB helper
    }

    // Insert without enroll_id and capture generated key
    public boolean addEnrollment(Enrollment enroll) {
        String sql = "INSERT INTO enrollment (student_id, course_id, enroll_date, grade) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setInt(1, enroll.getStudentId());
            pstmt.setInt(2, enroll.getCourseId());
            if (enroll.getEnrollDate() == null || enroll.getEnrollDate().isEmpty()) {
                pstmt.setNull(3, Types.DATE);
            } else {
                pstmt.setString(3, enroll.getEnrollDate());
            }
            pstmt.setString(4, enroll.getGrade());

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        enroll.setEnrollId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error adding enrollment: " + e.getMessage());
        }
        return false;
    }

    public boolean updateEnrollment(Enrollment enroll) {
        String sql = "UPDATE enrollment SET student_id = ?, course_id = ?, enroll_date = ?, grade = ? WHERE enroll_id = ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enroll.getStudentId());
            pstmt.setInt(2, enroll.getCourseId());
            pstmt.setString(3, enroll.getEnrollDate());
            pstmt.setString(4, enroll.getGrade());
            pstmt.setInt(5, enroll.getEnrollId());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error updating enrollment: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEnrollment(int enrollId) {
        String sql = "DELETE FROM enrollment WHERE enroll_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, enrollId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting enrollment: " + e.getMessage());
            return false;
        }
    }

    public List<Enrollment> getAllEnrollments() {
        List<Enrollment> enrollments = new ArrayList<>();
        String sql = "SELECT * FROM enrollment";
        try (Connection conn = getConnection();
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

    public List<Enrollment> searchEnrollments(String keyword) {
        List<Enrollment> list = new ArrayList<>();
        String sql = "SELECT * FROM enrollment WHERE CAST(enroll_id AS CHAR) LIKE ? OR CAST(student_id AS CHAR) LIKE ? OR CAST(course_id AS CHAR) LIKE ? OR enroll_date LIKE ? OR grade LIKE ?";
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            String q = "%" + keyword + "%";
            for (int i = 1; i <= 5; i++)
                pstmt.setString(i, q);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Enrollment e = new Enrollment(
                            rs.getInt("enroll_id"),
                            rs.getInt("student_id"),
                            rs.getInt("course_id"),
                            rs.getString("enroll_date"),
                            rs.getString("grade"));
                    list.add(e);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error searching enrollments: " + e.getMessage());
        }
        return list;
    }
}