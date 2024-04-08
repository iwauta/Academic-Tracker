package ca.ucalgary.groupprojectgui;

/**
 * Model object of Project (for project table)
 */
public class ProjectModel {
    private String courseName;
    private String projectName;
    private String weight;
    private String deadline;
    private String pending;
    private String special;

    public  ProjectModel(String courseName, String projectName, String weight, String deadline, String special,String pending){
        this.courseName = courseName;
        this.projectName = projectName;
        this.weight = weight;
        this.deadline = deadline;
        this.special = special;
        this.pending = pending;
    }


}
