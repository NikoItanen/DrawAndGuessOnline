package com.nijoat.frontend.controller.messaging;

import com.google.gson.stream.JsonReader;
import com.nijoat.frontend.websocket.ProgramWebSocket;
import com.nijoat.frontend.util.UserSession;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

import java.io.*;
import java.net.URI;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.event.ActionEvent;

import org.eclipse.jetty.websocket.client.WebSocketClient;

public class MessageController {

    private WebSocketClient client;
    private ProgramWebSocket socket;

    @FXML
    private TextField inputField;

    @FXML
    private TextFlow messageFlow;


    public void initialize() {
        client = new WebSocketClient();
        try {
            client.start();
            URI echoUri = new URI("ws://localhost:8080/websocket/room");
            socket = new ProgramWebSocket();
            client.connect(socket, echoUri);
            socket.setMessageHandler(this::processMessage);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void processMessage(String message) {
        try {
            JsonReader reader = new JsonReader(new StringReader(message));
            System.out.println(message);
            reader.setLenient(true);
            JsonObject jsonObject = new JsonParser().parse(reader).getAsJsonObject();

            String sender = jsonObject.get("sender").getAsString();
            String content = jsonObject.get("content").getAsString();
            boolean isCorrect = jsonObject.get("isCorrect").getAsBoolean();
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
    private void onButtonSend(ActionEvent event) {
        String message = inputField.getText();
        if (!message.isEmpty()) {
            JsonObject jsonMessage = new JsonObject();
            jsonMessage.addProperty("sender", UserSession.getUsername());
            jsonMessage.addProperty("content", message);
            socket.sendMessage(jsonMessage.toString());
            inputField.clear();
        }
    }

    public void stop() {
        try {
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}