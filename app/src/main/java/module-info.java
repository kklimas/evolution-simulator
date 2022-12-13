module com.app {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.app to javafx.fxml;
    exports com.app;
    exports com.app.controllers;
    opens com.app.controllers to javafx.fxml;
}