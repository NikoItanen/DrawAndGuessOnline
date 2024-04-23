package com.nijoat.frontend.controller.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.websocket.client.WebSocketClient;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import com.nijoat.frontend.websocket.GameWebSocket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nijoat.frontend.util.UserSession;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

import java.net.URI;

public class GameController {

    private WebSocketClient client;
    private GameWebSocket socket;

    @FXML
    private ListView<String> leaderboardListView;

    @FXML
    private ListView<String> leaderboardListView1;

    @FXML
    private Text gamewordText;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String username;

    public void initialize() {
        client = new WebSocketClient();
        try {
            client.start();
            URI uri = new URI("ws://localhost:8080/websocket/game");
            socket = new GameWebSocket();
            socket.setMessageHandler(this::processMessage);
            username = UserSession.getUsername();
            connectToWebSocket(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void connectToWebSocket(URI uri) throws Exception {
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        request.setHeader("Username", username);
        client.connect(socket, uri, request);
    }

    public void processMessage(String message) {
        System.out.println(message);
        Platform.runLater(() -> {
            try {
                if (message.startsWith("{")) {
                    // Jos tulee viestinä leaderboard
                    JsonNode leaderboardJson = objectMapper.readTree(message);
                    ObservableList<String> leaderboardEntries = FXCollections.observableArrayList();
    
                    leaderboardJson.fields().forEachRemaining(entry -> {
                        String username = entry.getKey();
                        int points = entry.getValue().asInt();
                        leaderboardEntries.add(username + ": " + points + " points");
                    });
    
                    leaderboardListView.setItems(leaderboardEntries);
                } else if (message.startsWith("[") && message.endsWith("]")) {
                    // Jos tulee viestinä tämänhetkisen pelin pisteet
                    JsonNode playerPointsArray = objectMapper.readTree(message);
                    ObservableList<String> playerPointsEntries = FXCollections.observableArrayList();
    
                    for (JsonNode node : playerPointsArray) {
                        String username = node.get("username").asText();
                        int points = node.get("points").asInt();
                        playerPointsEntries.add(username + ": " + points + " points");
                    }
    
                    leaderboardListView1.setItems(playerPointsEntries);
                } else {
                    // Jos tulee viestinä randword
                    gamewordText.setText(message);
                }
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