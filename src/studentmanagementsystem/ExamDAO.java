package StudentManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class ExamDAO {

    public List<Exam> getAllExams() {
        List<Exam> list = new ArrayList<>();
        String sql = "SELECT exam_id, student_id, course_id, exam_date, grade FROM exam ORDER BY exam_date DESC, exam_id";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Exam e = new Exam(
                        rs.getString("exam_id"),
                        rs.getInt("student_id"),
                        rs.getInt("course_id"),
                        rs.getString("exam_date"),
                        rs.getString("grade"));
                list.add(e);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public boolean addExam(Exam exam) {
        String sql = "INSERT INTO exam (exam_id, student_id, course_id, exam_date, grade) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, exam.getExamId());
            ps.setInt(2, exam.getStudentId());
            ps.setInt(3, exam.getCourseId());
            ps.setString(4, exam.getExamDate());
            ps.setString(5, exam.getGrade());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateExam(Exam exam) {
        String sql = "UPDATE exam SET student_id = ?, course_id = ?, exam_date = ?, grade = ? WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, exam.getStudentId());
            ps.setInt(2, exam.getCourseId());
            ps.setString(3, exam.getExamDate());
            ps.setString(4, exam.getGrade());
            ps.setString(5, exam.getExamId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteExam(String examId) {
        String sql = "DELETE FROM exam WHERE exam_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, examId);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Exam> searchExams(String keyword) {
        List<Exam> list = new ArrayList<>();
        String like = "%" + keyword + "%";
        String sql = "SELECT exam_id, student_id, course_id, exam_date, grade FROM exam WHERE " +
                "exam_id LIKE ? OR CONCAT(student_id, '') LIKE ? OR CONCAT(course_id, '') LIKE ? OR exam_date LIKE ? OR grade LIKE ? "
                +
                "ORDER BY exam_date DESC, exam_id";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i = 1; i <= 5; i++)
                ps.setString(i, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Exam e = new Exam(
                                    rs.getString("exam_id"),
                            rs.getInt("student_id"),
                            rs.getInt("course_id"),
                            rs.getString("exam_date"),
                            rs.getString("grade"));
                    list.add(e);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }
}