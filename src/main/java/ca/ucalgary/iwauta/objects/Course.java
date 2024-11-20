package ca.ucalgary.cpsc233.objects; /**
 * ca.ucalgary.groupprojectgui.objects.Course.java - represents ca.ucalgary.groupprojectgui.objects.Course otherect with properties regarding the course
 */

import java.util.ArrayList;
import java.util.Objects;

/**
 * Course.java - Represents Course object.
 *
 * @author Utaha Iwai
 * @tutorial T09
 * @email utaha.iwai@ucalgary.ca
 */
public class Course implements Comparable<Course> {

    // Properties
    private String courseName;
    private Professor professor; // Professor object
    private ArrayList<Project> projects;    // ArrayList of Project objects
    private Double targetGrade,actualGrade;
    private boolean inProgress; // status of the course (true - in-progress, false - withdrawn/closed)

    // Static constants
    // ca.ucalgary.groupprojectgui.objects.Course info formatter
    public static final String COURSE_FORMAT = "%-11s    %-20s   %-25s    %-17s%n";

    /**
     * Constructor
     *
     * @param courseName name of the course
     * @param professor professor for this course
     * @param targetGrade target grade for the course
     */
    public Course(String courseName, Professor professor, Double targetGrade) {
        this.courseName = courseName;
        this.professor = professor;
        this.projects = new ArrayList<Project>();
        this.targetGrade = targetGrade;
        this.actualGrade = new Double(0);
        this.inProgress = true;
    }


    /* Getters... /

    /**
     * Getter method for the name of the course
     * @return name of the course
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Getter method for the name of the professor
     * @return professor of this course
     */
    public String getProfName(){
        return professor.getName();
    }

    /**
     * Getter method for the email address of the professor
     * @return professor of this course
     */
    public String getProfEmail(){
        return professor.getEmail();
    }

    /**
     * Getter method for the list of the project
     * @return arrayList of the projects for the course
     */
    public ArrayList<Project> getAllProject() {
        return projects;
    }

    /**
     * Getter method for the status
     * @return whether the course is ongoing or not
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Getter method for targetGrade.
     * @return targeted grade for the course. If target hasn't been set previously return 0 by default.
     */
    public double getTargetGrade() {
        return targetGrade;
    }

    /**
     * Getter method for actualGrade. Automatically calculates the grade when called so should be current.
     * @return current grade for the course
     */
    public double getActualGrade() {
        calculateActualGrade();
        return actualGrade;
    }


    /* Setters... /

    /**
     * Setter method for courseName
     * @param courseName name of the course
     */
    public void setCourseName(String courseName){
        this.courseName = courseName;
    }

    /**
     * Setter method for professor
     * @param professor ca.ucalgary.groupprojectgui.objects.Professor otherect representing the prof of the course
     */
    public void setProfessor(Professor professor){
        this.professor = professor;
    }

    /**
     * Closes the course (sets the status of the course to not-in-progress.)
     */
    public void closeCourse(){
        this.inProgress = false;
    }

    /**
     * set course status to In-Progress
     */
    public void setInProgress() {
        this.inProgress = true;
    }

    /**
     * Sets a target grade for the course.
     * @param targetGrade targeted grade for the course
     */
    public void setTargetGrade(double targetGrade) {
        this.targetGrade = targetGrade;
    }


    /* Add/remove projects... */

    /**
     * Adds a project to the course
     * @param project Project object to add to the course
     */
    public void addProject(Project project){
        this.projects.add(project);
    }

    /**
     * Removes a project to the course
     * @param project object to remove from the course
     */
    public void removeProject(Project project){
        this.projects.remove(project);
    }


    /* Calculate grade... */

    /**
     * Calculates the actual grade for the course using project data. (Called in getActualGrade)
     */
    public void calculateActualGrade() {
        actualGrade = (double) 0; // reset grade
        double divisor = 100.0;
        for(Project project: projects){
            // get proportion of the project in the course's final grade
            double weight = (project.getProjectWeight()) / divisor;
            actualGrade += weight * project.getProjectGrade();
        }
    }

    /* Override from Object... */

    /**
     * Represents ca.ucalgary.groupprojectgui.objects.Course otherect as String (Formatted: "COURSE_NAME", "PROF_NAME","PROF_EMAIL","STATUS"
     * @return
     */
    @Override
    public String toString(){
        // Get withdrawn status
        String status = "IN-PROGRESS";
        if(!isInProgress()) { // ca.ucalgary.groupprojectgui.objects.Course is closed
            status = "CLOSED";
        } // Print the info for every course stored
        String course = String.format(COURSE_FORMAT, courseName, professor.getName(), professor.getEmail(), status);
        return course;
    }

    /**
     * Compares two ca.ucalgary.groupprojectgui.objects.Course otherects based on their courseName
     * @param other the otherect to be compared
     * @return returns:
     *      negative if 'this' is less than 'other'
     *      positive if 'this' is larger than 'other'
     *      0 if they are equal (lexicographically)
     */
    @Override
    public int compareTo(Course other) {
        return this.courseName.compareTo(other.courseName);
    }

    /**
     * Deep equals. compare contents of the object
     * @param other other object to compare to
     * @return true if the contents are equal or false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true; // Same reference > true
        }
        if (other == null || getClass() != other.getClass()){
            return false; // other is not ca.ucalgary.groupprojectgui.objects.Course object > false
        }
        Course course = (Course) other; // cast other
        // only true if every property is equal
        return Objects.equals(courseName, course.courseName) &&
                Objects.equals(professor, course.professor) &&
                Objects.equals(targetGrade, course.targetGrade);
    }

    /**
     * used for equals
     * @return hashed 'this'
     */
    @Override
    public int hashCode() {
        return Objects.hash(courseName, professor, targetGrade);
    }
}

