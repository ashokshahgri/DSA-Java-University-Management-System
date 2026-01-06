# 🎓 University Management System (Java Swing + DSA)

A comprehensive **University Management System** built from scratch using **Java Swing**. This project demonstrates the practical application of **Data Structures and Algorithms (DSA)** by implementing custom data structures instead of relying on Java's built-in Collections framework.

## 🚀 Key Features

### 1. 🏛️ Role Management
* **Students:** Add, remove, and view students with automatic email generation based on Department and ID.
* **Professors:** Manage faculty members and assign them to courses (Logic implemented to limit professors to max 3 courses).
* **Courses:** Create courses with credit hours and assign specific sections.

### 2. ⚡ Data Structures & Algorithms
* **Custom Linked List:** Implemented a Generic Singly Linked List (`MyList<T>`) to store data dynamically.
* **Binary Search:** Implemented **Binary Search** to quickly locate records by ID ($O(\log n)$).
    * *Note:* Since Binary Search requires sorted data, the system automatically sorts the Linked List before searching.
* **Bubble Sort:** Used to sort entities by ID to enable Binary Search.

### 3. 🔍 Search & Filter
* **Smart Filtering:** Filter all records by Department (BSCS, BSSE, BSAI).
* **Enrollment System:** Link Students to Courses and Professors to Courses with validation checks.

### 4. 💾 Data Persistence
* **File Handling:** The application automatically saves all data to `university_data.txt` upon closing and reloads it on startup, ensuring no data is lost.

### 5. 🎨 Modern UI (Java Swing)
* **Custom Components:** Features rounded buttons and input fields for a modern, flat UI look.
* **Responsive Layouts:** Built using `GridBagLayout` and `CardLayout` for precise alignment and smooth navigation.

---

## 🛠️ Tech Stack
* **Language:** Java (JDK 8+)
* **GUI:** Java Swing (JFrame, JPanel, Graphics2D)
* **Storage:** Local File I/O (.txt)
* **IDE:** IntelliJ IDEA 

---

## 👨‍💻 Contributors
* **[Sineha Tharwani]https://github.com/sinehatharwani** -
