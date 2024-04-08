package com.nijoat.frontend.controller.drawing;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

public class DrawingController {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSize;

    @FXML
    private CheckBox eraser;

    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();

        canvas.setOnMouseDragged(e -> {
            double size = Double.parseDouble(brushSize.getText());
            gc.setFill(colorPicker.getValue());

            if (eraser.isSelected()) {
                gc.clearRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            } else {
                gc.setFill(colorPicker.getValue());
                gc.fillRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            }
        });
    }

    public void onExit() {
        Platform.exit();
    }
}
