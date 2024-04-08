package ca.ucalgary.groupprojectgui;

import ca.ucalgary.groupprojectgui.objects.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * CPSC 233 W24 ca.ucalgary.groupprojectgui.objects.Project ca.ucalgary.groupprojectgui.Data.java - stores the data to be tracked. (course info, projects, grades, etc.)
 *
 * @author Utaha Iwai, Dipti Kumar
 * @tutorial T09
 * @email utaha.iwai@ucalgary.ca, dipti.kumar@ucalgary.ca
 */
public class Data {
    // Properties
    // ArrayList of course objects
    private ArrayList<Course> courses;
    // ArrayList of course objects
    private ArrayList<Project> projects;
    // HashMap of <course> : <target grade>
    private HashMap<Course, Double> targetGrades;
    // HashMap of <course> : <actual grade>
    private HashMap<Course, Double> actualGrades;


    /**
     * Constructor
     */
    public Data(){
        courses = new ArrayList<>();
        projects = new ArrayList<>();
        targetGrades = new HashMap<>();
        actualGrades = new HashMap<>();
    }


    /**
     * Stores information of a course into an array and adds the array to the ArrayList of courses. (Checks if the
     * course is previously stored.)
     *
     * @param courseName added course
     * @param profName name of the professor of the added course
     * @param profEmail @ucalgary.ca email address of the professor of the added course
     * @return true (added successfully) or false (otherwise)
     */
    public boolean storeNewCourse(String courseName, String profName, String profEmail, Double targetGrade) throws Exception {
        // Adds to the list unless the course already exists in the list
        if (!checkExistCourse(courseName)) {
            Course course = new Course(courseName, new Professor(profName, profEmail),targetGrade);
            this.courses.add(course); // Added a new course
            Double actualGrade = course.getActualGrade();
            targetGrades.put(course, targetGrade); // Store targetGrade for this course
            actualGrades.put(course, actualGrade); // store actual grade for this course
//            System.out.printf("Stored a new course %s!%n", courseName);
            return true;
        } else { // Failed to add the course (the course already exists)
            throw new Exception("Could not store course");
//            System.out.printf("Error in storing course %s. Check information entered.%n", courseName);
//            return false;
        }
    }

    /**
     * Checks if a course already exists in the list by checking the existence of the course name.
     * @param courseName name of course to check
     * @return true (already exists) or false (does not exist)
     */
    public  boolean checkExistCourse(String courseName) {
        for(Course course: courses){
            // ca.ucalgary.groupprojectgui.objects.Course object with the same name is already in the list
            if(course.getCourseName().equals(courseName)){
                return true;
            }
        } // No course with the same course name found
        return false;
    }

    /**
     * Stores a project object exam and adds the array to the ArrayList of projects. (Checks if the
     * project is previously stored.)
     *
     * @param courseName      existing course
     * @param projectName     name of the project in the existing course
     * @param projectWeight   percentage of the project's total weight in the course
     * @param projectDeadline integer array of project deadline in the form {YYYY, MM, DD}
     * @param location
     * @return true (added successfully) or false (otherwise)
     */
    public  boolean storeNewExam(String courseName, String projectName, double projectWeight, int[] projectDeadline, String location, String reviewTopics) {
        boolean stored;
        if (!checkProjectExistInCourse(courseName, projectName) || checkExistCourse(courseName)) { // the project is added to projects if it is not already in the ArrayList
            Project project = new Exam(courseName, projectName, projectWeight, projectDeadline, location, reviewTopics);

            // 2d list with project objects
            projects.add(project);
            getCourse(courseName).addProject(project);
            System.out.printf("Stored a new exam %s in %s!\n", projectName, courseName);
            stored = true;
        } else {
            System.out.printf("Error in storing project %s in %s. Check information entered.\n", projectName, courseName + "\n");
            stored = false;
        }
        return stored;
    }

    /**
     * Stores a project object exam and adds the array to the ArrayList of projects. (Checks if the
     * project is previously stored.)
     *
     * @param courseName existing course
     * @param projectName name of the project in the existing course
     * @param projectWeight percentage of the project's total weight in the course
     * @param projectDeadline integer array of project deadline in the form {YYYY, MM, DD}
     * @return true (added successfully) or false (otherwise)
     */
    public  boolean storeNewAssignment(String courseName, String projectName, double projectWeight, int[] projectDeadline, String specialInstructions) {
        boolean stored;
        // the project is added to projects if it is not already in the ArrayList
        if (!checkProjectExistInCourse(courseName, projectName) || checkExistCourse(courseName)) {
            Project project = new Assignment(courseName, projectName, projectWeight, projectDeadline, specialInstructions);

            // 2d list with project objects
            projects.add(project);
            getCourse(courseName).addProject(project);
            System.out.printf("Stored a new assignment %s in %s!\n", projectName, courseName);
            stored = true;
        } else {
            System.out.printf("Error in storing project %s in %s. Check information entered.\n", projectName, courseName + "\n");
            stored = false;
        }
        return stored;
    }


