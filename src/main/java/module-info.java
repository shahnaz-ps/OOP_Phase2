
module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires xstream;

    opens com.example.demo.views to javafx.fxml;
    exports com.example.demo.views;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;

}