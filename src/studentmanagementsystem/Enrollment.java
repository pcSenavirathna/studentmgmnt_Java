package StudentManagementSystem;

public class Enrollment {
    private int enrollId, studentId, courseId;
    private String enrollDate, grade;

    public Enrollment(int enrollId, int studentId, int courseId, String enrollDate, String grade) {
        this.enrollId = enrollId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.enrollDate = enrollDate;
        this.grade = grade;
    }

    public int getEnrollId() { return enrollId; }
    public int getStudentId() { return studentId; }
    public int getCourseId() { return courseId; }
    public String getEnrollDate() { return enrollDate; }
    public String getGrade() { return grade; }

    // NEW: allow DAO to set generated id
    public void setEnrollId(int enrollId) {
        this.enrollId = enrollId;
    }
}