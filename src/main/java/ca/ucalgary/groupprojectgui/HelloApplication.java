package ca.ucalgary.groupprojectgui;

/**
 * Main structure for GUI
 *
 * @author Utaha Iwai, Dipti Kumar
 * @tutorial T09
 * @email utaha.iwai@ucalgary.ca, dipti.kumar@ucalgary.ca
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    public static final String PROGRAM_TITLE = "Academic Tracker";
    public static final String VERSION = "1.0";
    public static final String AUTHOR = "Utaha Iwai, Dipti Kumar";
    public static final String EMAIL = "utaha.iwai@ucalgary.ca, dipti.kumar@ucalgary.ca";
    public static final String ABOUT = "\nThis is an Academic Tracker.\nYou can add courses and projects to be tracked. " +
            "View list of your courses/projects/grades in corresponding tabs; you can choose to 'show in-progress(incomplete) " +
            "courses(projects) only' by checking the box below. Program will keep track of information about each course " +
            "and project, and calculate current grades for each course.";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 640, 440);
        stage.setTitle("Academic Tracker v" + VERSION);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
