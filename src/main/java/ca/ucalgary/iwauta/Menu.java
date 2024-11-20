package ca.ucalgary.iwauta;

import ca.ucalgary.iwauta.objects.Course;
import ca.ucalgary.iwauta.objects.Project;
import ca.ucalgary.iwauta.util.FileLoader;
import ca.ucalgary.iwauta.util.FileSaver;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * CPSC 233 W24 ca.ucalgary.groupprojectgui.objects.Project ca.ucalgary.groupprojectgui.Menu.java - prompts user to pick an option and corrects data.
 *
 * @author Utaha Iwai
 * @tutorial T09
 * @email utaha.iwai@ucalgary.ca
 */
public class Menu {
    // scanner object
    private static final Scanner scanner = new Scanner(System.in);

    // ca.ucalgary.groupprojectgui.Data
    private static Data data = new Data();

    /*
    Formatting output texts (and menu)
     */
    // creating array list of menu options
    private static final ArrayList<String> options = new ArrayList<>();
    static {

        options.add("Exit"); //0
        options.add("Add a course."); //1
        options.add("Add a course target grade."); //2
        options.add("Add a project."); //3
        options.add("Mark a project as completed."); //4
        options.add("Print all pending assignments."); //5
        options.add("Record a project grade."); //6
        options.add("Print all projects."); //7
        options.add("Print all target and actual grades for each course."); //8
        options.add("Print all info on courses."); //9
        options.add("Close a course"); //10
        options.add("Save."); //11
        options.add("Load."); //12
    }

    private static String optMessage = """
            Track and manage your school work!
            \tMenu Options
            """;
    static {
        StringBuilder sb = new StringBuilder();
        sb.append(optMessage);
        for (int counter = 0; counter < options.size(); counter++) {
            sb.append(String.format("\t%d. %s\n", counter, options.get(counter)));
        }
        optMessage = sb.toString();
    }

    // ca.ucalgary.groupprojectgui.objects.Course info formatter
    private static final String COURSE_HEADER = String.format(Course.COURSE_FORMAT, "COURSE_NAME", "PROF_NAME","PROF_EMAIL","STATUS");
    private static String COURSE_SEP = "";// separate header and course info
    static {
        for (int i = 0; i < COURSE_HEADER.length(); i++){
            COURSE_SEP += "-";
        }
    }

    // ca.ucalgary.groupprojectgui.objects.Project info formatter
    private static final String PROJECT_HEADER = String.format(Project.PROJECT_FORMAT, "COURSE", "PROJECT_NAME","DEADLINE(DD/MM/YYYY)", "WEIGHT", "STATUS");
    private static String PROJECT_SEP = "";// separate header and project info
    static {
        for (int i = 0; i < PROJECT_HEADER.length(); i++){
            PROJECT_SEP += "-";
        }
    }

    // Target & Actual grade formatter
    private static final String GRADE_FORMAT = "%-11s    %-14s   %-14s%n";
    private static final String GRADE_HEADER = String.format(GRADE_FORMAT, "COURSE_NAME", "TARGET_GRADE","ACTUAL_GRADE");
    private static String GRADE_SEP = "";// separate header and grades
    static {
        for (int i = 0; i < GRADE_HEADER.length(); i++){
            GRADE_SEP += "-";
        }
    }



