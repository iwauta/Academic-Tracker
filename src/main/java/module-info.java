module ca.ucalgary.groupprojectgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens ca.ucalgary.groupprojectgui to javafx.fxml;
    exports ca.ucalgary.groupprojectgui;
    exports ca.ucalgary.groupprojectgui.objects;
    exports ca.ucalgary.groupprojectgui.util;
}