    /**
     * Checks if a project already exists in the list of the given course by checking the existence of the course
     * name and the project name.
     * @param courseName name of course to check
     * @param projectName name of project to check
     * @return true (already exists) or false (does not exist)
     */
    public  boolean checkProjectExistInCourse(String courseName, String projectName) {
        boolean projectExists = false;
        for (Project project : projects) { // iterates through ArrayList of projects


            if (project.getCourseName().equals(courseName) && project.getProjectName().equals(projectName)) { // if one project's info matches with info entered
                projectExists = true;
                break;

            }
        }
        return projectExists;
    }

    /**
     * Marks a project complete
     * @param courseName course name as a String
     * @param projectName project name as a String
     * @return true if project was marked complete, False otherwise
     */
    public  boolean markProjectComplete(String courseName, String projectName) {
        boolean markedComplete = false;
        if (checkProjectExistInCourse(courseName, projectName)) { // if the project matches the course and project name entered by user
            for(Project project: projects){
                if(project.getCourseName().equals(courseName) && project.getProjectName().equals(projectName)){
                    project.setProjectComplete();
                    System.out.println("Project marked complete!");
                    return true;
                }
            }
            return false; // failed for some reason...
        }
        else { // no such project in the course
            System.out.println("Project not marked complete, try again.");
            return false;
        }
    }

    /**
     * Stores a target grade for a course.
     * @param courseName course name in String.
     * @param targetGrade target grade in Double.
     * @return true (stored successfully) or false otherwise
     */
    public  boolean storeNewTargetGrade(String courseName, Double targetGrade){
        boolean stored;
        if(checkExistCourse(courseName)){
            getCourse(courseName).setTargetGrade(targetGrade);
            targetGrades.put(getCourse(courseName), targetGrade);
            System.out.printf("Stored a new goal for %s: %.2f%%!\n", courseName, targetGrade);
            stored = true;
        } else {
            System.out.println("Sorry, we weren't able to add a target grade.");
            stored = false;
        }
        return stored;
    }

    /**
     * Stores an actual grade for a project.
     * @param courseName course name in String.
     * @param projectName name of the project to set a grade for
     * @param actualGrade actual grade in double
     * @return true (stored successfully) or false otherwise
     */
    public  boolean storeProjectGrade(String courseName, String projectName, Double actualGrade){
        if(checkExistCourse(courseName)){
            if(checkProjectExistInCourse(courseName,projectName)){
                for(Project project: projects){
                    if(project.getCourseName().equals(courseName) && project.getProjectName().equals(projectName)){
                        project.setProjectGrade(actualGrade);
                        return true;
                    }
                }
            }
        }
        System.out.println("Something went wrong, please try again.");
        return false;
    }

    /**
     * Creates a list of cloned ca.ucalgary.groupprojectgui.objects.Course objects sorted.
     * @return sorted list of courses (shallow clone of them) in such order where in-progress courses come first, closed
     * ones after, each alphabetically.
     */
    public  ArrayList<Course> sortCourses(){
        // Clone of the list of the courses to return
        ArrayList<Course> sortedCourses = new ArrayList<>();

        // Divide into to groups based on status
        ArrayList<Course> inProgressCourses = new ArrayList<>(getInProgressCourses());
        ArrayList<Course> closedCourses = new ArrayList<>(getClosedCourses());

        // Sort each alphabetically
        Collections.sort(inProgressCourses);
        Collections.sort(closedCourses);

        // Combine them together
        for(Course course:inProgressCourses){
            sortedCourses.addLast(course);
        }
        for(Course course:closedCourses){
            sortedCourses.addLast(course);
        }
        return sortedCourses;
    }

    /**
     * Creates an ArrayList of Courses with 'in-progress' status.
     *
     * @return ArrayList of Courses with 'in-progress' status
     */
    private  ArrayList<Course> getInProgressCourses() {
        ArrayList<Course> inProgressCourses = new ArrayList<>();
        for(Course course: courses) {
            if (course.isInProgress()) {
                inProgressCourses.add(course);
            }
        }
        return inProgressCourses;
    }

    /**
     * Creates an ArrayList of Courses with 'closed' status.
     *
     * @return ArrayList of Courses with 'closed' status
     */
    private  ArrayList<Course> getClosedCourses() {
        ArrayList<Course> closedCourses = new ArrayList<>();
        for(Course course: this.courses) {
            if (!course.isInProgress()) {
                closedCourses.add(course);
            }
        }
        return closedCourses;
    }

