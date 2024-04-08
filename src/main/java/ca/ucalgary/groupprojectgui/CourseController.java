package ca.ucalgary.groupprojectgui;

import ca.ucalgary.groupprojectgui.Data;
import ca.ucalgary.groupprojectgui.objects.Course;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;

public class CourseController {

    @FXML
    private TableView<Course> courseTableView;

    @FXML
    private TableColumn<Course, String> courseNameColumn;

    @FXML
    private TableColumn<Course, String> professorNameColumn;

    @FXML
    private TableColumn<Course, String> professorEmailColumn;

    @FXML
    private TableColumn<Course, Boolean> inProgressColumn;

    private Data data; // Assuming you have a Data class to manage your data

    // Initialize method
    public void initialize() {
        // Associate columns with model properties
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        professorNameColumn.setCellValueFactory(new PropertyValueFactory<>("profName"));
        professorEmailColumn.setCellValueFactory(new PropertyValueFactory<>("profEmail"));
        inProgressColumn.setCellValueFactory(new PropertyValueFactory<>("inProgress"));

        // Set items to TableView
        updateTableView();
    }

    // Update TableView with data from Data class
    private void updateTableView() {
        ObservableList<Course> courses = FXCollections.observableArrayList(data.getAllCourses());
        courseTableView.setItems(courses);
    }
}
