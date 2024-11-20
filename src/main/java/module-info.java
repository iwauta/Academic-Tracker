module ca.ucalgary.groupprojectgui {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.junit.jupiter.api;


    opens ca.ucalgary.iwauta to javafx.fxml;
    exports ca.ucalgary.iwauta;
    exports ca.ucalgary.iwauta.objects;
    exports ca.ucalgary.iwauta.util;
}