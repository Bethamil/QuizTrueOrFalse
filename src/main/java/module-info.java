module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires org.jsoup;
    requires java.sql;

    opens org.example to javafx.fxml;
    exports org.example;
    exports org.example.model;
    opens org.example.model to javafx.fxml;
    exports org.example.controller;
    opens org.example.controller to javafx.fxml;
}
