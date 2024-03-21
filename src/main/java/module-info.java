module com.example.drawandguessonline {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;

    opens com.example.drawandguessonline to javafx.fxml;
    exports com.example.drawandguessonline;
}