package ca.ucalgary.cpsc233.objects;

import java.util.Objects;

/**
 * Project.java - Represents Project object.
 *
 * @author Utaha Iwai
 * @tutorial T09
 * @email utaha.iwai@ucalgary.ca
 */
public class Project implements Comparable<Project>{

    // Properties
    private String courseName;
    private String projectName;
    private double projectWeight;
    private double projectGrade;
    private int[] projectDeadline; // array representation of deadline {day, month, year}
    private boolean projectCompletion;

    // Static constants
    // ca.ucalgary.groupprojectgui.objects.Project info formatter
    public static final String PROJECT_FORMAT = "%-8s    %-15s    %-20s    %-9s    %-15s";
    // Index numbers for deadline
    static final int INDEX_DAY = 0;
    static final int INDEX_MONTH = 1;
    static final int INDEX_YEAR = 2;

    /**
     * Constructor
     * @param courseName name of the course
     * @param projectName name of the project
     * @param projectWeight percentage weight of the project
     * @param projectDeadline deadline {day, month, year}
     */
    public Project(String courseName, String projectName, double projectWeight, int[] projectDeadline) {
        this.courseName = courseName;
        this.projectName = projectName;
        this.projectWeight = projectWeight;
        this.projectGrade = 0;
        this.projectDeadline = projectDeadline;
        this.projectCompletion = false;
    }


    /* Getters... /

    /**
     * Getter for courseName
     * @return courseName
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Getter for projectName
     * @return projectName
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Getter for projectWeight
     * @return projectWeight
     */
    public double getProjectWeight() {
        return projectWeight;
    }

    /**
     * Getter for projectDeadline
     * @return array representation of deadline {day, month, year}
     */
    public int[] getProjectDeadline() {
        return projectDeadline;
    }

    /**
     * Getter for deadline (in a form of String)
     * @return deadline "dd/mm/yyyy"
     */
    public String deadlineToString(){
        String deadline = String.format("%d/%d/%d",projectDeadline[INDEX_DAY],projectDeadline[INDEX_MONTH],projectDeadline[INDEX_YEAR]);
        return deadline;
    }

    /**
     * Getter for project completion status
     * @return true-complete or false-pending
     */
    public boolean isProjectComplete() {
        return projectCompletion;
    }

    /**
     * Getter for grade
     * @return projectGrade
     */
    public double getProjectGrade() {
        return projectGrade;
    }


    /* Setters...*/

    /**
     * Setter for projectName
     * @param projectName name of the project
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Setter for projectWeight
     * @param projectWeight percent weight of the project
     */
    public void setProjectWeight(double projectWeight) {
        this.projectWeight = projectWeight;
    }

    /**
     * Setter for projectDeadline
     * @param projectDeadline deadline of the project in an array {day, month, year}
     */
    public void setProjectDeadline(int[] projectDeadline) {
        this.projectDeadline = projectDeadline;
    }

    /**
     * Setter for grade
     * @param projectGrade grade received in percentage
     */
    public void setProjectGrade(double projectGrade) {
        this.projectGrade = projectGrade;
    }

    /**
     * Marks the project complete
     */
    public void setProjectComplete() {
        projectCompletion = true;
    }



    /* Override from Object... */
    /**
     * Represents ca.ucalgary.groupprojectgui.objects.Project object as String (Formatted: "COURSE_NAME", "PROJECT_NAME","DEADLINE","WEIGHT", "STATUS")
     * @return String representation of ca.ucalgary.groupprojectgui.objects.Project
     */
    @Override
    public String toString(){
        // Get withdrawn status
        String status = "COMPLETE";
        if(!projectCompletion) { // project is not complete
            status = "INCOMPLETE";
        }
        // Convert deadline into string
        String deadline = projectDeadline[INDEX_DAY] + "/" + projectDeadline[INDEX_MONTH] + "/" + projectDeadline[INDEX_YEAR];
        // Convert weight into string
        String weight = String.format("%.2f%%", projectWeight);
        // Print the info for every project stored
        String project = String.format(PROJECT_FORMAT, courseName, projectName, deadline, weight, status);
        return project;
    }

    /**
     * Compares two ca.ucalgary.groupprojectgui.objects.Project objects based on their deadlines (in chronological order)
     * @param other the object to be compared
     * @return returns:
     *      negative if 'this' is earlier than 'other'
     *      positive if 'this' is later than 'other'
     *      0 if they are equal
     */
    @Override
    public int compareTo(Project other) {
        // Compare by year first
        int result = Integer.compare(this.projectDeadline[INDEX_YEAR], other.projectDeadline[INDEX_YEAR]);
        if(result != 0){return result;}
        // Same year, compare by month
        result =  Integer.compare(this.projectDeadline[INDEX_MONTH], other.projectDeadline[INDEX_MONTH]);
        if(result != 0){return result;}
        // Same month, compare by day
        result =  Integer.compare(this.projectDeadline[INDEX_DAY], other.projectDeadline[INDEX_DAY]);
        return result;
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
            return false; // 'other' is not ca.ucalgary.groupprojectgui.objects.Project object > false
        }
        Project project = (Project) other; // cast other
        // only true if every property is equal
        return Objects.equals(courseName, project.courseName) &&
                Objects.equals(projectName, project.projectName) &&
                Objects.equals(projectWeight, project.projectWeight)&&
                Objects.equals(projectDeadline, project.projectDeadline) &&
                Objects.equals(projectGrade, project.projectGrade)&&
                Objects.equals(projectCompletion, project.projectCompletion);
    }

    /**
     * used for equals
     * @return hashed 'this'
     */
    @Override
    public int hashCode() {
        return Objects.hash(courseName, projectName, projectWeight, projectGrade, projectDeadline, projectCompletion);
    }

}
