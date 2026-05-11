import java.util.*;
import java.io.*;
import java.text.DecimalFormat;

class Student
{
    String name;
    double[] marks;
    double total;
    double percentage;
    String grade;
    int rank;
    boolean pass;

    Student(String name, int subjects)
    {
        this.name = name;
        marks = new double[subjects];
    }
}

public class AdvancedStudentAnalyzer
{
    // COLORS
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String CYAN = "\u001B[36m";
    static final String BLUE = "\u001B[34m";
    static final String BOLD = "\u001B[1m";

    static Scanner sc = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("0.00");

    static ArrayList<Student> students = new ArrayList<>();

    static int numSubjects;
    static String[] subjects;

    public static void main(String[] args)
    {
        login();

        boolean running = true;

        while (running)
        {
            menu();

            int choice = readInt("Enter Choice: ", 1, 10);

            switch(choice)
            {
                case 1 -> setupSubjects();
                case 2 -> addStudent();
                case 3 -> viewStudents();
                case 4 -> searchStudent();
                case 5 -> showTopper();
                case 6 -> classSummary();
                case 7 -> failStudents();
                case 8 -> subjectTopper();
                case 9 -> saveToFile();
                case 10 -> running = false;
            }
        }

        System.out.println(GREEN + "\nThank You!" + RESET);
    }

    // LOGIN SYSTEM
    static void login()
    {
        String user, pass;

        System.out.println(CYAN + BOLD);
        System.out.println("====================================");
        System.out.println("   STUDENT ANALYZER LOGIN SYSTEM");
        System.out.println("====================================");
        System.out.println(RESET);

        while (true)
        {
            System.out.print("Username: ");
            user = sc.next();

            System.out.print("Password: ");
            pass = sc.next();

            if(user.equals("admin") && pass.equals("1234"))
            {
                System.out.println(GREEN + "\nLogin Successful!\n" + RESET);
                break;
            }
            else
            {
                System.out.println(RED + "Invalid Credentials!\n" + RESET);
            }
        }
    }

    // MENU
    static void menu()
    {
        System.out.println(CYAN + BOLD);
        System.out.println("\n====================================");
        System.out.println(" ADVANCED STUDENT ANALYZER SYSTEM");
        System.out.println("====================================");
        System.out.println(RESET);

        System.out.println(YELLOW + "1. Setup Subjects");
        System.out.println("2. Add Student");
        System.out.println("3. View All Students");
        System.out.println("4. Search Student");
        System.out.println("5. Show Topper");
        System.out.println("6. Class Summary");
        System.out.println("7. Failed Students");
        System.out.println("8. Subject Toppers");
        System.out.println("9. Save Report To File");
        System.out.println("10. Exit" + RESET);
    }

    // SETUP SUBJECTS
    static void setupSubjects()
    {
        numSubjects = readInt("Enter Number of Subjects: ", 1, 10);

        sc.nextLine();

        subjects = new String[numSubjects];

        for(int i = 0; i < numSubjects; i++)
        {
            System.out.print("Enter Subject " + (i + 1) + ": ");
            subjects[i] = sc.nextLine();
        }

        System.out.println(GREEN +
                "\nSubjects Added Successfully!" + RESET);
    }

    // ADD STUDENT
    static void addStudent()
    {
        if(subjects == null)
        {
            System.out.println(RED +
                    "Setup Subjects First!" + RESET);
            return;
        }

        sc.nextLine();

        System.out.print("\nEnter Student Name: ");
        String name = sc.nextLine();

        Student s = new Student(name, numSubjects);

        for(int i = 0; i < numSubjects; i++)
        {
            s.marks[i] = readDouble(
                    "Enter Marks in " + subjects[i] + ": ",
                    0, 100);
        }

        calculateResult(s);

        students.add(s);

        updateRanks();

        System.out.println(GREEN +
                "\nStudent Added Successfully!" + RESET);
    }

