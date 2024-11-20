package ca.ucalgary.cpsc233;

/**
 * Model object of Course (for course table)
 */
public class CourseModel {

    private String courseName;
    private String profName;
    private String profEmail;
    private String inProgress;


    private Data data; // Assuming you have a Data class to manage your data

    /**
     * Constructor
     *
     * @param courseName name of the course
     * @param profName name of prof
     * @param profEmail email of prof
     * @param inProgress true-inprogress or false-closed
     */
    public CourseModel(String courseName, String profName, String profEmail, String inProgress){
        courseName.toUpperCase();
        profEmail.toLowerCase();
        this.courseName = courseName;
        this.profName = profName;
        this.profEmail = profEmail;
        this.inProgress = inProgress;
    }

    public String getCourseName(){
        return this.courseName;
    }
    public String getProfEmail(){
        return this.profEmail;
    }
    public String getProfName(){
        return this.profName;
    }
    public String getInProgress(){
        return this.inProgress;
    }

}
