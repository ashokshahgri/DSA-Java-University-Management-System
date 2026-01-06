public class Student extends Person {
    private int age; // Changed to int
    private String email;
    private String department;

    public Student(String id, String name, int age, String email, String dept) {
        super(id, name);
        this.age = age;
        this.email = email;
        this.department = dept;
    }

    public int getAge() { return age; } // Returns int
    public String getEmail() { return email; }
    public String getDepartment() { return department; }

    @Override
    public String toString() {
        return String.format("ID: %s | NAME: %s | DEPT: %s", id, name, department);
    }

    public Object[] toRow() { return new Object[]{id, name, age, email, department}; }
}