    // CALCULATE RESULT
    static void calculateResult(Student s)
    {
        double sum = 0;
        boolean failed = false;

        for(double m : s.marks)
        {
            sum += m;

            if(m < 33)
            {
                failed = true;
            }
        }

        s.total = sum;
        s.percentage = sum / numSubjects;
        s.pass = !failed;

        if(!s.pass)
        {
            s.grade = "F";
        }
        else
        {
            s.grade = calculateGrade(s.percentage);
        }
    }

    // CALCULATE GRADE
    static String calculateGrade(double p)
    {
        if(p >= 90)
            return "A+";
        else if(p >= 80)
            return "A";
        else if(p >= 70)
            return "B+";
        else if(p >= 60)
            return "B";
        else if(p >= 50)
            return "C";
        else if(p >= 40)
            return "D";
        else
            return "F";
    }

    // UPDATE RANKS
    static void updateRanks()
    {
        students.sort((a, b) ->
                Double.compare(b.percentage, a.percentage));

        for(int i = 0; i < students.size(); i++)
        {
            students.get(i).rank = i + 1;
        }
    }

    // VIEW STUDENTS
    static void viewStudents()
    {
        if(!hasStudents())
            return;

        System.out.println(BLUE + BOLD);
        System.out.println("\n================ STUDENT REPORT ================");
        System.out.println(RESET);

        System.out.printf("%-20s %-10s %-10s %-10s %-10s\n",
                "Name", "Percent", "Grade", "Rank", "Status");

        System.out.println("----------------------------------------------------------");

        for(Student s : students)
        {
            String status = s.pass
                    ? GREEN + "PASS"
                    : RED + "FAIL";

            System.out.printf("%-20s %-10s %-10s %-10d %s%s\n",
                    s.name,
                    df.format(s.percentage),
                    s.grade,
                    s.rank,
                    status,
                    RESET);
        }
    }

    // SEARCH STUDENT
    static void searchStudent()
    {
        if(!hasStudents())
            return;

        sc.nextLine();

        System.out.print("Enter Student Name: ");
        String search = sc.nextLine();

        for(Student s : students)
        {
            if(s.name.toLowerCase()
                    .contains(search.toLowerCase()))
            {
                int max = 0;
                int min = 0;

                for(int i = 1; i < numSubjects; i++)
                {
                    if(s.marks[i] > s.marks[max])
                        max = i;

                    if(s.marks[i] < s.marks[min])
                        min = i;
                }

                System.out.println(GREEN +
                        "\nStudent Found!" + RESET);

                System.out.println("Name: " + s.name);

                System.out.println("Percentage: "
                        + df.format(s.percentage) + "%");

                System.out.println("Grade: "
                        + s.grade);

                System.out.println("Status: "
                        + (s.pass ? "PASS" : "FAIL"));

                System.out.println("Strong Subject: "
                        + subjects[max]);

                System.out.println("Weak Subject: "
                        + subjects[min]);

                return;
            }
        }

        System.out.println(RED +
                "Student Not Found!" + RESET);
    }

    // SHOW TOPPER
    static void showTopper()
    {
        if(!hasStudents())
            return;

        Student top = students.get(0);

        System.out.println(YELLOW + BOLD);
        System.out.println("\n=========== TOPPER OF CLASS ===========");
        System.out.println(RESET);

        System.out.println("Name: " + top.name);

        System.out.println("Percentage: "
                + df.format(top.percentage) + "%");

        System.out.println("Grade: " + top.grade);
    }

    // CLASS SUMMARY
    static void classSummary()
    {
        if(!hasStudents())
            return;

        double sum = 0;

        int passCount = 0;
        int failCount = 0;

        for(Student s : students)
        {
            sum += s.percentage;

            if(s.pass)
                passCount++;
            else
                failCount++;
        }

        double avg = sum / students.size();

        Student top = students.get(0);
        Student weak = students.get(students.size() - 1);

        System.out.println(CYAN + BOLD);
        System.out.println("\n============= CLASS SUMMARY =============");
        System.out.println(RESET);

        System.out.println("Total Students: "
                + students.size());

        System.out.println("Class Average: "
                + df.format(avg) + "%");

        System.out.println("Passed Students: "
                + passCount);

        System.out.println("Failed Students: "
                + failCount);

        System.out.println(GREEN +
                "Topper: " + top.name +
                " (" + df.format(top.percentage) + "%)"
                + RESET);

        System.out.println(RED +
                "Weak Student: " + weak.name +
                " (" + df.format(weak.percentage) + "%)"
                + RESET);
    }

