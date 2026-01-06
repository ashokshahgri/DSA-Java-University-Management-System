import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class UniversitySystemGUI extends JFrame {

    // Entity Lists
    private MyList<Student> students = new MyList<>();
    private MyList<Professor> professors = new MyList<>();
    private MyList<Course> courses = new MyList<>();
    private MyList<Enrollment> enrollments = new MyList<>();

    // === CLEAN WHITE PALETTE ===
    private final Color NAV_BAR_BG = new Color(245, 245, 245);
    private final Color CONTENT_BG = new Color(255, 255, 255);
    private final Color PRIMARY_BTN = new Color(0, 6, 65);
    private final Color ACCENT_COLOR = new Color(0, 90, 182);
    private final Color DANGER_COLOR = new Color(220, 53, 69);
    private final Color TEXT_COLOR = new Color(0, 0, 0);
    private final Color INPUT_BORDER = new Color(100, 100, 100);

    private final int INPUT_RADIUS = 15;

    // Navigation Buttons
    private RoundedButton btnStudentTab;
    private RoundedButton btnProfTab;
    private RoundedButton btnCourseTab;
    private RoundedButton btnAssignTab;
    private RoundedButton btnFilterTab;

    // Layout
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContentPanel = new JPanel(cardLayout);

    // Tables & Inputs
    private JTable tblStudent, tblProfessor, tblCourse;
    private DefaultTableModel modelStudent, modelProfessor, modelCourse;
    private JComboBox<String> cmbProfessorsForCourse;

    public UniversitySystemGUI() {
        setTitle("UNIVERSITY MANAGEMENT SYSTEM");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(CONTENT_BG);

        loadData();

        // --- 1. TOP NAVIGATION BAR ---
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        navPanel.setBackground(NAV_BAR_BG);

        btnStudentTab = new RoundedButton("STUDENTS", ACCENT_COLOR);
        btnProfTab = new RoundedButton("PROFESSORS", PRIMARY_BTN);
        btnCourseTab = new RoundedButton("COURSES", PRIMARY_BTN);
        btnAssignTab = new RoundedButton("ASSIGNMENTS", PRIMARY_BTN);
        btnFilterTab = new RoundedButton("FILTER & SEARCH", PRIMARY_BTN);

        navPanel.add(btnStudentTab);
        navPanel.add(btnProfTab);
        navPanel.add(btnCourseTab);
        navPanel.add(btnAssignTab);
        navPanel.add(btnFilterTab);

        add(navPanel, BorderLayout.NORTH);

        // --- 2. MAIN CONTENT AREA ---
        mainContentPanel.setBackground(CONTENT_BG);
        mainContentPanel.add(createStudentPanel(), "STUDENTS");
        mainContentPanel.add(createProfessorPanel(), "PROFESSORS");
        mainContentPanel.add(createCoursePanel(), "COURSES");
        mainContentPanel.add(createAssignmentPanel(), "ASSIGNMENTS");
        mainContentPanel.add(createFilterPanel(), "FILTER");

        add(mainContentPanel, BorderLayout.CENTER);

        // --- 3. NAVIGATION LOGIC ---
        btnStudentTab.addActionListener(e -> { cardLayout.show(mainContentPanel, "STUDENTS"); updateNavColors(btnStudentTab); });
        btnProfTab.addActionListener(e -> { cardLayout.show(mainContentPanel, "PROFESSORS"); updateNavColors(btnProfTab); });
        btnCourseTab.addActionListener(e -> { cardLayout.show(mainContentPanel, "COURSES"); updateNavColors(btnCourseTab); });
        btnAssignTab.addActionListener(e -> { cardLayout.show(mainContentPanel, "ASSIGNMENTS"); updateNavColors(btnAssignTab); });
        btnFilterTab.addActionListener(e -> { cardLayout.show(mainContentPanel, "FILTER"); updateNavColors(btnFilterTab); });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                saveData();
                System.exit(0);
            }
        });
    }

    private void updateNavColors(RoundedButton activeBtn) {
        btnStudentTab.setBackground(PRIMARY_BTN);
        btnProfTab.setBackground(PRIMARY_BTN);
        btnCourseTab.setBackground(PRIMARY_BTN);
        btnAssignTab.setBackground(PRIMARY_BTN);
        btnFilterTab.setBackground(PRIMARY_BTN);
        activeBtn.setBackground(ACCENT_COLOR);
        btnStudentTab.repaint(); btnProfTab.repaint(); btnCourseTab.repaint();
        btnAssignTab.repaint(); btnFilterTab.repaint();
    }

    private void saveData() {
        try {
            FileWriter fw = new FileWriter("university_data.txt");
            Node<Student> s = students.getHead();
            while (s != null) { fw.write("STUDENT," + s.data.getId() + "," + s.data.getName() + "," + s.data.getAge() + "," + s.data.getEmail() + "," + s.data.getDepartment() + "\n"); s = s.next; }
            Node<Professor> p = professors.getHead();
            while (p != null) { fw.write("PROFESSOR," + p.data.getId() + "," + p.data.getName() + "," + p.data.getEmail() + "," + p.data.getFaculty() + "," + p.data.getAssignedCourse() + "\n"); p = p.next; }
            Node<Course> c = courses.getHead();
            while (c != null) { fw.write("COURSE," + c.data.getId() + "," + c.data.getName() + "," + c.data.getCredits() + "," + c.data.getDepartment() + "," + c.data.getSection() + "," + c.data.getProfessor() + "\n"); c = c.next; }
            Node<Enrollment> en = enrollments.getHead();
            while (en != null) { fw.write("ENROLL," + en.data.studentId + "," + en.data.courseId + "\n"); en = en.next; }
            fw.close();
        } catch (IOException ex) { System.out.println("Error Saving"); }
    }

    private void loadData() {
        try {
            File f = new File("university_data.txt");
            if (!f.exists()) return;
            Scanner sc = new Scanner(f);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                String[] parts = line.split(",");
                if (parts.length > 0) {
                    if (parts[0].equals("STUDENT")) students.add(new Student(parts[1].toUpperCase(), parts[2].toUpperCase(), Integer.parseInt(parts[3]), parts[4].toLowerCase(), parts[5].toUpperCase()));
                    else if (parts[0].equals("PROFESSOR")) {
                        Professor p = new Professor(parts[1].toUpperCase(), parts[2].toUpperCase(), parts[3].toLowerCase(), parts[4].toUpperCase());
                        for (int i = 5; i < parts.length; i++) {
                            String cName = parts[i].trim().toUpperCase();
                            if (!cName.equals("NONE") && !cName.equals("TRUE") && !cName.equals("FALSE")) p.assignToCourse(cName);
                        }
                        professors.add(p);
                    } else if (parts[0].equals("COURSE")) {
                        Course c = new Course(parts[1].toUpperCase(), parts[2].toUpperCase(), Double.parseDouble(parts[3]), parts[4].toUpperCase(), (parts.length > 5) ? parts[5].toUpperCase() : "A");
                        c.setProfessor((parts.length > 6) ? parts[6].toUpperCase() : "NONE");
                        courses.add(c);
                    } else if (parts[0].equals("ENROLL")) enrollments.add(new Enrollment(parts[1].toUpperCase(), parts[2].toUpperCase()));
                }
            }
            sc.close();
        } catch (Exception ex) { System.out.println("Error Loading"); }
    }

    private JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text.toUpperCase());
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(TEXT_COLOR);
        return lbl;
    }

    private void styleTable(JTable table) {
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);
        table.setGridColor(new Color(230, 230, 230));
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(240, 240, 240));
        table.getTableHeader().setForeground(Color.BLACK);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
    }

    private void addFormField(JPanel panel, String labelText, Component field, int gridy, int col) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = gridy;
        gbc.gridx = col * 2; gbc.weightx = 0;
        panel.add(createLabel(labelText), gbc);
        gbc.gridx = (col * 2) + 1; gbc.weightx = 1.0;
        panel.add(field, gbc);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CONTENT_BG);

        JTextField txtId = new JTextField(); txtId.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtName = new JTextField(); txtName.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtAge = new JTextField(); txtAge.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JComboBox<String> cmbDept = new JComboBox<>(new String[]{"BSCS", "BSSE", "BSAI"});
        cmbDept.setBackground(Color.WHITE); cmbDept.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));

        addFormField(formPanel, "ID:", txtId, 0, 0);
        addFormField(formPanel, "NAME:", txtName, 0, 1);
        addFormField(formPanel, "AGE:", txtAge, 1, 0);
        addFormField(formPanel, "DEPT:", cmbDept, 1, 1);

        RoundedButton btnAdd = new RoundedButton("ADD", ACCENT_COLOR); btnAdd.setPreferredSize(new Dimension(80, 30));
        RoundedButton btnRemove = new RoundedButton("REMOVE", DANGER_COLOR); btnRemove.setPreferredSize(new Dimension(80, 30));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(CONTENT_BG); btnPanel.add(btnAdd); btnPanel.add(btnRemove);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(CONTENT_BG); topWrapper.add(formPanel, BorderLayout.CENTER); topWrapper.add(btnPanel, BorderLayout.SOUTH);

        String[] cols = {"ID", "NAME", "AGE", "EMAIL", "DEPARTMENT"};
        modelStudent = new DefaultTableModel(cols, 0);
        tblStudent = new JTable(modelStudent);
        styleTable(tblStudent);
        refreshTable(modelStudent, students.getHead());

        panel.add(topWrapper, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblStudent), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            try {
                String name = txtName.getText().toUpperCase();
                if (!name.matches("[A-Z\\s]+")) { JOptionPane.showMessageDialog(this, "ALPHABETS ONLY."); return; }
                int age = Integer.parseInt(txtAge.getText());
                if (age < 18) { JOptionPane.showMessageDialog(this, "AGE MUST BE 18+"); return; }
                students.add(new Student(txtId.getText().toUpperCase(), name, age, "std@szabist.pk", (String) cmbDept.getSelectedItem()));
                refreshTable(modelStudent, students.getHead());
                txtId.setText(""); txtName.setText(""); txtAge.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "INVALID INPUT"); }
        });

        btnRemove.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ENTER ID:");
            if (id != null && students.removeById(id.toUpperCase())) refreshTable(modelStudent, students.getHead());
            else JOptionPane.showMessageDialog(this, "NOT FOUND");
        });
        return panel;
    }

    private JPanel createProfessorPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CONTENT_BG);

        JTextField txtId = new JTextField(); txtId.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtName = new JTextField(); txtName.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtEmailUser = new JTextField(); txtEmailUser.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JComboBox<String> cmbFaculty = new JComboBox<>(new String[]{"BSCS", "BSSE", "BSAI"});
        cmbFaculty.setBackground(Color.WHITE); cmbFaculty.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));

        addFormField(formPanel, "ID:", txtId, 0, 0);
        addFormField(formPanel, "NAME:", txtName, 0, 1);
        addFormField(formPanel, "USER:", txtEmailUser, 1, 0);
        addFormField(formPanel, "FACULTY:", cmbFaculty, 1, 1);

        RoundedButton btnAdd = new RoundedButton("ADD", ACCENT_COLOR); btnAdd.setPreferredSize(new Dimension(80, 30));
        RoundedButton btnRemove = new RoundedButton("REMOVE", DANGER_COLOR); btnRemove.setPreferredSize(new Dimension(80, 30));

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(CONTENT_BG); btnPanel.add(btnAdd); btnPanel.add(btnRemove);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(CONTENT_BG); topWrapper.add(formPanel, BorderLayout.CENTER); topWrapper.add(btnPanel, BorderLayout.SOUTH);

        String[] cols = {"ID", "NAME", "EMAIL", "FACULTY", "ASSIGNED COURSES"};
        modelProfessor = new DefaultTableModel(cols, 0);
        tblProfessor = new JTable(modelProfessor);
        styleTable(tblProfessor);
        refreshTable(modelProfessor, professors.getHead());

        panel.add(topWrapper, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblProfessor), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            String name = txtName.getText().toUpperCase();
            if (!name.matches("[A-Z\\s]+")) { JOptionPane.showMessageDialog(this, "ALPHABETS ONLY."); return; }
            professors.add(new Professor(txtId.getText().toUpperCase(), name, txtEmailUser.getText()+"@szabist.pk", (String) cmbFaculty.getSelectedItem()));
            refreshTable(modelProfessor, professors.getHead());
            updateProfessorDropdown();
            txtId.setText(""); txtName.setText(""); txtEmailUser.setText("");
        });

        btnRemove.addActionListener(e -> {
            String id = JOptionPane.showInputDialog("ENTER ID:");
            if (id != null && professors.removeById(id.toUpperCase())) { refreshTable(modelProfessor, professors.getHead()); updateProfessorDropdown(); }
            else JOptionPane.showMessageDialog(this, "NOT FOUND");
        });
        return panel;
    }

    private JPanel createCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(CONTENT_BG);

        JTextField txtId = new JTextField(); txtId.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtName = new JTextField(); txtName.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtCr = new JTextField(); txtCr.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtSection = new JTextField(); txtSection.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JComboBox<String> cmbDept = new JComboBox<>(new String[]{"BSCS", "BSSE", "BSAI"});
        cmbDept.setBackground(Color.WHITE); cmbDept.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));

        cmbProfessorsForCourse = new JComboBox<>(); cmbProfessorsForCourse.addItem("NONE");
        cmbProfessorsForCourse.setBackground(Color.WHITE); cmbProfessorsForCourse.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        updateProfessorDropdown();

        addFormField(formPanel, "ID:", txtId, 0, 0);
        addFormField(formPanel, "NAME:", txtName, 0, 1);
        addFormField(formPanel, "SEC:", txtSection, 1, 0);
        addFormField(formPanel, "CREDITS:", txtCr, 1, 1);
        addFormField(formPanel, "DEPT:", cmbDept, 2, 0);
        addFormField(formPanel, "PROF:", cmbProfessorsForCourse, 2, 1);

        RoundedButton btnAdd = new RoundedButton("ADD", ACCENT_COLOR); btnAdd.setPreferredSize(new Dimension(80, 30));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnPanel.setBackground(CONTENT_BG); btnPanel.add(btnAdd);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(CONTENT_BG); topWrapper.add(formPanel, BorderLayout.CENTER); topWrapper.add(btnPanel, BorderLayout.SOUTH);

        String[] cols = {"ID", "NAME", "SECTION", "CREDITS", "DEPARTMENT", "PROFESSOR"};
        modelCourse = new DefaultTableModel(cols, 0);
        tblCourse = new JTable(modelCourse);
        styleTable(tblCourse);
        refreshTable(modelCourse, courses.getHead());

        panel.add(topWrapper, BorderLayout.NORTH);
        panel.add(new JScrollPane(tblCourse), BorderLayout.CENTER);

        btnAdd.addActionListener(e -> {
            try {
                double cr = Double.parseDouble(txtCr.getText());
                String sec = txtSection.getText().toUpperCase();
                if (sec.isEmpty()) sec = "A";
                Course c = new Course(txtId.getText().toUpperCase(), txtName.getText().toUpperCase(), cr, (String) cmbDept.getSelectedItem(), sec);
                String selectedProf = (String) cmbProfessorsForCourse.getSelectedItem();
                if (!selectedProf.equals("NONE")) {
                    Node<Professor> p = professors.getHead();
                    while (p != null) {
                        if (p.data.getName().equals(selectedProf)) {
                            if (p.data.canTakeMoreCourses()) { p.data.assignToCourse(c.getName() + "(" + sec + ")"); c.setProfessor(p.data.getName()); }
                            else JOptionPane.showMessageDialog(this, "PROFESSOR FULL!");
                            break;
                        } p = p.next;
                    }
                }
                courses.add(c); refreshTable(modelCourse, courses.getHead()); refreshTable(modelProfessor, professors.getHead());
                txtId.setText(""); txtName.setText(""); txtCr.setText(""); txtSection.setText("");
            } catch (Exception ex) { JOptionPane.showMessageDialog(this, "INVALID CREDITS"); }
        });
        return panel;
    }

    private void updateProfessorDropdown() {
        if (cmbProfessorsForCourse == null) return;
        cmbProfessorsForCourse.removeAllItems(); cmbProfessorsForCourse.addItem("NONE");
        Node<Professor> p = professors.getHead();
        while (p != null) { cmbProfessorsForCourse.addItem(p.data.getName()); p = p.next; }
    }

    // === ALIGNMENT FIX FOR ASSIGNMENTS ===
    private JPanel createAssignmentPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel grid = new JPanel(new GridLayout(2, 1, 15, 15));
        grid.setBackground(CONTENT_BG);

        // ENROLL
        JPanel pnlEnroll = new JPanel(new GridBagLayout()); // Using GridBag for precise alignment
        pnlEnroll.setBackground(new Color(245, 245, 245));
        pnlEnroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "ENROLL STUDENT", 0, 0, new Font("Segoe UI", Font.BOLD, 12), TEXT_COLOR));

        JTextField txtSid = new JTextField(8); txtSid.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtCid = new JTextField(8); txtCid.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        RoundedButton btnEnroll = new RoundedButton("ENROLL", ACCENT_COLOR);
        btnEnroll.setPreferredSize(new Dimension(80, 30));

        addAssignmentRow(pnlEnroll, "Student ID:", txtSid, "Course ID:", txtCid, btnEnroll);

        // ASSIGN
        JPanel pnlAssign = new JPanel(new GridBagLayout());
        pnlAssign.setBackground(new Color(245, 245, 245));
        pnlAssign.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "ASSIGN PROFESSOR", 0, 0, new Font("Segoe UI", Font.BOLD, 12), TEXT_COLOR));

        JTextField txtPid = new JTextField(8); txtPid.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        JTextField txtCid2 = new JTextField(8); txtCid2.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        RoundedButton btnAssign = new RoundedButton("ASSIGN", new Color(0, 21, 72));
        btnAssign.setPreferredSize(new Dimension(80, 30));

        addAssignmentRow(pnlAssign, "Prof ID:", txtPid, "Course ID:", txtCid2, btnAssign);

        grid.add(pnlEnroll);
        grid.add(pnlAssign);
        panel.add(grid, BorderLayout.NORTH);

        JTextArea log = new JTextArea("ACTION LOG:\n");
        log.setEditable(false);
        log.setBackground(new Color(240, 240, 240));
        log.setForeground(new Color(0, 100, 0));
        log.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(log), BorderLayout.CENTER);

        btnEnroll.addActionListener(e -> {
            Student s = students.findById(txtSid.getText().toUpperCase());
            Course c = courses.findById(txtCid.getText().toUpperCase());
            if (s != null && c != null) { enrollments.add(new Enrollment(txtSid.getText().toUpperCase(), txtCid.getText().toUpperCase())); log.append("SUCCESS: ENROLLED " + s.getName() + "\n"); } else log.append("ERROR: INVALID ID\n");
        });

        btnAssign.addActionListener(e -> {
            Professor p = professors.findById(txtPid.getText().toUpperCase());
            Course c = courses.findById(txtCid2.getText().toUpperCase());
            if (p != null && c != null) {
                if (!c.getProfessor().equals("NONE")) log.append("FAIL: COURSE TAKEN.\n");
                else if (!p.canTakeMoreCourses()) log.append("FAIL: PROF FULL.\n");
                else { p.assignToCourse(c.getName() + "(" + c.getSection() + ")"); c.setProfessor(p.getName()); refreshTable(modelProfessor, professors.getHead()); refreshTable(modelCourse, courses.getHead()); log.append("SUCCESS: ASSIGNED " + p.getName() + "\n"); }
            } else log.append("ERROR: INVALID ID.\n");
        });
        return panel;
    }

    // Helper for Assignment Row Alignment
    private void addAssignmentRow(JPanel p, String l1, Component c1, String l2, Component c2, Component btn) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Col 1: Label 1 (Fixed width for alignment)
        gbc.gridx = 0; p.add(createLabel(l1), gbc);

        // Col 2: Field 1
        gbc.gridx = 1; gbc.weightx = 1.0; p.add(c1, gbc);

        // Col 3: Label 2
        gbc.gridx = 2; gbc.weightx = 0; p.add(createLabel(l2), gbc);

        // Col 4: Field 2
        gbc.gridx = 3; gbc.weightx = 1.0; p.add(c2, gbc);

        // Col 5: Button
        gbc.gridx = 4; gbc.weightx = 0; p.add(btn, gbc);
    }

    private JPanel createFilterPanel() {
        JPanel panel = new JPanel(new BorderLayout(15, 10));
        panel.setBackground(CONTENT_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(CONTENT_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<String> cmbFilter = new JComboBox<>(new String[]{"BSCS", "BSSE", "BSAI"});
        cmbFilter.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        cmbFilter.setBackground(Color.WHITE);

        // **COLOR FIX: Blue**
        RoundedButton btnDeptFilter = new RoundedButton("FILTER", ACCENT_COLOR);
        btnDeptFilter.setPreferredSize(new Dimension(80, 30));

        gbc.gridx = 0; gbc.gridy = 0; topPanel.add(createLabel("VIEW BY DEPT:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1.0; topPanel.add(cmbFilter, gbc);
        gbc.gridx = 2; gbc.weightx = 0; topPanel.add(btnDeptFilter, gbc);

        JTextField txtSearchStudent = new JTextField(); txtSearchStudent.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));
        RoundedButton btnSearchStudent = new RoundedButton("SEARCH (BINARY)", ACCENT_COLOR);
        btnSearchStudent.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0; gbc.gridy = 1; topPanel.add(createLabel("SEARCH (ID):"), gbc);
        gbc.gridx = 1; topPanel.add(txtSearchStudent, gbc);
        gbc.gridx = 2; topPanel.add(btnSearchStudent, gbc);

        JTextField txtSearchCourse = new JTextField(); txtSearchCourse.setBorder(new RoundedBorder(INPUT_RADIUS, INPUT_BORDER));

        // **COLOR FIX: Blue**
        RoundedButton btnSearchCourse = new RoundedButton("FIND COURSE", ACCENT_COLOR);
        btnSearchCourse.setPreferredSize(new Dimension(120, 30));
        gbc.gridx = 0; gbc.gridy = 2; topPanel.add(createLabel("SEARCH COURSE:"), gbc);
        gbc.gridx = 1; topPanel.add(txtSearchCourse, gbc);
        gbc.gridx = 2; topPanel.add(btnSearchCourse, gbc);

        RoundedButton btnSort = new RoundedButton("SORT (BUBBLE)", PRIMARY_BTN);
        gbc.gridx = 0; gbc.gridy = 3; topPanel.add(createLabel("ORGANIZE:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; topPanel.add(btnSort, gbc);

        JTextArea txtResult = new JTextArea();
        txtResult.setEditable(false);
        txtResult.setFont(new Font("Monospaced", Font.PLAIN, 12));
        txtResult.setBackground(new Color(245, 245, 245));
        txtResult.setForeground(Color.BLACK);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtResult), BorderLayout.CENTER);

        btnDeptFilter.addActionListener(e -> {
            String dept = (String) cmbFilter.getSelectedItem();
            StringBuilder sb = new StringBuilder("=== " + dept + " REPORT ===\n\n");
            appendList(sb, students.getHead(), dept, "STUDENTS", true);
            appendList(sb, professors.getHead(), dept, "PROFESSORS", true);
            appendList(sb, courses.getHead(), dept, "COURSES", true);
            txtResult.setText(sb.toString());
        });

        btnSearchStudent.addActionListener(e -> {
            String id = txtSearchStudent.getText().trim().toUpperCase();
            if(id.isEmpty()) { JOptionPane.showMessageDialog(this, "ENTER ID!"); return; }
            students.bubbleSort();
            Object result = students.binarySearch(id);
            txtResult.setText(result != null ? "=== FOUND ===\n" + result : "ID NOT FOUND.");
        });

        btnSearchCourse.addActionListener(e -> {
            String courseName = txtSearchCourse.getText().trim().toUpperCase();
            String targetCid = null;
            Node<Course> c = courses.getHead();
            while(c != null) { if(c.data.getName().equals(courseName)) { targetCid = c.data.getId(); break; } c = c.next; }
            if(targetCid != null) {
                StringBuilder sb = new StringBuilder("COURSE: " + courseName + "\n\n--- ENROLLED ---\n");
                Node<Enrollment> en = enrollments.getHead();
                boolean found = false;
                while(en != null) {
                    if (en.data.courseId.equals(targetCid)) {
                        Student s = students.findById(en.data.studentId);
                        if (s != null) { sb.append(s.toString()).append("\n"); found = true; }
                    }
                    en = en.next;
                }
                txtResult.setText(found ? sb.toString() : sb.toString() + "NO STUDENTS.");
            } else txtResult.setText("COURSE NOT FOUND");
        });

        btnSort.addActionListener(e -> {
            students.bubbleSort(); professors.bubbleSort(); courses.bubbleSort();
            JOptionPane.showMessageDialog(this, "DATA SORTED!");
        });
        return panel;
    }

    // --- HELPER METHODS CORRECTLY PLACED INSIDE THE CLASS ---
    private <T> void refreshTable(DefaultTableModel model, Node<T> head) {
        model.setRowCount(0);
        while (head != null) {
            if (head.data instanceof Student) model.addRow(((Student) head.data).toRow());
            else if (head.data instanceof Professor) model.addRow(((Professor) head.data).toRow());
            else if (head.data instanceof Course) model.addRow(((Course) head.data).toRow());
            head = head.next;
        }
    }

    private <T> void appendList(StringBuilder sb, Node<T> head, String filter, String title, boolean useFilter) {
        sb.append("--- ").append(title).append(" ---\n");
        while (head != null) {
            boolean match = false;
            if (useFilter) {
                if (head.data instanceof Student && ((Student) head.data).getDepartment().equalsIgnoreCase(filter)) match = true;
                if (head.data instanceof Professor && ((Professor) head.data).getFaculty().equalsIgnoreCase(filter)) match = true;
                if (head.data instanceof Course && ((Course) head.data).getDepartment().equalsIgnoreCase(filter)) match = true;
            } else match = true;
            if (match) sb.append(head.data).append("\n");
            head = head.next;
        }
        sb.append("\n");
    }

    public static void showLoginScreen() {
        JFrame loginFrame = new JFrame("SIGN IN");
        loginFrame.setSize(350, 250);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setLayout(new GridBagLayout());
        loginFrame.getContentPane().setBackground(new Color(255, 255, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("ADMIN LOGIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(50, 50, 50));

        JTextField txtUser = new JTextField(15);
        txtUser.setBorder(new RoundedBorder(15, Color.GRAY));

        JPasswordField txtPass = new JPasswordField(15);
        txtPass.setBorder(new RoundedBorder(15, Color.GRAY));

        RoundedButton btnLogin = new RoundedButton("LOGIN", new Color(0, 96, 183));

        JLabel lblUser = new JLabel("USERNAME:"); lblUser.setForeground(new Color(50, 50, 50));
        JLabel lblPass = new JLabel("PASSWORD:"); lblPass.setForeground(new Color(50, 50, 50));

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        loginFrame.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1; gbc.gridx = 0; loginFrame.add(lblUser, gbc);
        gbc.gridx = 1; loginFrame.add(txtUser, gbc);

        gbc.gridy = 2; gbc.gridx = 0; loginFrame.add(lblPass, gbc);
        gbc.gridx = 1; loginFrame.add(txtPass, gbc);

        gbc.gridy = 3; gbc.gridx = 0; gbc.gridwidth = 2;
        loginFrame.add(btnLogin, gbc);

        loginFrame.setVisible(true);

        btnLogin.addActionListener(e -> {
            if (txtUser.getText().equals("admin") && new String(txtPass.getPassword()).equals("admin")) {
                loginFrame.dispose();
                new UniversitySystemGUI().setVisible(true);
            } else {
                JOptionPane.showMessageDialog(loginFrame, "INVALID (TRY: admin/admin)");
            }
        });
    }

    private static class RoundedBorder extends javax.swing.border.AbstractBorder {
        private int radius;
        private Color color;
        public RoundedBorder(int radius, Color color) { this.radius = radius; this.color = color; }
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2.dispose();
        }
        @Override
        public Insets getBorderInsets(Component c) { return new Insets(radius/2, radius/2, radius/2, radius/2); }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(UniversitySystemGUI::showLoginScreen);
    }
}