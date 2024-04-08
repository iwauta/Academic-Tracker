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
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;

public class HelloController {

    Data data = new Data();

    private String defaultDirectory = "data.csv";

    @FXML
    private Label status;

    // course details ////////

    private TextField courseNameTF = new TextField();
    private TextField profNameTF = new TextField();

    private TextField profEmailTF = new TextField();
    private TextField targetGradeTF = new TextField();

    // project details ///////

    private ChoiceBox<String> coursePickTF = new ChoiceBox();

    private ChoiceBox<String> projectTypeTF = new ChoiceBox();

    private TextField projectNameTF = new TextField();
    private TextField projectWeightTF = new TextField();

    private DatePicker projectDeadlineTF = new DatePicker();
    private TextField examLocationTF = new TextField();




    @FXML
    public void initialize() {
    }


    /**
     * Constructs a course entered by the user
     * Tales user to new stage to add course details
     * @param actionEvent user selects "Add Course"
     */
    @FXML
    void addCourse(ActionEvent actionEvent) {
        // new scene opens to construct a course
        Stage stage = new Stage();
        VBox newRoot = new VBox();

        courseNameTF.setPromptText("Enter the course name:");

        profNameTF.setPromptText("Enter the professor's name:");

        profEmailTF.setPromptText("Enter the professor's email address (ending in @ucalgary.ca):");

        targetGradeTF.setPromptText("Enter you target grade for this course:");

        Scene newScene = new Scene(newRoot, 400, 400);

        stage.setScene(newScene);
        stage.setTitle("Add a course:");
        stage.show();

        Button add = new Button("Add Course");

        add.setOnAction(event -> constructCourse()); // course is constructing using user input

        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> stage.close()); // cancel exists course constructor

        newRoot.getChildren().addAll(courseNameTF, profNameTF, profEmailTF, targetGradeTF, add, cancel);

    }

    /**
     * Constructs a course object based on user input
     */
    private void constructCourse() {
        String courseName = courseNameTF.getText();
        String profName = profNameTF.getText();
        String profEmail = profEmailTF.getText();
        String target = targetGradeTF.getText();
        double targetGrade;

        if (courseName.isEmpty() || profName.isEmpty() || target.isEmpty()) {
            errorStatus("Invalid input. Make sure no fields are empty.");
            return;
        } else if (!profEmail.endsWith("@ucalgary.ca")) {
            errorStatus("Invalid email address.");
            return;
        } else if (data.checkExistCourse(courseName)) {
            errorStatus("Invalid email address.");
            return;
        }

        try {
            targetGrade = Double.parseDouble(target);
        } catch (NumberFormatException e) {
            errorStatus("Could not parse integer target grade.");
            return;
        }

        if (targetGrade < 0 || targetGrade > 100) {
            errorStatus("Invalid target grade.");
            return;
        }

        try {
            data.storeNewCourse(courseName, profName, profEmail, targetGrade);
            successStatus("Course stored.");
        } catch (Exception e) {
            errorStatus("Could not store course");
            return;
        }


    }

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
     * @param event user selects "File -> Save As"
     */
    @FXML
    void saveAs(ActionEvent event) {
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
     * @param event user selects "Help -> About"
     */
    @FXML
    void about(ActionEvent event) {
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
     * Quits GUI
     * @param event user selects "File -> Quit"
     */
    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
    }


}
