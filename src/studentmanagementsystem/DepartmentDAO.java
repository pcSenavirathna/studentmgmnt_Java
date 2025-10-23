package StudentManagementSystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;

public class DepartmentDAO {
    public boolean addDepartment(Department dept) {
        String sql = "INSERT INTO department (dept_id, dept_name, building, phone, head_of_dept) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dept.getDeptId());
            pstmt.setString(2, dept.getDeptName());
            pstmt.setString(3, dept.getBuilding());
            pstmt.setString(4, dept.getPhone());
            pstmt.setString(5, dept.getHead());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error adding department: " + e.getMessage());
            return false;
        }
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
                    rs.getString("head_of_dept")
                );
                departments.add(dept);
            }
        } catch (SQLException e) {
            System.out.println("Error getting departments: " + e.getMessage());
        }
        return departments;
    }
}