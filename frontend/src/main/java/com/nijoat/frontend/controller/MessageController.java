package com.nijoat.frontend.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

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

// Tässä voitaisiin myös käyttää websockettia, mutta helpompi ja yhteinäisempi tapa HTTP-pyynnöillä
// Toimii melko reaaliajassa


public class MessageController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField messageField;
    @FXML
    private Button sendButton;

    public void openSecondWindow() {
        try {
            Stage secondStage = new Stage();
            Label messageLabel = new Label();
            HBox root = new HBox(10);
            
            // Tämä osio hakee viestit bäkkäristä
            String messages = fetchMessages();
            messageLabel.setText(messages);
            root.getChildren().add(messageLabel);

            // Input UI
            usernameField = new TextField();
            usernameField.setPromptText("Enter username");
            messageField = new TextField();
            messageField.setPromptText("Enter message");
            root.getChildren().addAll(usernameField, messageField);

            // Send UI
            sendButton = new Button("Send");
            sendButton.setOnAction(event -> sendMessage());
            root.getChildren().add(sendButton);
            
            // Hakee viestit bäkkäristä x sekunnin välein
            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(() -> {
                Platform.runLater(() -> updateMessages(messageLabel));
            }, 0, 1, TimeUnit.SECONDS);
            
            secondStage.setScene(new Scene(root));
            secondStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMessages(Label messageLabel) {
        String messages = fetchMessages();
        Platform.runLater(() -> messageLabel.setText(messages));
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


    // Source: https://www.baeldung.com/httpurlconnection-post
    private void sendMessage() {
        try {
            String username = usernameField.getText();
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
            usernameField.clear();
            messageField.clear();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}