    /**
     * Prompts user to pick an option from the menu. Calls a corresponding method to do continue.
     */
    public static void menuLoop(File file){
        if (file != null) {
            menuLoad(file);
        }
        String userInput;
        int option;
        // Initial input
        while(true) { // Loops if user enters nothing or invalid input
            System.out.println(optMessage);
            userInput = scanner.nextLine().trim();
            // Error checking
            try {
                option = Integer.parseInt(userInput);
                break;
            } catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }

        // Option other than 'Exit'
        while (option != 0) {
            if(option > 0 && option < options.size()) {
                //Output the option selected
                System.out.printf("Selected option %d. %s%n", option, options.get(option));
                // Pause
                System.out.println("Press the Enter key to continue...");
                scanner.nextLine();
            }
            switch (option) {
                case 1 -> menuEnterNewCourse();
                case 2 -> menuEnterCourseTargetGrade();
                case 3 -> menuEnterNewProject();
                case 4 -> menuMarkProjectComplete();
                case 5 -> menuPrintPendingAssignments();
                case 6 -> menuEnterProjectGrade();
                case 7 -> menuPrintAllProjects();
                case 8 -> menuPrintTargetAndActualGrade();
                case 9 -> menuPrintCourseInfo();
                case 10 -> menuCloseCourse();
                case 11 -> menuSave();
                case 12 -> menuLoad();
                default -> System.out.printf("Option %d not recognized%n", option);
            }
            // Pause before showing the menu again
            System.out.println("Press the Enter key to see the menu again...");
            scanner.nextLine();
            // Gets next input
            while(true) { // Loops if user enters nothing or invalid input
                System.out.println(optMessage);
                userInput = scanner.nextLine().trim();
                // Error checking
                try {
                    option = Integer.parseInt(userInput);
                    break;
                } catch (NumberFormatException e){
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            }
        }
        // Exit
        System.out.println("Thanks for using this program.\nBye!\n");
    }

    private static void menuSave() {
        String fileName;
        File file;
        do {
            do {
                System.out.println("Enter a file name: ");
                fileName = scanner.nextLine().trim();
            } while (fileName.isEmpty());
            file = new File(fileName);
        } while(file.exists() && !file.canWrite());
        menuLoad(file);
    }

    private static void menuLoad(File file) {
        if (FileSaver.save(file, data)) {
            System.out.println("Successfully saved to file");
        } else {
            System.err.println("Failed to save to file ");
        }
    }

    private static void menuLoad() {
        String fileName;
        File file;
        do {
            do {
                System.out.println("Enter a file name: ");
                fileName = scanner.nextLine().trim();
            } while (fileName.isEmpty());
            file = new File(fileName);
        } while(!file.exists() || !file.canRead());

        Data data = FileLoader.load(file);
        if (data == null) {
            System.err.printf("Failed to load data from file%s\n", fileName);
        } else {
            Menu.data = data;
        }
    }


    /*
     Methods corresponding each option on the menu...
     */

    /**
     * User adds a new course. Prompts to enter information about the course (course name, name of the
     * prof, email address (@ucalgary.ca).) The information gets stored in data.java.
     */
    private static void menuEnterNewCourse() {
        boolean success = false;
        do{
            System.out.println("Enter information about a new course!");
            String courseName = getCourseName();
            String profName = getProfName();
            String profEmail = getProfEmail();
            double targetGrade = getTargetGrade();
            try {
                success = data.storeNewCourse(courseName, profName, profEmail, targetGrade);
            } catch (Exception e) {
                System.out.println("Could not store course");
            }
            if(!success){
                System.out.println("Course already exists (course name is tracked already)!\nTry again");
            }
        }while(!success);
    }


    /**
     * User adds a target grade for a course.
     */
    private static void menuEnterCourseTargetGrade() {
        // No courses being tracked > prevent loop
        if(data.getAllCourses().isEmpty()){
            System.out.println("You have not yet added any courses.");
            return; // exits the method
        }
        boolean success = false;
        do { // loops until storing data goes successfully
            System.out.println("For which course would you like to set a goal for?");
            String courseName = getExistingCourse();
            if (data.getTargetGrade(courseName) == null) { // no target grade set for this course
                success =data.storeNewTargetGrade(courseName, getTargetGrade()); // assign a new target grade
            } else { // There is a target grade previously stored to the course
                // Concatenate grade into String (two decimals)
                String currentTarget = String.format("%.2f%%", data.getTargetGrade(courseName));
                // Asks user if they want to overwrite the current one or not
                System.out.println("The course has an existing target grade:" + currentTarget);
                boolean wantChange = yesNo("Would you like to overwrite?");
                if (wantChange) { // Overwrite only if user wishes to
                    success = data.storeNewTargetGrade(courseName, getTargetGrade()); // assign a new target grade
                } // Leave it as it is otherwise
                else{
                    return;
                }
            }
        }while(!success);
    }


    /**
     * User adds a new project to an existing course. Prompts to enter information about the project: course name,
     * project name, project weight, deadline, and if it's complete or not. The information gets stored in data.java
     */
    private static void menuEnterNewProject() {
        boolean success;

        // No courses being tracked > prevent loop
        if (data.getAllCourses().isEmpty()){
            System.out.println("You have not yet added any courses.");
            return; // exits the method
        }

        do {
            System.out.println("Enter information about a new project:");

            String courseName = getExistingCourse();
            ProjectType projectType = getProjectType();
            String projectName = getProjectName();
            double projectWeight = getProjectWeight();
            int[] projectDeadline = getProjectDeadline();

            if (projectType == ProjectType.EXAM) {
                String reviewTopics = getReviewTopics();
                String location = getLocation();
                success = data.storeNewExam(courseName, projectName, projectWeight, projectDeadline, location, reviewTopics);
            } else { // if project type is an assignment
                String specialInstructions = getSpecialInstructions();
                success = data.storeNewAssignment(courseName, projectName, projectWeight, projectDeadline, specialInstructions);
            }

        } while (!success);

    }


    private static void menuMarkProjectComplete() {

        if (data.getAllCourses().isEmpty() || data.getAllProjects().isEmpty()) {
            System.out.println("You have not yet added any courses.");
            return; // exits the method
        }
        boolean success;

        do {
            String courseName = getExistingCourse();
            String projectName = getExistingProject(courseName);

            success = data.markProjectComplete(courseName, projectName);

        } while (!success);

    }

    /**
     * User adds an actual grade for a project.
     */
    private static void menuEnterProjectGrade() {
        // No courses being tracked > prevent loop
        if(data.getAllCourses().isEmpty()){
            System.out.println("You have not yet added any courses.");
            return; // exits the method
        }
        boolean success = false;
        do { // loops until storing data goes successfully
            String courseName = getExistingCourse();
            if(data.getCourse(courseName).getAllProject().isEmpty()){
                System.out.println("You have not yet added any projects to this course.");
                return; // exits the method
            }
            String projectName = getExistingProject(courseName);
            double grade = getActualGrade();
            success = data.storeProjectGrade(courseName,projectName,grade);
        }while(!success);
    }

    private static void menuPrintPendingAssignments() {
        ArrayList<Project> projects = data.getAllProjects();
        for (Project project : projects){
            // if assignment has been marked incomplete by user, information about the assignment is printed
            if (!project.isProjectComplete()){
                System.out.print("Project: " + project.getProjectName() + " for " + project.getCourseName());
                System.out.print(" weighed " + project.getProjectWeight() + "%. of total course. ");
                System.out.println("Due on " + Arrays.toString(project.getProjectDeadline()) + ". ");
            }
        }
    }


    private static void menuPrintAllProjects() {
        ArrayList<Project> projects = new ArrayList<>();

        // Ask if they want all proj or specifically for a course
        if(yesNo("Would you like to see a list for a specific course?")){
            String courseName = getExistingCourse();
            projects = data.sortProjects(courseName);
        }
        else {
            projects = data.sortProjects();
        }
        System.out.println(PROJECT_HEADER);
        System.out.println(PROJECT_SEP);
        for (Project project : projects){
            System.out.println(project.toString());
        }
    }

    /**
     * Prints out target grades and actual grades for each course.
     */
    private static void menuPrintTargetAndActualGrade() {
        // Print header
        System.out.print(GRADE_HEADER);
        System.out.println(GRADE_SEP);

        HashMap<Course, Double> target = new HashMap<>(data.getAllTargetGrades());
        HashMap<Course, Double> actual = new HashMap<>(data.getAllActualGrades());
        String t = ""; // Target grade in String
        String a = ""; // Actual grade in String
        for(Course course: data.getAllCourses()){
            // No previously stored grades
            if(target.get(course) == 0 && actual.get(course) == 0){
                t = "--%"; // default
                a = "--%"; // default
            } // Target is null but actual isn't
            else if(target.get(course) == 0){
                t = "--%"; // default
                a = String.format("%.2f%%", actual.get(course));
            } // Target isn't null but actual is
            else if(actual.get(course) == 0){
                t = String.format("%.2f%%", target.get(course));
                a = "--%"; // default
            }// course has target and actual grade stored
            else {
                t = String.format("%.2f%%", target.get(course));
                a = String.format("%.2f%%", actual.get(course));
            }
            // Print the target/actual grade for every course stored
            System.out.printf(
                    GRADE_FORMAT, course.getCourseName(), t, a);

        }
    }

    /**
     * Print out information tracked courses
     */
    private static void menuPrintCourseInfo() {
        // Print header
        System.out.print(COURSE_HEADER);
        System.out.println(COURSE_SEP);
        // Get the courses sorted
        ArrayList<Course> sortedCourses = data.sortCourses();
        // Iterates the tracked courses
        for (Course course: sortedCourses) {
            System.out.println(course.toString()); // calls toString in ca.ucalgary.groupprojectgui.objects.Course class
        }
    }

    /**
     * Close a course (withdrawn/invalidate)
     */
    private static void menuCloseCourse(){
        // No courses being tracked > prevent loop
        if(data.getAllCourses().isEmpty()){
            System.out.println("You have not yet added any courses.");
            return; // exits the method
        }
        String courseName = getExistingCourse();
        boolean wantToClose = yesNo("Are you sure you would like to close " + courseName + "?");
        if(wantToClose){ // Close
            data.getCourse(courseName).closeCourse();
            System.out.println("Closed course.");

        }
        else{ // DON'T close
            System.out.println("Course in-progress.");
        }
        printCourseInfo(data.getCourse(courseName));
    }



    /*
     Methods used inside the option methods...
     */

    /**
     * Asks user a yes-no question
     * @param text string of text to display (question)
     * @return true for yes, false otherwise
     */
    private static boolean yesNo(String text) {
        String input;
        while(true){ // Keep looping as long as the input is empty or input is not yes or no
            System.out.println(text + "\tEnter 'yes' or 'no':");
            input = scanner.nextLine().trim().toLowerCase(); // convert to lower
            if (!input.isEmpty() && (input.equals("yes") || input.equals("no"))){
                break;
            }
            System.out.println("Invalid input.");
        }
        if (input.equals("yes")){
            return true;
        } else{
            return false;
        }
    }

    /**
     * Prints out information for a course
     * @param course ca.ucalgary.groupprojectgui.objects.Course object to print information about
     */
    private static void printCourseInfo(Course course){
        // Print header
        System.out.print(COURSE_HEADER);
        System.out.println(COURSE_SEP);
        System.out.println(course.toString());
    }


    /**
     * Prompts user to enter a course name with a minimum length of 6 and maximum length of 7.
     * @return course name (ex: MATH211, ART321)1
     */
    private static String getCourseName() {
        String courseName;
        final int MIN_LENGTH = 6;
        final int MAX_LENGTH = 7;
        while (true) {
            while (true) { // Keep looping as long as the input is empty
                System.out.println("Enter a course name (valid length:6 or 7):");
                courseName = scanner.nextLine().trim();
                if (!courseName.isEmpty()) {
                    break;
                }
                System.out.println("Invalid input.");
            }
            courseName = courseName.replace(" ", ""); // Remove all spaces
            if(courseName.length()== MIN_LENGTH || courseName.length() == MAX_LENGTH){
                break;
            } // keep looping as long as the length of the course name is not 6 or 7
            System.out.println("Invalid input.");
        }
        return courseName.toUpperCase(); // Stores as all upper string
    }

    /**
     * Prompts user to enter the name of a professor.
     * @return name of the prof (format: "First Last")
     */
    private static String getProfName() {
        // First name...
        String firstName;
        while(true){ // Keep looping as long as the input is empty
            System.out.println("Enter the first name of the professor for the course:");
            firstName = scanner.nextLine().trim();
            if(!firstName.isEmpty()){ // valid input
                break;
            } // otherwise keeps looping
            System.out.println("Please enter a non-empty string");
        }
        // Format
        firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
        // Last name...
        String lastName;
        while(true){ // Keep looping as long as the input is empty
            System.out.println("Enter the last name of the professor for the course:");
            lastName = scanner.nextLine().trim();
            if(!lastName.isEmpty()){ // valid input
                break;
            } // otherwise keeps looping
            System.out.println("Please enter a non-empty string");
        }
        // Format
        lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
        // Concatenate
        String fullName = firstName + " " + lastName;
        return fullName;
    }

    /**
     * Prompts user to enter the email address of a professor.
     * @return email address of the prof ends with @ucalgary.ca, all lowercase
     */
    private static String getProfEmail() {
        String text = "Enter @ucalgary.ca email address of the professor:";
        String profEmail;
        while(true){ // Keep looping as long as the input is empty or is not an @ucalgary.ca email address
            System.out.println(text);
            profEmail = scanner.nextLine().trim().toLowerCase(); // stores email address as all lower case
            profEmail = profEmail.replace(" ", ""); // remove excess spaces
            if(!profEmail.isEmpty()){ // valid
                if(profEmail.endsWith("@ucalgary.ca")){ // valid input
                    break;
                }
                // doesn't end with "@ucalgary.ca"
                System.out.println("Invalid email address.");
            }
            else{ // input is empty
                System.out.println("Invalid input.");
            }
        }
        return profEmail;
    }

    /**
     * Prompts user to enter an existing course name.
     * @return existing course name (String)
     */
    private static String getExistingCourse() {
        String courseName;
        boolean courseExists;
        while(true) {
            System.out.println("Enter a previously added course name:");
            courseName = scanner.nextLine().trim().toUpperCase();
            courseExists = data.checkExistCourse(courseName); // checking if the course been previously stored
            if(courseName.isEmpty()){
                System.out.println("Invalid input.");
            }
            else if(!courseExists){
                System.out.println("Course does not exist.");
            }
            else{ // valid input
                break;
            }
        }  // loop repeats if line is empty or not a previously added course
        return courseName;
    }

    /**
     * Prompts user to enter the type of project, "E" for exam or "A" for assignment.
     * @return the ca.ucalgary.groupprojectgui.ProjectType (enum)
     */
    private static ProjectType getProjectType() {
        String type;
        ProjectType projectType;

        do {
            System.out.println("Enter 'E' for exam or 'A' for assignment");
            type = scanner.nextLine().trim().toUpperCase();
        } while(!type.equals("E") && !type.equals("A"));

        if (type.equals("E")) {
            projectType = ProjectType.EXAM;
        } else {
            projectType = ProjectType.ASSIGNMENT;
        }
        return projectType;
    }


    /**
     * Prompts user to enter an existing project name under a course.
     * @return existing project name (String)
     */
    private static String getExistingProject(String courseName) {
        String projectName;
        boolean projectExists;
        do {
            System.out.println("Enter a previously added project name under the course:");
            projectName = scanner.nextLine().trim().toUpperCase();
            projectExists = data.checkProjectExistInCourse(courseName, projectName); // checking if the project been previously stored under the entered course
        } while(projectName.isEmpty() || !projectExists); // loop repeats if line is empty or not a previously added project
        return projectName;
    }

    /**
     * Prompts user to enter a project name.
     * @return project name (String)
     */
    private static String getProjectName() {
        String projectName;
        do {
            System.out.println("Enter a new project name (ex. cs_assignment1):");
            projectName = scanner.nextLine().trim().toUpperCase(); // stores project name as uppercase
        } while(projectName.isEmpty());
        return projectName;
    }

    /**
     * Prompts user to enter a project weight.
     * @return project's weight as percentage (double)
     */
    private static double getProjectWeight() {
        double projectWeight;
        String weight;
        while(true) {
            System.out.println("Enter the project's total weight percentage \n (ex. for 15.75% enter 15.75):");
            weight = scanner.nextLine();
            if(!weight.isEmpty()){
                try {
                    projectWeight = Double.parseDouble(weight);
                    if (projectWeight >= 0 && projectWeight <= 100) {
                        break; // valid input
                    } else if (projectWeight < 0) {
                        System.out.println("Project weight cannot be less than 0."); // negative weight
                    } else {
                        System.out.println("Project weight cannot be greater than 100."); // exceed 100
                    }
                }catch (Exception e) {
                    System.out.println("Invalid input."); // non-number/empty
                }
            } else{
                System.out.println("Invalid input"); //empty
            }
        }
        return projectWeight;
    }

    /**
     * Prompts user to enter a project's deadline.
     * @return project deadline in form YYYYMMDD (int[])
     */
    private static int[] getProjectDeadline() {
        String input;
        int[] deadline = new int[3]; // int array to return
        // Index numbers for the array
        final int D = 0;
        final int M = 1;
        final int Y = 2;

        while(true) {
            System.out.println("Enter the year, month, and date the project is due in DD MM YYYY format in one line.");
            System.out.println("Ex. March 15, 2024 would be written as:  15 03 2024");
            input = scanner.nextLine(); // Store input as one line of string
            String[] inputs = input.split(" "); // array of day, month, year
            try {
                if (inputs.length == 3) { // Correct number of inputs
                    for (int i = 0; i < deadline.length; i++) {
                        deadline[i] = Integer.parseInt(inputs[i]);
                    }
                    // No past dates (no earlier than 2023)
                    if (deadline[Y] >= 2023) {
                        // Months with 31 days
                        if (deadline[M] == 1 || deadline[M] == 3 || deadline[M] == 5 || deadline[M] == 7 || deadline[M] == 8 || deadline[M] == 10 || deadline[M] == 12) {
                            if (deadline[D] > 0 && deadline[D] <= 31) {
                                break; // valid
                            }
                        }
                        // Months with 30 days
                        else if (deadline[M] == 4 || deadline[M] == 6 || deadline[M] == 9 || deadline[M] == 11) {
                            if (deadline[D] > 0 && deadline[D] <= 30) {
                                break; // valid
                            }
                        }
                        // February
                        else if (deadline[M] == 2) {
                            if (deadline[D] > 0 && deadline[D] <= 29) {
                                break; // valid
                            }
                        }
                        else{
                            System.out.println("Invalid date.");
                        }
                    }
                    else {
                        System.out.println("Invalid year.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Invalid input.");
            }
        }
        return deadline;
    }

    /**
     * Prompts user to enter a project's deadline
     * @return True if project is complete, false otherwise
     */
    private static boolean getProjectCompletionStatus() {
        boolean projectCompletion = false;
        String response;
        do {
            System.out.println("Has this project been completed? (yes/no):");
            response = scanner.nextLine().trim().toUpperCase();
            if (response.equals("YES")){
                projectCompletion = true;
            } else if (!response.equals("NO")) {
                response = ""; // if user input is not "yes" or "no," loop will repeat
            }
        } while(response.isEmpty());

        return projectCompletion;
    }

    private static String getSpecialInstructions() {
        String specialInstructions;
        System.out.println("Enter a review topic for the exam, press Enter to skip.");
        specialInstructions = scanner.nextLine();
        return specialInstructions;
    }

    private static String getReviewTopics() {
        String reviewTopics;
        do {
            System.out.println("Enter a location");
            reviewTopics = scanner.nextLine();
        } while(reviewTopics.isEmpty());
        return reviewTopics;
    }

    private static String getLocation() {
        String location;
        do {
            System.out.println("Enter a location");
            location = scanner.nextLine();
        } while(location.isEmpty());
        return location;
    }


    /**
     * Prompts user to enter a target grade
     * @return target grade (Double)
     */
    private static double getTargetGrade(){
        double targetGrade;
        String input;
        while(true) {
            while(true) {
                System.out.println("Enter a target grade for the course as percentage (ex. for 80.5% enter 80.50):");
                input = scanner.nextLine();
                if(!input.isEmpty()){
                    try {
                        targetGrade = Double.parseDouble(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                else{
                    System.out.println("Invalid input.");
                }
            }
            if(targetGrade >= 0 && targetGrade <= 100){
                break;
            }
            else {
                System.out.println("Enter a percentage between 0-100");
            }
        }
        return targetGrade;
    }

    /**
     * Prompts user to enter the actual grade
     * @return actual grade (Double)
     */
    private static double getActualGrade(){
        double actualGrade;
        String input;
        while(true) {
            while(true) {
                System.out.println("Enter the actual grade for the project as percentage (ex. for 80.5% enter 80.50):");
                input = scanner.nextLine();
                if(!input.isEmpty()){
                    try {
                        actualGrade = Double.parseDouble(input);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input.");
                    }
                }
                else{
                    System.out.println("Invalid input.");
                }
            }
            if(actualGrade >= 0 && actualGrade <= 100){
                break;
            }
            else {
                System.out.println("Enter a percentage between 0-100");
            }        }
        return actualGrade;
    }

}