module com.nijoat.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.google.gson;

    opens com.nijoat.frontend to javafx.fxml;
    exports com.nijoat.frontend;
    exports com.nijoat.frontend.controller;
    opens com.nijoat.frontend.controller to javafx.fxml;
}