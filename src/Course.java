public class Course {
    private String id;
    private double creditHours;
    private String name, department, section;
    private String assignedProfName;

    public Course(String id, String name, double cr, String dept, String section) {
        this.id = id;
        this.name = name;
        this.creditHours = cr;
        this.department = dept;
        this.section = section;
        this.assignedProfName = "NONE";
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getCredits() { return creditHours; }
    public String getDepartment() { return department; }
    public String getSection() { return section; }

    public void setProfessor(String profName) { this.assignedProfName = profName; }
    public String getProfessor() { return assignedProfName; }

    @Override
    public String toString() {
        return String.format("%s (%s)", name, section);
    }

    public Object[] toRow() {
        return new Object[]{id, name, section, creditHours, department, assignedProfName};
    }
}