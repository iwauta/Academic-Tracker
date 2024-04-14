package ca.ucalgary.groupprojectgui;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Model object of Project (for project table)
 */
public class ProjectModel {
    private String projectName;
    private String projectWeight;
    private String projectDeadline;
    private String projectType;
    private String projectSpecial;
    private String projectStatus;

    public  ProjectModel(String projectName, String weight, String deadline, String projectType, String special, String status){
        this.projectName = projectName;
        this.projectWeight = weight;
        this.projectDeadline = deadline;
        this.projectType = projectType;
        this.projectSpecial = special;
        this.projectStatus = status;

    }

    private final StringProperty project = new SimpleStringProperty();

    public final StringProperty projectProperty() {
        return project;
    }

    public final String getProject() {
        return project.get();
    }

    public String getProjectName() { return projectName; }

    public String projectWeight() {
        return projectWeight;
    }

    public String projectDeadline() {
        return projectDeadline;
    }

    public String projectType() {
        return projectType;
    }

    public String projectSpecial() {
        return projectSpecial;
    }

    public String projectPending() {
        return projectSpecial;
    }

}
