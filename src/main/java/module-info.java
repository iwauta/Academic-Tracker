module ca.ucalgary.groupprojectgui {
    requires javafx.controls;
    requires javafx.fxml;


    opens ca.ucalgary.groupprojectgui to javafx.fxml;
    exports ca.ucalgary.groupprojectgui;
}