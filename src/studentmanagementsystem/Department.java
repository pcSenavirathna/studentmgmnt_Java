package StudentManagementSystem;

public class Department {
    private int deptId;
    private String deptName;
    private String building;
    private String phone;
    private String head;

    public Department(int deptId, String deptName, String building, String phone, String head) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.building = building;
        this.phone = phone;
        this.head = head;
    }

    public int getDeptId() { return deptId; }
    public String getDeptName() { return deptName; }
    public String getBuilding() { return building; }
    public String getPhone() { return phone; }
    public String getHead() { return head; }
    
    public void displayInfo() {
        System.out.printf("Dept ID: %d | Name: %s | Building: %s | Head: %s%n", deptId, deptName, building, head);
    }
}