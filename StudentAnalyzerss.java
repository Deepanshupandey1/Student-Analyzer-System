import java.text.*;
import java.util.*;

public class StudentAnalyzerss
{
    // 🎨 COLORS
    static final String RESET = "\u001B[0m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String CYAN = "\u001B[36m";
    static final String BLUE = "\u001B[34m";
    static final String BOLD = "\u001B[1m";

    static Scanner sc = new Scanner(System.in);
    static DecimalFormat df = new DecimalFormat("0.00");

    static int numStudents = 0, numSubjects = 0;
    static String[] subjects = {}, names = {};
    static double[][] marks = {};
    static boolean dataEntered = false;

    static double[] totals = {}, averages = {}, percentages = {};
    static String[] grades = {};
    static int[] ranks = {};

    public static void main(String[] args)
    {
        boolean running = true;

        while (running)
        {
            loadingBar();
            printMenu();

            int choice = readInt(CYAN + "\nEnter choice: " + RESET, 0, 9);

            switch (choice)
            {
                case 1 -> setupClass();
                case 2 -> enterMarks();
                case 3 -> viewAllStudents();
                case 4 -> running = false;
                case 5 -> searchStudent();
                case 6 -> showTopper();
                case 7 -> classSummary();
                case 8 -> failStudents();
                case 9 -> subjectTopper();
            }
        }
    }

    // 🔄 LOADING BAR (FIXED)
    static void loadingBar()
    {
        System.out.print(YELLOW + "\nLoading: " + RESET);

        for (int i = 0; i < 15; i++)
        {
            try
            {
                Thread.sleep(50);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            System.out.print(GREEN + "#" + RESET);
        }

        System.out.println(" " + GREEN + "DONE" + RESET);
    }

    // 🎯 MENU
    static void printMenu()
    {
        System.out.println(CYAN + BOLD + "\n====================================");
        System.out.println("        STUDENT ANALYZER SYSTEM");
        System.out.println("====================================" + RESET);

        System.out.println(YELLOW + "1. Setup Class");
        System.out.println("2. Enter Marks");
        System.out.println("3. View All Students");
        System.out.println("4. Exit");
        System.out.println("5. Search Student");
        System.out.println("6. Show Topper");
        System.out.println("7. Class Summary");
        System.out.println("8. Fail Students");
        System.out.println("9. Subject Topper" + RESET);
    }

    static void setupClass()
    {
        numStudents = readInt("Enter number of students: ", 1, 50);
        numSubjects = readInt("Enter number of subjects: ", 1, 10);

        sc.nextLine();

        subjects = new String[numSubjects];

        System.out.println("\nEnter subject names:");
        for (int i = 0; i < numSubjects; i++)
        {
            System.out.print("Subject " + (i + 1) + ": ");
            subjects[i] = sc.nextLine();
        }

        names = new String[numStudents];
        marks = new double[numStudents][numSubjects];
        totals = new double[numStudents];
        averages = new double[numStudents];
        percentages = new double[numStudents];
        grades = new String[numStudents];
        ranks = new int[numStudents];

        System.out.println(GREEN + "\nSetup completed successfully!" + RESET);
    }

    static void enterMarks()
    {
        if (numStudents == 0)
        {
            System.out.println(RED + "Setup class first!" + RESET);
            return;
        }

        for (int i = 0; i < numStudents; i++)
        {
            System.out.print("\nEnter student name: ");
            names[i] = sc.next();

            for (int j = 0; j < numSubjects; j++)
            {
                marks[i][j] = readDouble("Marks in " + subjects[j] + ": ", 0, 100);
            }
        }

        computeResults();
        dataEntered = true;

        System.out.println(GREEN + "\nData saved successfully!" + RESET);
    }

    static void computeResults()
    {
        for (int i = 0; i < numStudents; i++)
        {
            double sum = 0;

            for (int j = 0; j < numSubjects; j++)
                sum += marks[i][j];

            totals[i] = sum;
            averages[i] = sum / numSubjects;
            percentages[i] = (sum / (numSubjects * 100)) * 100;
            grades[i] = calcGrade(percentages[i]);
        }

        Integer[] idx = new Integer[numStudents];

        for (int i = 0; i < numStudents; i++)
            idx[i] = i;

        Arrays.sort(idx, (a, b) -> Double.compare(percentages[b], percentages[a]));

        for (int i = 0; i < numStudents; i++)
            ranks[idx[i]] = i + 1;
    }

    static String calcGrade(double p)
    {
        if (p >= 90) return "A+";
        else if (p >= 80) return "A";
        else if (p >= 70) return "B+";
        else if (p >= 60) return "B";
        else if (p >= 50) return "C";
        else if (p >= 40) return "D";
        else return "F";
    }

    static void viewAllStudents()
    {
        if (!dataEntered)
        {
            System.out.println(RED + "No data available!" + RESET);
            return;
        }

        System.out.println(BLUE + "\n----------- STUDENT REPORT -----------" + RESET);

        for (int i = 0; i < numStudents; i++)
        {
            String status = (percentages[i] >= 40) ? GREEN + "PASS" : RED + "FAIL";

            System.out.println("Name: " + names[i] +
                    " | %: " + df.format(percentages[i]) +
                    " | Grade: " + grades[i] +
                    " | Rank: " + ranks[i] +
                    " | " + status + RESET);
        }
    }

    static void searchStudent()
    {
        System.out.print("Enter name: ");
        String s = sc.next();

        for (int i = 0; i < numStudents; i++)
        {
            if (names[i].equalsIgnoreCase(s))
            {
                System.out.println(GREEN + "Found: " + df.format(percentages[i]) + "%" + RESET);
                return;
            }
        }

        System.out.println(RED + "Student not found!" + RESET);
    }

    static void showTopper()
    {
        int top = 0;

        for (int i = 1; i < numStudents; i++)
            if (percentages[i] > percentages[top]) top = i;

        System.out.println(YELLOW + "\nTopper: " + names[top] +
                " (" + df.format(percentages[top]) + "%)" + RESET);
    }

    static void classSummary()
    {
        double sum = 0;
        for (double p : percentages) sum += p;

        System.out.println(CYAN + "Class Average: " +
                df.format(sum / numStudents) + "%" + RESET);
    }

    static void failStudents()
    {
        System.out.println(RED + "\nFailed Students:" + RESET);

        boolean found = false;

        for (int i = 0; i < numStudents; i++)
        {
            if (percentages[i] < 40)
            {
                System.out.println("-> " + names[i]);
                found = true;
            }
        }

        if (!found)
            System.out.println(GREEN + "No failed students!" + RESET);
    }

    static void subjectTopper()
    {
        for (int j = 0; j < numSubjects; j++)
        {
            int top = 0;

            for (int i = 1; i < numStudents; i++)
                if (marks[i][j] > marks[top][j]) top = i;

            System.out.println("Topper in " + subjects[j] + ": " + names[top]);
        }
    }

    static int readInt(String msg, int min, int max)
    {
        while (true)
        {
            try
            {
                System.out.print(msg);
                int v = sc.nextInt();
                if (v >= min && v <= max) return v;
            }
            catch (Exception e)
            {
                sc.nextLine();
            }
            System.out.println(RED + "Invalid input!" + RESET);
        }
    }

    static double readDouble(String msg, double min, double max)
    {
        while (true)
        {
            try
            {
                System.out.print(msg);
                double v = sc.nextDouble();
                if (v >= min && v <= max) return v;
            }
            catch (Exception e)
            {
                sc.nextLine();
            }
            System.out.println(RED + "Invalid input!" + RESET);
        }
    }
}