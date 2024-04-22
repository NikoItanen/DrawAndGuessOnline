package com.nijoat.frontend.controller.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nijoat.frontend.util.UserSession;

import com.nijoat.frontend.websocket.ChatWebSocket;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import org.eclipse.jetty.websocket.client.WebSocketClient;

public class MessageController {

    @FXML
    private TextField inputField;

    @FXML
    private TextFlow messageFlow;

    @FXML
    private Label gamewordText;

    private ChatWebSocket socket;
    private WebSocketClient client;


    public void initialize() {
        client = new WebSocketClient();
        try {
            client.start();
            URI echoURI = new URI("ws://localhost:8080/websocket/chat");
            socket = new ChatWebSocket();
            client.connect(socket, echoURI);
            socket.setMessageHandler(this::processMessage);
            
            // Mahdollistaa viestin lähettämisen enterillä!
            inputField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER)) {
                    try {
                        onButtonSend(new ActionEvent());
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void processMessage(String message) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonObject = objectMapper.readTree(message);

            String sender = jsonObject.get("sender").asText();
            String content = jsonObject.get("content").asText();
            boolean isCorrect = jsonObject.get("isCorrect").asBoolean();
            System.out.println(isCorrect);

            Platform.runLater(() -> {
                Text senderText = new Text(sender + ": ");
                senderText.setFill(Color.RED);
                Text contentText = new Text(content);
                if (isCorrect) {
                    contentText.setFill(Color.GREEN);
                }

                messageFlow.getChildren().addAll(senderText, contentText, new Text("\n"));
            });
        } catch (Exception e) {
            System.err.println("Error parsing JSON: " + e.getMessage());
        }
    }


    @FXML
    protected void onButtonSend(ActionEvent event) throws JsonProcessingException {
        if (socket != null) {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                ObjectNode jsonMessage = objectMapper.createObjectNode();
                jsonMessage.put("sender", UserSession.getUsername());
                jsonMessage.put("content", message);
                socket.sendMessage(jsonMessage.toString());
                inputField.clear();
            }
        } else {
            System.out.println("WebSocket is null");
        }
    }

    public void closeChatWebSocket() {
        try {
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}