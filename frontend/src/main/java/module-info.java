module com.nijoat.frontend {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires org.eclipse.jetty.websocket.client;
    requires org.eclipse.jetty.websocket.api;
    requires org.eclipse.jetty.util;
    requires org.eclipse.jetty.websocket.common;
    requires javafx.graphics;
    requires com.fasterxml.jackson.databind;
    requires java.logging;
    requires java.net.http;
    requires com.fasterxml.jackson.core;

    opens com.nijoat.frontend to javafx.fxml;
    opens com.nijoat.frontend.model to com.fasterxml.jackson.databind;

    exports com.nijoat.frontend;
    exports com.nijoat.frontend.controller.auth;
    opens com.nijoat.frontend.controller.auth to javafx.fxml;
    exports com.nijoat.frontend.controller.game;
    opens com.nijoat.frontend.controller.game to javafx.fxml;
    exports com.nijoat.frontend.controller.messaging;
    opens com.nijoat.frontend.controller.messaging to javafx.fxml;
    exports com.nijoat.frontend.controller.room;
    opens com.nijoat.frontend.controller.room to javafx.fxml;
    exports com.nijoat.frontend.controller.drawing;
    opens com.nijoat.frontend.controller.drawing to javafx.fxml;
    exports com.nijoat.frontend.controller.menu;
    opens com.nijoat.frontend.controller.menu to javafx.fxml;
}