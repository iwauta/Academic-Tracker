package ca.ucalgary.groupprojectgui;

import ca.ucalgary.groupprojectgui.Data;
import ca.ucalgary.groupprojectgui.objects.Course;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

/**
 * Model object of Course (for course table)
 */
public class CourseController {

    private String courseName;
    private String profName;
    private String profEmail;
    private boolean inProgress;


    private Data data; // Assuming you have a Data class to manage your data

    /**
     * Construcor
     *
     * @param courseName
     * @param profName
     * @param profEmail
     * @param inProgress
     */
    public CourseController(String courseName, String profName, String profEmail, boolean inProgress){
        this.courseName = courseName;
        this.profName = profName;
        this.profEmail = profEmail;
        this.inProgress = inProgress;
    }

    // Update TableView with data from Data class
    private void updateTableView() {
        ObservableList<Course> courses = FXCollections.observableArrayList(data.getAllCourses());
        courseTableView.setItems(courses);
    }
}
