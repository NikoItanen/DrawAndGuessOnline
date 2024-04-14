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

    public void initialize() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,canvas.getHeight(), canvas.getWidth());

        double initialBrushSize = Double.parseDouble(brushSize.getText());
        brushSizeSlider.setValue(initialBrushSize);

        brushSizeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            brushSize.setText(String.format("%.0f", newValue.doubleValue()));
            double size = newValue.doubleValue();
        });

        canvas.setOnMouseClicked(e -> {
            double size = brushSizeSlider.getValue();
            gc.setFill(colorPicker.getValue());

            if (eraser.isSelected()) {
                gc.setFill(Color.WHITE);
                gc.fillRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            } else {
                gc.setFill(colorPicker.getValue());
                gc.fillRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            }
        });


        canvas.setOnMouseDragged(e -> {
            double size = brushSizeSlider.getValue();
            gc.setFill(colorPicker.getValue());

            if (eraser.isSelected()) {
                gc.setFill(Color.WHITE);
                gc.fillRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            } else {
                gc.setFill(colorPicker.getValue());
                gc.fillRect(e.getX() - size / 2, e.getY() - size / 2, size, size);
            }
        });
    }

        public void onExit () {
            Platform.exit();
        }
    }
