package ca.ucalgary.groupprojectgui;

import ca.ucalgary.groupprojectgui.objects.Course;
import ca.ucalgary.groupprojectgui.util.FileLoader;
import ca.ucalgary.groupprojectgui.util.FileSaver;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;

public class HelloController {

    Data data = new Data();

    private String defaultDirectory = "data.csv";

    @FXML
    private Label status;

    // course details ////////
    @FXML
    private TableView<CourseModel> courseTableView;

    @FXML
    private CheckBox inProgressOnlyCheckBox;

    // List of models of Course objects
    private ArrayList<CourseModel> courseData;

    @FXML
    private TableColumn<CourseModel, String> courseNameColumn;

    @FXML
    private TableColumn<CourseModel, String> profNameColumn;

    @FXML
    private TableColumn<CourseModel, String> profEmailColumn;

    @FXML
    private TableColumn<CourseModel, String> inProgressColumn;

    // project details ///////

    private ChoiceBox<String> coursePickTF = new ChoiceBox();

    private ChoiceBox<String> projectTypeTF = new ChoiceBox();

    private TextField projectNameTF = new TextField();
    private TextField projectWeightTF = new TextField();

    private DatePicker projectDeadlineTF = new DatePicker();
    private TextField examLocationTF = new TextField();




    @FXML
    public void initialize() {
        // Associate columns with model properties
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        profNameColumn.setCellValueFactory(new PropertyValueFactory<>("profName"));
        profEmailColumn.setCellValueFactory(new PropertyValueFactory<>("profEmail"));
        inProgressColumn.setCellValueFactory(new PropertyValueFactory<>("inProgress"));

        // Populate TableView with data&initialize the table
        courseData = generateCourseTableContents(false); // Implement this method to fetch data from your Data class
        courseTableView.getItems().addAll(courseData);
    }


    // Courses

