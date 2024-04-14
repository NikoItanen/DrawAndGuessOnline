package com.nijoat.frontend.controller.messaging;

import com.google.gson.stream.JsonReader;
import com.nijoat.frontend.controller.room.RoomController;
import com.nijoat.frontend.util.UserSession;

import com.nijoat.frontend.websocket.ProgramWebSocket;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

import java.io.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class MessageController {

    @FXML
    private TextField inputField;

    @FXML
    private TextFlow messageFlow;

    private ProgramWebSocket socket;

    private RoomController roomController;

    public void setWebSocket(ProgramWebSocket socket) {
        this.socket = socket;
    }

    public ProgramWebSocket getWebSocket() {
        return socket;
    }
    public void processMessage(String message) {
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
    protected void onButtonSend(ActionEvent event) {
        System.out.println("painike painettu");
        System.out.println("Painikkeen socket: " + getWebSocket());
        if (getWebSocket() != null) {
            String message = inputField.getText();
            if (!message.isEmpty()) {
                JsonObject jsonMessage = new JsonObject();
                jsonMessage.addProperty("sender", UserSession.getUsername());
                jsonMessage.addProperty("content", message);
                socket.sendMessage(jsonMessage.toString());
                inputField.clear();
            }
        } else {
            System.out.println("WebSocket is null");
        }
    }
}