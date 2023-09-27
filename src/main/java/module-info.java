module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.opencsv;


    opens com.myapp to javafx.fxml;
    exports com.myapp;
}