package ca.ucalgary.cpsc233;

import ca.ucalgary.cpsc233.objects.Assignment;
import ca.ucalgary.cpsc233.objects.Course;
import ca.ucalgary.cpsc233.objects.Exam;
import ca.ucalgary.cpsc233.objects.Project;
import ca.ucalgary.cpsc233.util.FileLoader;
import ca.ucalgary.cpsc233.util.FileSaver;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import java.time.LocalDate;
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

/**
 * HelloController - controller for GUI
 *
 */
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
    @FXML
    private TableView<ProjectModel> projectTableView;

    @FXML
    private CheckBox pendingOnlyCheckBox;

    // List of models of Project objects
    private ArrayList<ProjectModel> projectData;

    @FXML
    private TableColumn<ProjectModel, String> projectNameColumn;

    @FXML
    private TableColumn<ProjectModel, String> projectWeightColumn;

    @FXML
    private TableColumn<ProjectModel, String> projectDeadlineColumn;

    @FXML
    private TableColumn<ProjectModel, String> projectTypeColumn;

    @FXML
    private TableColumn<ProjectModel, String> projectSpecialColumn;

    @FXML
    private TableColumn<ProjectModel, String> projectStatusColumn;


    // Grades
    @FXML
    private TableView<GradeModel> gradeTableView;

    @FXML
    private CheckBox gradeInProgressOnlyCheckBox;

    // List of models of Course objects
    private ArrayList<GradeModel> gradeData;

    @FXML
    private TableColumn<GradeModel, String> gradeCourseNameColumn;

    @FXML
    private TableColumn<GradeModel, String> targetGradeColumn;

    @FXML
    private TableColumn<GradeModel, String> actualGradeColumn;

    /**
     * initialize
     */
    @FXML
    public void initialize() {
        data = new Data();
        // Associate columns with model properties (Courses)
        courseNameColumn.setCellValueFactory(new PropertyValueFactory<>("courseName"));
        profNameColumn.setCellValueFactory(new PropertyValueFactory<>("profName"));
        profEmailColumn.setCellValueFactory(new PropertyValueFactory<>("profEmail"));
        inProgressColumn.setCellValueFactory(new PropertyValueFactory<>("inProgress"));

        // Associate columns with model properties (Projects)
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        projectWeightColumn.setCellValueFactory(new PropertyValueFactory<>("projectWeight"));
        projectDeadlineColumn.setCellValueFactory(new PropertyValueFactory<>("projectDeadline"));
        projectTypeColumn.setCellValueFactory(new PropertyValueFactory<>("projectType"));
        projectSpecialColumn.setCellValueFactory(new PropertyValueFactory<>("projectSpecial"));
        projectStatusColumn.setCellValueFactory(new PropertyValueFactory<>("projectStatus"));

        // Associate columns with model properties (Grades)
        gradeCourseNameColumn.setCellValueFactory(new PropertyValueFactory<>("course"));
        targetGradeColumn.setCellValueFactory(new PropertyValueFactory<>("targetGrade"));
        actualGradeColumn.setCellValueFactory(new PropertyValueFactory<>("actualGrade"));

        // Populate TableView with data&initialize the table
        courseData = generateCourseTableContents(false);
        courseTableView.getItems().addAll(courseData);
        projectData = generateProjectTableContents(false);
        projectTableView.getItems().addAll(projectData);
        gradeData = generateGradeTableContents(false);
        gradeTableView.getItems().addAll(gradeData);

    }


    /* Courses */

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
        courseName = courseName.toUpperCase();
        profEmail = profEmail.toLowerCase();
        boolean emailValid = false;
        try {
            String profEmailType = "@" + profEmail.split("@")[1];
            if(HelloApplication.EMAIL_TYPES.contains(profEmailType)){ // one of the supported email type.
                emailValid = true;
            } // emailValid stays false otherwise
        } catch (Exception e){
            emailValid = false; // doesn't end with @emailType
        }

        // Course name too short or too long
        if (courseName.length() < MIN_LENGTH || courseName.length() > MAX_LENGTH) {
            errorStatus("Invalid course name.");
            return false;
        } else if (!emailValid) {
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
//            profName = formatName(profName);
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
//
//    private String formatName(String unformattedName){
//        String formattedName; // formatted name to return
//        try{ // separate into array of names (first, middle last, etc.)
//            String[] names = unformattedName.split(" ");
//            StringBuilder sb = new StringBuilder(Arrays.toString(names));
//
//            for (int i = 0; i < sb.length(); i++){
//                String name = "";
//                for(int j=0; j< names[i].length(); j++){
//                    if(j == 0){ // capitalize the first letter
//                        name += names[i].charAt(i);
//                        name.toUpperCase();
//                    } else{
//
//                    }
//                }
//                formattedName += names[i].charAt(0).toUpperCase();
//            }
//        }
//
//        return formattedName;
//    }

    @FXML
    void closeCourse(ActionEvent actionEvent){
        Stage stage = new Stage();
        VBox newRoot = new VBox();

        Label label = new Label("Course to close:");
        ChoiceBox<String> courseChoiceBox = new ChoiceBox();
        // select course
        ArrayList<Course> courses = data.getInProgressCourses();
        String courseName;
        for (Course course : courses) {
            if (course.isInProgress()) {
                courseName = course.getCourseName().toUpperCase();
                courseChoiceBox.getItems().add(courseName);
            }
        }
        Button closeCourseButton = new Button("Close");
        closeCourseButton.setOnAction(event -> {
            courseClosure(courseChoiceBox.getValue());
            stage.close();
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> stage.close());
        newRoot.getChildren().addAll(label,courseChoiceBox,closeCourseButton,cancel);
        Scene newScene = new Scene(newRoot, 400, 400);
        stage.setScene(newScene);
        stage.setTitle("Close a course:");
        stage.show();
    }

    /**
     * Close a course
     * @param courseName name of the course to close
     */
    private void courseClosure(String courseName){
        if(courseName.isEmpty()){
            errorStatus("Please select a course.");
            return;
        }
        for(Course course: data.getAllCourses()){
            if(course.getCourseName().equals(courseName)){
                course.closeCourse();
                successStatus(courseName+" closed.");
                continue;
            }
        }
        updateCourseList();
    }

    /**
     * Controls list of Course models for the
     * @param inProgressOnly want to display in-progress courses only(true) or not
     * @return list of CourseControllers
     */
    private ArrayList<CourseModel> generateCourseTableContents(boolean inProgressOnly){
        ArrayList<Course> courses = new ArrayList<>();
        // ArrayList of CourseModels to return
        ArrayList<CourseModel> courseModels = new ArrayList<>();
        if(inProgressOnly) {
            for (Course course : data.sortCourses()) {
                if (course.isInProgress()) { // course is in-progress
                    courses.add(course); // add from the list
                }
            }
        } else {
            for (Course course : data.sortCourses()) {
                courses.add(course); // add from the list

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
        if (inProgressOnlyCheckBox.isSelected()) {
            courseData = generateCourseTableContents(true);
        } else{ // show all the courses
            courseData = generateCourseTableContents(false);
        }
        updateGradeList();
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



    /* Projects */

    /**
     * Tales user to new stage to add course details
     * @param actionEvent user selects "Add Course"
     */
    @FXML
    void addProject(ActionEvent actionEvent) {
        // new scene opens to construct a course
        Stage stage = new Stage();
        VBox newRoot = new VBox();

        ChoiceBox<String> courseChoiceBox = new ChoiceBox();
        ChoiceBox<String> projectTypeChoiceBox = new ChoiceBox();
        DatePicker projectDeadlineDatePicker = new DatePicker();
        TextField examLocationTF = new TextField();

        // select course
        ArrayList<Course> courses = data.getInProgressCourses();
        String courseName;
        for (Course course : courses) {
            if (course.isInProgress()) {
                courseName = course.getCourseName().toUpperCase();
                courseChoiceBox.getItems().add(courseName);
            }
        }
        // select project type
        projectTypeChoiceBox.getItems().addAll("EXAM", "ASSIGNMENT");

        // get user inputs
        TextField projectNameTextField = new TextField();
        projectNameTextField.setPromptText("Enter the project name:");

        TextField projectWeightTextField = new TextField();
        projectWeightTextField.setPromptText("Enter the project weight:");

        TextField projectLocationTextFiled = new TextField();
        projectLocationTextFiled.setPromptText("Enter the location of the exam:");

        TextField projectReviewTopicsTextFiled = new TextField();
        projectReviewTopicsTextFiled.setPromptText("Enter the topics of the exam:");

        TextField projectSpecialInstructionsTextFiled = new TextField();
        projectSpecialInstructionsTextFiled.setPromptText("Enter the special instructions of the assignment:");

        Button add = new Button("Add Project");
        add.setOnAction(event -> {
            boolean success = constructProject(courseChoiceBox.getValue(), projectNameTextField.getText(), projectTypeChoiceBox.getValue(),projectWeightTextField.getText(), projectDeadlineDatePicker.getValue(),projectLocationTextFiled.getText(),projectReviewTopicsTextFiled.getText(),projectSpecialInstructionsTextFiled.getText());
            if (success) {
                // Successfully added a project, update the list
                updateProjectTable();
                System.out.println("project list updated");
                stage.close();
            }
        });
        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> stage.close()); // cancel exists course constructor
        newRoot.getChildren().addAll(courseChoiceBox, projectTypeChoiceBox, projectNameTextField, projectWeightTextField, projectDeadlineDatePicker,projectLocationTextFiled,projectReviewTopicsTextFiled,projectSpecialInstructionsTextFiled, add, cancel);

        Scene newScene = new Scene(newRoot, 400, 400);
        stage.setScene(newScene);
        stage.setTitle("Add a Project:");
        stage.show();
    }

    /**
     * Constructs a project object based on user input
     */
    private boolean constructProject(String courseName, String projectName, String projectType, String projectWeight, LocalDate projectDeadline, String location, String reviewTopics, String specialInstructions) {
        double weight;
        int[] deadline;

        if (courseName.isEmpty() || projectType.isEmpty() || projectWeight.isEmpty()) {
            errorStatus("Invalid input. Make sure no fields are empty.");
            return false;
        } else if (data.checkProjectExistInCourse(courseName, projectName)) {
            errorStatus("Project already exists in course.");
            return false;
        }

        try {
            weight = Double.parseDouble(projectWeight);
            // Convert LocalDate to an array of integers [day,month,year]
            deadline = new int[]{projectDeadline.getDayOfMonth(), projectDeadline.getMonthValue(), projectDeadline.getYear()};
        } catch (NumberFormatException e) {
            errorStatus("Could not parse integer project weight.");
            return false;
        }

        if (weight < 0 || weight > 100) {
            errorStatus("Invalid project weight.");
            return false;
        }

        // Adding EXAM
        if (projectType.equals("EXAM")) {
            try {
                if (location.isEmpty()){
                    location = null;
                }
                if(reviewTopics.isEmpty()){
                    reviewTopics = null;
                }

                boolean success = data.storeNewExam(courseName, projectName, weight, deadline,location, reviewTopics);
                if(success){
                    successStatus("Exam stored.");
                }
                else{
                    errorStatus("Something went wrong.");
                }
                return success;
            } catch (Exception e) {
                errorStatus("Could not store exam.");
                return false;
            }

        } // Adding ASSIGNMENT
        else {
            try {
                if(specialInstructions.isEmpty()){
                    specialInstructions = null;
                }
                boolean success = data.storeNewAssignment(courseName, projectName, weight, deadline, specialInstructions);
                if(success){
                    successStatus("Assignment stored.");
                }
                return success;
            } catch (Exception e) {
                errorStatus("Could not store assignment");
                return false;
            }
        }
    }

    /**
     * Mark project complete, update the table
     * @param actionEvent park project complete button clicked
     */
    @FXML
    void closeProject(ActionEvent actionEvent){
        Stage stage = new Stage();
        VBox newRoot = new VBox();

        Label label = new Label("Project to mark complete:");
        ChoiceBox<String> courseChoiceBox = new ChoiceBox();
        // select course
        ArrayList<Course> courses = data.getInProgressCourses();
        String courseName;
        for (Course course : courses) {
            if (course.isInProgress()) {
                courseName = course.getCourseName().toUpperCase();
                courseChoiceBox.getItems().add(courseName);
            }
        }

        ChoiceBox<String> projectChoiceBox = new ChoiceBox();
        // select course
        ArrayList<Project> projects = data.getAllProjects();
        String projectName;
        for (Project project : projects) {
            projectName = project.getProjectName();
            projectChoiceBox.getItems().add(projectName);
        }

        Button closeCourseButton = new Button("Mark Complete");
        closeCourseButton.setOnAction(event -> {
            projectCompleter(courseChoiceBox.getValue(),projectChoiceBox.getValue());
            stage.close();
        });

        Button cancel = new Button("Cancel");
        cancel.setOnAction(event -> stage.close());
        newRoot.getChildren().addAll(label,courseChoiceBox,projectChoiceBox,closeCourseButton,cancel);
        Scene newScene = new Scene(newRoot, 400, 400);
        stage.setScene(newScene);
        stage.setTitle("Mark Project Complete:");
        stage.show();
    }

    /**
     * complete project, update table
     * @param courseName name of the course
     * @param projectName name of the project
     */
    private void projectCompleter(String courseName, String projectName){
        if(!data.checkProjectExistInCourse(courseName,projectName)){
            errorStatus("Check information entered.");
            return;
        }
        for(Project project: data.getAllProjects()){
            // found project (check courseName and projectName)
            if(project.getCourseName().equals(courseName)&&project.getProjectName().equals(projectName)){
                project.setProjectComplete(); // mark complete
                updateCourseList(); // update the list
                successStatus(projectName + " for " + courseName+" marked complete.");
                return;
            }
        }
    }

    /**
     * Updates the ProjectModel list (used in updateProjectTable)
     */
    private ArrayList<ProjectModel> generateProjectTableContents(boolean pendingOnly){
        System.out.println("generating table project");
        ArrayList<Project> projects = new ArrayList<>(); // Arraylist of Projects to show info about
        ArrayList<ProjectModel> projectModels = new ArrayList<>(); // ArrayList of ProjectModels to return
        if (pendingOnly) {
            System.out.println("pending only True");
            for (Project project : data.sortProjects()) {
                if (!project.isProjectComplete()) {
                    projects.add(project);
                    System.out.println("added project to projects");
                }
            }
        } else {
            System.out.println("pending only false");
            for (Project project : data.sortProjects()) {
                projects.add(project);
                System.out.println("added project to projects");
            }
        }

        // Add to the list to return
        for (Project project: projects) {
            String courseName = project.getCourseName();
            String projectName = project.getProjectName();
            // Properties for ProjectModel
            // projectName for the table ('project (course)')
            String projectNameWithCourseName = String.format("%s (%s)",projectName,courseName);
            String weight = String.valueOf(project.getProjectWeight());
            String deadline = project.deadlineToString(); // 'dd/mm/yyyy'
            String type = "";
            String special= "";
            String status;
            if(project.isProjectComplete()){
                status = "COMPLETE";
            } else{
                status = "PENDING";
            }

            // formatting special instructions / location / exam topics based on project type
            if (project instanceof Exam){
                type = "E";
                special = String.format("Location: %s, Topics: %s", ((Exam) project).getLocation(), ((Exam) project).getReviewTopics());
            } else if (project instanceof Assignment){
                type = "A";
                special = String.format("Instruction: %s", ((Assignment) project).getSpecialInstructions());
            }

            ProjectModel projectModel = new ProjectModel(projectNameWithCourseName, weight, deadline, type, special, status);
            projectModels.add(projectModel);
            System.out.println("projectmodel created");
        }
        return projectModels;
    }

    /**
     * Updates the table of projects using generateProjectTableContents
     */
    private void updateProjectTable(){
        // Clear the existing items in the TableView
        projectTableView.getItems().clear();
        projectData.clear();
        // Show In-Progress projects only
        if (pendingOnlyCheckBox.isSelected()){
            projectData = generateProjectTableContents(true);
        } else { // show all the projects
            projectData = generateProjectTableContents(false);
        }
        // Add all the items to the TableView
        projectTableView.getItems().addAll(projectData);
        // Refresh the TableView
        projectTableView.refresh();
    }

    /**
     * Updates the table when user checks on/off the checkbox
     * @param event checking on/off the checkbox
     */
    @FXML
    void checkPendingOnly(ActionEvent event){
        updateProjectTable();
    }


    // Grades

    /**
     * Generates models for grade
     * @param inProgressOnly true if in-progress courses only or false otherwise
     * @return models of grades for the table
     */
    private ArrayList<GradeModel> generateGradeTableContents(boolean inProgressOnly){
        ArrayList<Course> courses = new ArrayList<>();
        // ArrayList of CourseModels to return
        ArrayList<GradeModel> gradeModels = new ArrayList<>();
        if(inProgressOnly) {
            for (Course course : data.sortCourses()) {
                if (course.isInProgress()) { // course is in-progress
                    courses.add(course); // add from the list
                }
            }
        } else {
            // add from the list
            courses.addAll(data.sortCourses());
        }
        // Add to the list to return
        for (Course course : courses) {
            String target = course.getTargetGrade() + "%";
            String actual = course.getActualGrade() + "%";
            GradeModel gradeModel = new GradeModel(course.getCourseName(), target, actual);
            gradeModels.add(gradeModel);
        }
        return gradeModels;
    }

    /**
     * Updates the table
     */
    private void updateGradeList(){
        // Clear the existing items in the TableView
        gradeTableView.getItems().clear();
        gradeData.clear();
        // Show In-Progress courses only
        if(gradeInProgressOnlyCheckBox.isSelected()){
            gradeData = generateGradeTableContents(true);
        } else{ // show all the courses
            gradeData = generateGradeTableContents(false);
        }
        // Add all the items to the TableView
        gradeTableView.getItems().addAll(gradeData);
        // Refresh the TableView
        gradeTableView.refresh();
    }

    /**
     * Updates table if user pressed refresh button
     * @param actionEvent refresh button pressed
     */
    @FXML
    void refreshGradeTable(ActionEvent actionEvent){
        updateGradeList();
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

        // Call updateProjectTable() to ensure the table view is initially populated
        updateCourseList();
        updateProjectTable();
        updateCourseList();

    }

    /**
     * Shows green success message
     * @param message Success message to print.
     */
    private void successStatus(String message){
        status.setTextFill(Color.GREEN);
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
                updateCourseList();
                updateProjectTable();
                updateGradeList();
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
            status.setText("Data saved to " + defaultDirectory + "!");
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
                status.setText("Data saved to " + defaultDirectory + "!");
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
        alert.setHeaderText(HelloApplication.PROGRAM_TITLE + " v" + HelloApplication.VERSION);
        alert.setContentText(String.format("Author: %s%nEmail: %s%nVersion: %s%n%s", HelloApplication.AUTHOR, HelloApplication.EMAIL, HelloApplication.VERSION, HelloApplication.ABOUT));
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

