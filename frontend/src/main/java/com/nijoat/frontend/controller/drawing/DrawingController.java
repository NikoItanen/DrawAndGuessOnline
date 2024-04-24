package com.nijoat.frontend.controller.drawing;

import java.net.URI;

import com.nijoat.frontend.util.UserSession;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nijoat.frontend.websocket.GameWebSocket;

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

    private WebSocketClient client;
    private GameWebSocket socket;

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

    private GraphicsContext gc;

    public void initialize() {

        client = new WebSocketClient();
        try {
            client.start();
            URI uri = new URI("ws://localhost:8080/websocket/drawing");
            socket = new GameWebSocket();
            socket.setMessageHandler(this::processMessage);
            client.connect(socket, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gc = canvas.getGraphicsContext2D();
        clearCanvas();

        double initialBrushSize = Double.parseDouble(brushSize.getText());
        brushSizeSlider.setValue(initialBrushSize);

        brushSizeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
            brushSize.setText(String.format("%.0f", newValue.doubleValue()));
        });

        canvas.setOnMouseClicked(e -> drawOrErase(e.getX(), e.getY(), brushSizeSlider.getValue()));
        canvas.setOnMouseDragged(e -> drawOrErase(e.getX(), e.getY(), brushSizeSlider.getValue()));
    }

    public void processMessage(String message) {
        Platform.runLater(() -> {

            try {
                if (message.equals("clear")) {
                    clearCanvas();
                } else {
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(message);

                    double x = rootNode.get("x").asDouble();
                    double y = rootNode.get("y").asDouble();
                    double size = rootNode.get("size").asDouble();
                    Color color = Color.web(rootNode.get("color").asText());

                    draw(x, y, size, color);
                }
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
            }
        });
    }

    private void draw(double x, double y, double size, Color color) {
        gc.setFill(color);
        gc.fillRect(x - size / 2, y - size / 2, size, size);
    }
    
    public void closeWebSocket() {
        try {
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawOrErase(double x, double y, double size) {
        if (UserSession.getIsDrawer()) {
            if (eraser.isSelected()) {
                gc.setFill(Color.WHITE);
            } else {
                gc.setFill(colorPicker.getValue());
            }
            gc.fillRect(x - size / 2, y - size / 2, size, size);
            String message = drawingToJSON(x, y, size);
            sendMessageToBackend(message);
        }
    }

    private String drawingToJSON(double x, double y, double size) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("x", x);
            rootNode.put("y", y);
            rootNode.put("size", size);
            rootNode.put("color", colorPicker.getValue().toString());
            return objectMapper.writeValueAsString(rootNode);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private void sendMessageToBackend(String message) {
        try {
            socket.sendMessage(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCanvas() {
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    @FXML
    private void sendClearCanvas() {
        if (UserSession.getIsDrawer()) {
            socket.sendMessage("clear");
        }
    }

    public void onExit() {
        Platform.exit();
    }
}
