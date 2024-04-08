package ca.ucalgary.groupprojectgui.objects;

import java.util.Objects;

public class Project implements Comparable<Project>{

    private String courseName;
    private String projectName;
    private double projectWeight;
    private double projectGrade;
    private int[] projectDeadline;
    private boolean projectCompletion;

    // Static constants
    // ca.ucalgary.groupprojectgui.objects.Project info formatter
    public static final String PROJECT_FORMAT = "%-8s    %-15s    %-20s    %-9s    %-15s";
    // Index numbers for deadline
    static final int INDEX_DAY = 0;
    static final int INDEX_MONTH = 1;
    static final int INDEX_YEAR = 2;

    public Project(String courseName, String projectName, double projectWeight, int[] projectDeadline) {
        this.courseName = courseName;
        this.projectName = projectName;
        this.projectWeight = projectWeight;
        this.projectGrade = 0;
        this.projectDeadline = projectDeadline;
        this.projectCompletion = false;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getProjectName() {
        return projectName;
    }

    public double getProjectWeight() {
        return projectWeight;
    }

    public int[] getProjectDeadline() {
        return projectDeadline;
    }

    public String deadlineToString(){
        String deadline = String.format("%d/%d/%d",projectDeadline[INDEX_DAY],projectDeadline[INDEX_MONTH],projectDeadline[INDEX_YEAR]);
        return deadline;
    }

    public boolean isProjectComplete() {
        return projectCompletion;
    }

    public double getProjectGrade() {
        return projectGrade;
    }

    public void setProjectGrade(double projectGrade) {
        this.projectGrade = projectGrade;
    }

    public void setProjectComplete() {
        projectCompletion = true;
    }

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
