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

    public static final String version = "1.0";
    public static final String author = "Utaha Iwai, Dipti Kumar";
    public static final String email = "utaha.iwai@ucalgary.ca, dipti.kumar@ucalgary.ca";
    public static final String about = "This is an Academic Tracker.";

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}