package ca.ucalgary.groupprojectgui;

/**
 * Model object of Project (for project table)
 */
public class ProjectModel {
    private String projectName;
    private String projectWeight;
    private String projectDeadline;
    private String projectPending;
    private String projectSpecial;
    private String projectType;

    public  ProjectModel(String projectName, String courseName, String weight, String deadline, String projectType,String special,String pending){
        this.projectName = String.format("%s(%s)",projectName,courseName);
        this.projectWeight = weight;
        this.projectDeadline = deadline;
        this.projectType = projectType;
        this.projectSpecial = special;
        this.projectPending = pending;
    }


}
