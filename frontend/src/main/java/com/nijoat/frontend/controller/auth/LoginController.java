package com.nijoat.frontend.controller.auth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nijoat.frontend.controller.menu.MenuController;
import com.nijoat.frontend.model.User;
import com.nijoat.frontend.util.UserSession;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;
import java.util.logging.Logger;

public class LoginController {
    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label statusLabel;

    private static final double WINDOW_WIDTH = 1280;
    private static final double WINDOW_HEIGHT = 720;

    private static final String BASE_URL = "http://localhost:8080";

    public void showLoginWindow() throws IOException {

        Stage loginStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/login-view.fxml"));

        loginStage.setMinHeight(WINDOW_HEIGHT);
        loginStage.setMinWidth(WINDOW_WIDTH);
        loginStage.setMaxHeight(WINDOW_HEIGHT);
        loginStage.setMaxWidth(WINDOW_WIDTH);

        Scene scene = new Scene(fxmlLoader.load(), WINDOW_WIDTH, WINDOW_HEIGHT);
        loginStage.setScene(scene);
        loginStage.setTitle("DrawAndGuess Online");
        loginStage.show();
    }

    private OutputStreamWriter createOutputStreamWriter(HttpURLConnection connection) throws IOException {
        connection.setDoOutput(true);
        return new OutputStreamWriter(connection.getOutputStream());
    }


    // Method to handle login button click
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {

            URL url = new URL(BASE_URL + "/login");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(new User(username, password));

            try (OutputStream outputStream = connection.getOutputStream();
                 OutputStreamWriter writer = new OutputStreamWriter(outputStream)){

                writer.write(jsonBody);
                writer.flush();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    System.out.println("Login Successful.");
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/menu-view.fxml"));
                        Parent root = fxmlLoader.load();
                        MenuController menuController = fxmlLoader.getController();
                        UserSession.setUsername(username);

                        menuController.initializeWebSocket();

                        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

                        currentStage.getScene().setRoot(root);
                        System.out.println(username + " logged in");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
                    statusLabel.setText("Invalid username or password.");
                    statusLabel.setTextFill(Color.RED);
                } else {
                    System.out.println("Failed to login. HTTP Error Code: " + responseCode);
                }
            }

        } catch (IOException e) {
            logger.severe("An error occurred during login: " + e.getMessage());
            logger.severe("Stack trace: ");
            for (StackTraceElement element : e.getStackTrace()) {
                logger.severe(element.toString());
            }
        }
    }

    // Method to handle register button click
    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try {
            URL url = new URL(BASE_URL + "/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/json");

            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode requestBodyJson = objectMapper.createObjectNode();
            requestBodyJson.put("username", username);
            requestBodyJson.put("password", password);

            try (OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream())) {
                writer.write(requestBodyJson.toString());
                writer.flush();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    statusLabel.setText("User registered successfully!");
                    statusLabel.setTextFill(Color.GREEN);
                } else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
                    statusLabel.setText("Username already exists!");
                    statusLabel.setTextFill(Color.RED);
                } else {
                    System.out.println("Failed to register. HTTP Error Code: " + responseCode);
                }
            }

        } catch (IOException e) {
            logger.severe("An error occurred during registration: " + e.getMessage());
            logger.severe("Stack trace: ");
            for (StackTraceElement element : e.getStackTrace()) {
                logger.severe(element.toString());
            }
        }
    }

    @FXML
    protected void devTestButtonClick (ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/menu-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    