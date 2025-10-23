package StudentManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class CourseDAO {
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO course (course_id, course_name, credits, dept_id, semester) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, course.getCourseId());
            pstmt.setString(2, course.getCourseName());
            pstmt.setInt(3, course.getCredits());
            pstmt.setInt(4, course.getDeptId());
            pstmt.setString(5, course.getSemester());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding course: " + e.getMessage());
            return false;
        }
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        String sql = "SELECT * FROM course";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Course course = new Course(
                    rs.getInt("course_id"),
                    rs.getString("course_name"),
                    rs.getInt("credits"),
                    rs.getInt("dept_id"),
                    rs.getString("semester")
                );
                courses.add(course);
            }
        } catch (SQLException e) {
            System.out.println("Error getting courses: " + e.getMessage());
        }
        return courses;
    }

    // Add update and delete methods as needed
}