    /**
     * Constructs a course entered by the user
     * Tales user to new stage to add course details
     * @param actionEvent user selects "Add Course"
     */
    @FXML
    void addCourse(ActionEvent actionEvent) {
        Stage stage = new Stage();
        VBox newRoot = new VBox();

        TextField courseNameTextField = new TextField();
        courseNameTextField.setPromptText("Enter the course name:");
        TextField profNameTextField = new TextField();
        profNameTextField.setPromptText("Enter the professor's name:");
        TextField profEmailTextField = new TextField();
        profEmailTextField.setPromptText("Enter the professor's email address (ending in @ucalgary.ca):");
        TextField targetGradeTextField = new TextField();
        targetGradeTextField.setPromptText("Enter your target grade for this course:");

        Button add = new Button("Add Course");
        add.setOnAction(event -> {
            boolean success = constructCourse(courseNameTextField.getText(), profNameTextField.getText(),
                    profEmailTextField.getText(), targetGradeTextField.getText());
            if (success) {
                // Successfully added a course, update the list
                updateCourseList();
                stage.close();
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> stage.close());

        newRoot.getChildren().addAll(courseNameTextField, profNameTextField, profEmailTextField,
                targetGradeTextField, add, cancel);

        Scene newScene = new Scene(newRoot, 400, 400);
        stage.setScene(newScene);
        stage.setTitle("Add a course:");
        stage.show();
    }

    /**
     * Constructs a course object based on user input; update status based on errors if there was any.
     * @return true if adding a course was successful or false otherwise
     */
    private boolean constructCourse(String courseName, String profName, String profEmail, String targetGrade) {
        final int MIN_LENGTH = 6;
        final int MAX_LENGTH = 7;
        Double target;
        // Empty input
        if (courseName.isEmpty() || profName.isEmpty() || profEmail.isEmpty() || targetGrade.isEmpty()) {
            errorStatus("Invalid input. Make sure no fields are empty.");
            return false;
        }
        courseName = courseName.replace(" ", ""); // Remove all spaces
        // Course name too short or too long
        if (courseName.length() < MIN_LENGTH || courseName.length() > MAX_LENGTH) {
            errorStatus("Invalid course name.");
            return false;
        } else if (!profEmail.endsWith("@ucalgary.ca")) {
            errorStatus("Invalid email address.");
            return false;
        } else if (data.checkExistCourse(courseName)) {
            errorStatus("Course already tracked.");
            return false;
        }
        try {
            target = Double.parseDouble(targetGrade);
            if (target < 0 || target > 100) {
                errorStatus("Invalid target grade.");
                return false;
            }
        } catch (NumberFormatException e) {
            errorStatus("Invalid target grade. Please enter a number.");
            return false;
        }

        // Adding course
        try {
            courseName.toUpperCase();
            profEmail.toLowerCase();
            boolean success= data.storeNewCourse(courseName, profName, profEmail, target);
            if(success) {
                successStatus("Course stored.");
                return true;
            }
        } catch (Exception e) {
            errorStatus("Could not store course.");
        }
        return false;
    }

    /**
     * Controls list of Course models for the
     * @param inProgressOnly want to display in-progress courses only(true) or not
     * @return list of CourseControllers
     */
    private ArrayList<CourseModel> generateCourseTableContents(boolean inProgressOnly){
        ArrayList<Course> courses = data.sortCourses();
        // ArrayList of CourseControllers to return
        ArrayList<CourseModel> courseModels = new ArrayList<>();
        if(inProgressOnly) {
            for (Course course : courses) {
                if (!course.isInProgress()) { // course is closed
                    courses.remove(course); // remove from the list
                }
            }
        }
        // Add to the list to return
        for (Course course : courses) {
            String inProgress;
            if(course.isInProgress()){
                inProgress = "IN-PROGRESS";
            } else{
                inProgress = "CLOSED";
            }
            CourseModel courseModel = new CourseModel(course.getCourseName(), course.getProfName(), course.getProfEmail(), inProgress);
            courseModels.add(courseModel);
        }

        return courseModels;
    }

    /**
     * Updates the course list
     */
    private void updateCourseList() {
        // Clear the existing items in the TableView
        courseTableView.getItems().clear();
        courseData.clear();
        // Show In-Progress courses only
        if(inProgressOnlyCheckBox.isSelected()){
            courseData = generateCourseTableContents(true); // Implement this method to fetch data from your Data class
        } else{ // show all the courses
            courseData = generateCourseTableContents(false); // Implement this method to fetch data from your Data class
        }
        courseTableView.getItems().addAll(courseData);
    }

    /**
     * Updates the table when user checks on/off the checkbox
     * @param event checking on/off the checkbox
     */
    @FXML
    void checkInProgressOnly(ActionEvent event){
        updateCourseList();
    }



    // Projects

    /**
     * Tales user to new stage to add course details
     * @param actionEvent user selects "Add Course"
     */
    @FXML
    void addProject(ActionEvent actionEvent) {
        // new scene opens to construct a course
        Stage stage = new Stage();
        VBox newRoot = new VBox();

        ArrayList<Course> courses = data.getInProgressCourses();
        String courseName;
        for (Course course : courses) {
            if (course.isInProgress()) {
                courseName = course.getCourseName();
                coursePickTF.getItems().add(courseName);
            }
        }

        projectTypeTF.getItems().addAll("EXAM", "ASSIGNMENT");

        projectNameTF.setPromptText("Enter the project name:");

        projectWeightTF.setPromptText("Enter the project weight:");

        Scene newScene = new Scene(newRoot, 400, 400);

        stage.setScene(newScene);
        stage.setTitle("Add a Project:");
        stage.show();

        Button add = new Button("Add Project");

        add.setOnAction(event -> constructProject()); // course is constructing using user input

        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> stage.close()); // cancel exists course constructor

        newRoot.getChildren().addAll(coursePickTF, projectTypeTF, projectNameTF, projectWeightTF, projectDeadlineTF, add, cancel);

    }

    /**
     * Constructs a project object based on user input
     */
    private void constructProject() {
        String courseName = coursePickTF.getValue();
        String projectName = projectNameTF.getText();
        String projectType = projectTypeTF.getValue();
        String projectWeightS = projectWeightTF.getText();
        String projectDeadlineS = String.valueOf(projectDeadlineTF.getValue());
        double projectWeight;
        int[] projectDeadline = new int[3];

        if (courseName.isEmpty() || projectType.isEmpty() || projectWeightS.isEmpty() || projectDeadlineS.isEmpty()) {
            errorStatus("Invalid input. Make sure no fields are empty.");
            return;
        } else if (data.checkProjectExistInCourse(courseName, projectName)) {
            errorStatus("Project already exists in course.");
            return;
        }

        try {
            projectWeight = Double.parseDouble(projectWeightS);

            String[] parts = projectDeadlineS.split("-"); // deadline is turned into integer array
            int date;
            int i = 0;
            for (String part : parts) {
                date = Integer.parseInt(part);
                projectDeadline[i] = date;
                i++;
            }

        } catch (NumberFormatException e) {
            errorStatus("Could not parse integer project weight.");
            return;
        }

        if (projectWeight < 0 || projectWeight > 100) {
            errorStatus("Invalid target grade.");
            return;
        }

        if (projectType.equals("EXAM")) {
            try {
                data.storeNewExam(courseName, projectName, projectWeight, projectDeadline, "location", "topics");
                successStatus("Exam stored.");
            } catch (Exception e) {
                errorStatus("Could not store exam");
            }
        } else {
            try {
                data.storeNewAssignment(courseName, projectName, projectWeight, projectDeadline, "specialInstructions");
                successStatus("Assignment stored.");
            } catch (Exception e) {
                errorStatus("Could not store assignment");
            }
        }

    }





    // Status
    /**
     * Shows red error message
     * @param message Error message to print.
     */
    private void errorStatus(String message){
        status.setTextFill(Color.RED);
        status.setText("Error! " + message);

        // clear the error message after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            status.setText(""); // Clear the error message
        }));
        timeline.play();

    }

    /**
     * Shows green success message
     * @param message Success message to print.
     */
    private void successStatus(String message){
        status.setTextFill(Color.RED);
        status.setText("Success! " + message);

        // clear the error message after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            status.setText(""); // Clear the error message
        }));
        timeline.play();

    }

    /**
     * Shows black message
     * @param message Message to print.
     */
    private void neutralStatus(String message){
        status.setTextFill(Color.BLACK);
        status.setText(message);

        // clear the error message after 3 seconds
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(3), event -> {
            status.setText(""); // Clear the error message
        }));
        timeline.play();
    }



    // Menu bar

    /**
     * Loads the current data from .csv file of user's choice
     * @param event user selects "File -> Load"
     */
    @FXML
    void load(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose File");
        File selectedFile = fileChooser.showOpenDialog(new Stage()); // Show the file chooser dialog
        if (selectedFile != null) {
            // handle selected file
            try {
                data = FileLoader.load(selectedFile);
                successStatus("Loaded file " + selectedFile.getName());

            } catch (Exception e) { // Invalid selection
                errorStatus("Couldn't save data to " + selectedFile.getName());
            }
        } else {
            // User canceled the file selection
            neutralStatus("File selection cancelled");
        }
    }

    /**
     * Saves the current data in .csv file. (saves to 'data.csv' by default)
     * @param event user selects "File -> Save"
     */
    @FXML
    void save(ActionEvent event) {
        File file = new File(defaultDirectory);
        boolean success = FileSaver.save(file, data);
        if (success) {
            status.setTextFill(Color.GREEN);
            status.setText("Battle saved to " + defaultDirectory + "!");
        } else {
            status.setTextFill(Color.RED);
            status.setText("Error! Couldn't save data to " + defaultDirectory);
        }
    }

    /**
     * Saves the current data in .csv file of users choice, changes default file to user's choice of file
     */
    @FXML
    void saveAs() {
        try {
            FileChooser fileChooser = new FileChooser();
            File selectedFile = fileChooser.showSaveDialog(new Stage());
            defaultDirectory = selectedFile.getName();

            boolean success = FileSaver.save(selectedFile, data);
            if (success) {
                status.setTextFill(Color.GREEN);
                status.setText("Battle saved to " + defaultDirectory + "!");
            } else {
                status.setTextFill(Color.RED);
                status.setText("Error! Couldn't save data to " + defaultDirectory);
            }
        } catch (Exception e) {
            status.setTextFill(Color.RED);
            status.setText("Error! Couldn't save data to " + defaultDirectory);
        }
    }

    /**
     * Provides user with information about GUI's function
     */
    @FXML
    void about() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("Course Tracker v" + HelloApplication.version);
        alert.setContentText(String.format("Author: %s%nEmail: %s%nVersion: %s%n%s", HelloApplication.author, HelloApplication.email, HelloApplication.version, HelloApplication.about));
        // Add OK button
        alert.getButtonTypes().setAll(ButtonType.OK);
        // Show the dialog
        alert.showAndWait();
    }

    /**
     * Quits the application after confirming with the user.
     */
    @FXML
    void quit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quit Application");
        alert.setContentText("Are you sure you want to quit?");
        // Add OK and Cancel buttons
        alert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        // Show the dialog and handle user's choice
        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                // If user chooses OK, exit the application
                Platform.exit();
            }
        });
    }


}
