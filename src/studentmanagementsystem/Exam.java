package StudentManagementSystem;

public class Exam {
    private String examId;
    private int studentId;
    private int courseId;
    private String examDate;
    private String grade;

    public Exam(String examId, int studentId, int courseId, String examDate, String grade) {
        this.examId = examId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.examDate = examDate;
        this.grade = grade;
    }

    public String getExamId() {
        return examId;
    }
    public int getStudentId() { return studentId; }
    public int getCourseId() { return courseId; }

    public String getExamDate() {
        return examDate;
    }
    public String getGrade() { return grade; }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public void setExamDate(String examDate) {
        this.examDate = examDate;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}