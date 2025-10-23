package StudentManagementSystem;

public class Course {
    private int courseId, credits, deptId;
    private String courseName, semester;

    public Course(int courseId, String courseName, int credits, int deptId, String semester) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credits = credits;
        this.deptId = deptId;
        this.semester = semester;
    }

    public int getCourseId() { return courseId; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public int getDeptId() { return deptId; }
    public String getSemester() { return semester; }
}