package ca.ucalgary.groupprojectgui;

public class GradeModel {
    private String courseName;
    private String targetGrade;
    private String actualGrade;

    public GradeModel(String courseName, String targetGrade, String actualGrade){
        this.courseName = courseName;
        this.targetGrade = targetGrade;
        this.actualGrade = actualGrade;
    }
}