    // FAILED STUDENTS
    static void failStudents()
    {
        if(!hasStudents())
            return;

        boolean found = false;

        System.out.println(RED + BOLD);
        System.out.println("\n=========== FAILED STUDENTS ===========");
        System.out.println(RESET);

        for(Student s : students)
        {
            if(!s.pass)
            {
                System.out.println("-> " + s.name);
                found = true;
            }
        }

        if(!found)
        {
            System.out.println(GREEN +
                    "No Failed Students!" + RESET);
        }
    }

    // SUBJECT TOPPERS
    static void subjectTopper()
    {
        if(!hasStudents())
            return;

        System.out.println(CYAN + BOLD);
        System.out.println("\n=========== SUBJECT TOPPERS ===========");
        System.out.println(RESET);

        for(int j = 0; j < numSubjects; j++)
        {
            Student top = students.get(0);

            for(Student s : students)
            {
                if(s.marks[j] > top.marks[j])
                {
                    top = s;
                }
            }

            System.out.println(subjects[j] +
                    " -> " + top.name +
                    " (" + top.marks[j] + ")");
        }
    }

    // SAVE REPORT TO FILE
    static void saveToFile()
    {
        if(!hasStudents())
            return;

        try
        {
            FileWriter fw =
                    new FileWriter("StudentReport.txt");

            fw.write("=========== STUDENT REPORT ===========\n\n");

            for(Student s : students)
            {
                fw.write("Name: " + s.name + "\n");

                for(int i = 0; i < numSubjects; i++)
                {
                    fw.write(subjects[i] + ": "
                            + s.marks[i] + "\n");
                }

                fw.write("Percentage: "
                        + df.format(s.percentage) + "%\n");

                fw.write("Grade: " + s.grade + "\n");

                fw.write("Rank: " + s.rank + "\n");

                fw.write("Status: "
                        + (s.pass ? "PASS" : "FAIL"));

                fw.write("\n-----------------------------------\n");
            }

            fw.close();

            System.out.println(GREEN +
                    "Report Saved Successfully!" + RESET);
        }
        catch(IOException e)
        {
            System.out.println(RED +
                    "Error Saving File!" + RESET);
        }
    }

    // CHECK STUDENT DATA
    static boolean hasStudents()
    {
        if(students.isEmpty())
        {
            System.out.println(RED +
                    "No Student Data Available!" + RESET);

            return false;
        }

        return true;
    }

    // READ INTEGER
    static int readInt(String msg,
                       int min,
                       int max)
    {
        while(true)
        {
            try
            {
                System.out.print(msg);

                int val = sc.nextInt();

                if(val >= min && val <= max)
                {
                    return val;
                }
                else
                {
                    System.out.println(RED +
                            "Enter Value Between "
                            + min + " and " + max
                            + RESET);
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println(RED +
                        "Invalid Integer Input!"
                        + RESET);

                sc.nextLine();
            }
        }
    }

    // READ DOUBLE
    static double readDouble(String msg,
                             double min,
                             double max)
    {
        while(true)
        {
            try
            {
                System.out.print(msg);

                double val = sc.nextDouble();

                if(val >= min && val <= max)
                {
                    return val;
                }
                else
                {
                    System.out.println(RED +
                            "Enter Marks Between "
                            + min + " and " + max
                            + RESET);
                }
            }
            catch(InputMismatchException e)
            {
                System.out.println(RED +
                        "Invalid Decimal Input!"
                        + RESET);

                sc.nextLine();
            }
        }
    }
}