    /**
     * Sorts the entire ArrayList of Projects.
     * @return Sorted list of all the projects (incomplete > complete, chronologically)
     */
    public  ArrayList<Project> sortProjects() {
        // Clone of the list of the projects to return
        ArrayList<Project> sortedProjects = new ArrayList<>();
        // Divide into to groups based on status
        ArrayList<Project> inProgressProjects = new ArrayList<>(getInProgressProjects(projects));
        ArrayList<Project> completedProjects = new ArrayList<>(getCompletedProjects(projects));

        // Sort each alphabetically
        Collections.sort(inProgressProjects);
        Collections.sort(completedProjects);

        // Combine them together
        for(Project project:inProgressProjects){
            sortedProjects.addLast(project);
        }
        for(Project project:completedProjects){
            sortedProjects.addLast(project);
        }
        return sortedProjects;
    }

    /**
     * Sorts an ArrayList of Projects for a specific course.
     * @param courseName name of the course to sort the list of the projects for
     * @return Sorted list of all the projects in the course(incomplete > complete, chronologically)
     */
    public  ArrayList<Project> sortProjects(String courseName) {
        ArrayList<Project> unsortedProjects = new ArrayList<>(getCourse(courseName).getAllProject());
        // Clone of the list of the projects to return
        ArrayList<Project> sortedProjects = new ArrayList<>();
        // Divide into to groups based on status
        ArrayList<Project> inProgressProjects = new ArrayList<>(getInProgressProjects(unsortedProjects));
        ArrayList<Project> completedProjects = new ArrayList<>(getCompletedProjects(unsortedProjects));

        // Sort each alphabetically
        Collections.sort(inProgressProjects);
        Collections.sort(completedProjects);

        // Combine them together
        for(Project project:inProgressProjects){
            sortedProjects.addLast(project);
        }
        for(Project project:completedProjects){
            sortedProjects.addLast(project);
        }
        return sortedProjects;
    }

    /**
     * Creates an ArrayList of Projects with 'incomplete' status.
     *
     * @return ArrayList of Projects with 'incomplete' status
     */
    private  ArrayList<Project> getInProgressProjects(ArrayList<Project> projects){
        ArrayList<Project> inProgressProjects = new ArrayList<>();
        for(Project project: projects) {
            if (!project.isProjectComplete()) {
                inProgressProjects.add(project);
            }
        }
        return inProgressProjects;
    }

    /**
     * Creates an ArrayList of Projects with 'complete' status.
     *
     * @return ArrayList of Projects with 'complete' status
     */
    private  ArrayList<Project> getCompletedProjects(ArrayList<Project> projects){
        ArrayList<Project> completedProject = new ArrayList<>();
        for(Project project: projects) {
            if (project.isProjectComplete()) {
                completedProject.add(project);
            }
        }
        return completedProject;
    }



    // Getter methods...

    /**
     * Getter for the list of courses
     * @return courses (ArrayList of course information)
     */
    public  ArrayList<Course> getAllCourses() {
        return courses;
    }

    /**
     * Getter for a course object. Assumes that a course with the given courseName exists.
     * @param courseName course name in String
     * @return course object with the provided name
     */
    public Course getCourse(String courseName){
        for(Course course: this.courses){
            // ca.ucalgary.groupprojectgui.objects.Course object with the same name is already in the list
            if(course.getCourseName().equals(courseName)){
                return course;
            }
        }
        System.out.println("Course not found.");
        return null; // shouldn't happen
    }

    /**
     * Getter for the list of project information
     * @return courses (ArrayList of course information)
     */
    public  ArrayList<Project> getAllProjects() {
        return projects;
    }

    /**
     * Getter for a target grade for a course. Returns null if target grade for the given course was null
     * @param courseName course name in String
     * @return target grade for the provided course (null if target grade hasn't been stored previously)
     */
    public  Double getTargetGrade(String courseName){
        if(checkExistCourse(courseName)){
            for(Course course: courses){
                if(course.getCourseName().equals(courseName)){
                    return course.getTargetGrade();
                }
            }
        }
        return null;
    }

    /**
     * Getter for a actual grade for a course. Returns null if actual grade for the given course was null
     * @param courseName course name in String
     * @return actual grade for the provided course (null if actual grade hasn't been stored previously)
     */
    public  Double getActualGrade(String courseName){
        if(checkExistCourse(courseName)){
            for(Course course: courses){
                if(course.getCourseName().equals(courseName)){
                    course.calculateActualGrade();
                    return course.getActualGrade();
                }
            }
        }
        return null;
    }

    /**
     * Getter for the entire HashMap of the target grades
     * @return HashMap of [course name (String)]:[target grade (Double)]
     */
    public  HashMap<Course, Double> getAllTargetGrades(){
        return targetGrades;
    }

    /**
     * Getter for the entire HashMap of the actual grades
     * @return HashMap of [course name (String)]:[actual grade (Double)]
     */
    public  HashMap<Course, Double> getAllActualGrades(){
        for(Course course: courses){
            course.calculateActualGrade();
        }
        return actualGrades;
    }

}
