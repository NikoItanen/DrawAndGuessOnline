package com.nijoat.frontend.controller.drawing;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.CheckBox;
import javafx.scene.paint.Color;

public class DrawingController {

    @FXML
    private Canvas canvas;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private TextField brushSize;

    @FXML
    private CheckBox eraser;

    @FXML
    private Slider brushSizeSlider;

    private GraphicsContext gc;  // Store GraphicsContext as a field to reuse in multiple methods

    public void initialize() {
        gc = canvas.getGraphicsContext2D();
        clearCanvas();  // Clear the canvas at initialization

        double initialBrushSize = Double.parseDouble(brushSize.getText());
        brushSizeSlider.setValue(initialBrushSize);

        brushSizeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            brushSize.setText(String.format("%.0f", newValue.doubleValue()));
        });

        canvas.setOnMouseClicked(e -> drawOrErase(e.getX(), e.getY(), brushSizeSlider.getValue()));
        canvas.setOnMouseDragged(e -> drawOrErase(e.getX(), e.getY(), brushSizeSlider.getValue()));
    }

    private void drawOrErase(double x, double y, double size) {
        if (eraser.isSelected()) {
            gc.setFill(Color.WHITE);  // Set to erase (white)
        } else {
            gc.setFill(colorPicker.getValue());  // Set to draw with selected color
        }
        gc.fillRect(x - size / 2, y - size / 2, size, size);
    }

    @FXML
    private void clearCanvas() {
        gc.setFill(Color.WHITE);  // Set color to white for clearing
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());  // Fill the entire canvas
    }

    public void onExit() {
        Platform.exit();
    }
}
