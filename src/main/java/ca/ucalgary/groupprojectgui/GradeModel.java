package ca.ucalgary.groupprojectgui;

public class GradeModel {
    private String course;
    private String targetGrade;
    private String actualGrade;

    public GradeModel(String courseName, String targetGrade, String actualGrade){
        this.course = courseName;
        this.targetGrade = targetGrade;
        this.actualGrade = actualGrade;
    }

    public String getCourse() {
        return course;
    }

    public String getTargetGrade() {
        return targetGrade;
    }

    public String getActualGrade() {
        return actualGrade;
    }
}
