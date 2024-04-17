package com.nijoat.frontend.controller.game;

import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import com.nijoat.frontend.websocket.GameWebSocket;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

import java.net.URI;

public class GameController {

    private WebSocketClient client;
    private GameWebSocket socket;

    @FXML
    private Text gamewordText;

    public void initialize() {
        client = new WebSocketClient();
        try {
            client.start();
            URI uri = new URI("ws://localhost:8080/websocket/game");
            socket = new GameWebSocket();
            socket.setMessageHandler(this::processMessage);
            client.connect(socket, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processMessage(String message) {
        System.out.println(message);
        Platform.runLater(() -> {
            try {
                gamewordText.setText(message);
            } catch (Exception e) {
                System.err.println("Error processing message: " + e.getMessage());
            }
        });
    }
    
    public void closeWebSocket() {
        try {
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}