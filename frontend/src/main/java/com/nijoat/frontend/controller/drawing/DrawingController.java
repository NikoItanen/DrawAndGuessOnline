package com.nijoat.frontend.controller.drawing;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;

import java.io.IOException;

public class DrawingController {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSize;

    @FXML
    private CheckBox eraser;

    public DrawingController() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/drawing-view.fxml"));
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
