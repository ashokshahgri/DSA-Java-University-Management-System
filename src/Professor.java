public class Professor extends Person {
    private String faculty;
    private String email;
    private int courseCount;
    private String assignedCourses;

    public Professor(String id, String name, String email, String faculty) {
        super(id, name);
        this.email = email;
        this.faculty = faculty;
        this.courseCount = 0;
        this.assignedCourses = "";
    }

    public String getEmail() { return email; }
    public String getFaculty() { return faculty; }

    public boolean canTakeMoreCourses() {
        return courseCount < 3;
    }

    public void assignToCourse(String courseName) {
        if (courseName.equals("TRUE") || courseName.equals("FALSE") || courseName.equals("NONE")) return;
        if (assignedCourses.contains(courseName)) return;

        if (courseCount < 3) {
            courseCount++;
            if (assignedCourses.isEmpty()) {
                assignedCourses = courseName;
            } else {
                assignedCourses += ", " + courseName;
            }
        }
    }

    public String getAssignedCourse() {
        return assignedCourses.isEmpty() ? "NONE" : assignedCourses;
    }

    @Override
    public String toString() {
        return name;
    }

    public Object[] toRow() {
        return new Object[]{id, name, email, faculty, getAssignedCourse()};
    }
}