module example.demodic {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires google.cloud.translate;
    requires google.cloud.core;
    requires freetts;


    opens example.demodic to javafx.fxml;
    exports example.demodic;
}