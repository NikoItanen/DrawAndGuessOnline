package com.nijoat.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextFlow;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Platform;
import javafx.event.ActionEvent;

public class MessageController {

    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;
    @FXML
    private TextFlow messageFlow;

    private String username;

    public void openSecondWindow(String username) {
        this.username = username;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/message-view.fxml"));
            fxmlLoader.setController(this);
            Stage secondStage = new Stage();
            VBox root = fxmlLoader.load();
            messageFlow = (TextFlow) fxmlLoader.getNamespace().get("messageFlow");
            updateMessages();
    
            // Fetch messages from backend periodically
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(this::updateMessages, 0, 1, TimeUnit.SECONDS);
    
            secondStage.setScene(new Scene(root));
            secondStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMessages() {
        Platform.runLater(() -> {
            String messages = fetchMessages();
    
            messageFlow.getChildren().clear();
            Text messageText = new Text(messages);
            messageFlow.getChildren().add(messageText);
        });
    }

    private String fetchMessages() {
        try {
            URL url = new URL("http://localhost:8080/messages");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to fetch messages";
        }
    }

    @FXML
    private void onButtonSend(ActionEvent event) {
        sendMessage();
    }
    
    // Source: https://www.baeldung.com/httpurlconnection-post
    private void sendMessage() {
        try {
            String message = messageField.getText();

            String jsonPayload = "{\"sender\":\"" + username + "\",\"content\":\"" + message + "\"}";

            URL url = new URL("http://localhost:8080/sendmessage");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonPayload.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("Message sent successfully");
            } else {
                System.out.println("Failed to send message.");
            }

            con.disconnect();
            messageField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}