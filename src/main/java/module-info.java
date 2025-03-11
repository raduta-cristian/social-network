module org.example.social_network {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.social_network to javafx.fxml;
    exports org.example.social_network;
    exports org.example.social_network.controller;
    opens org.example.social_network.controller to javafx.fxml;
}