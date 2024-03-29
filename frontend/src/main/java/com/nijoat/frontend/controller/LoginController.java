package com.nijoat.frontend.controller;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.IOException;

public class LoginController {
    public void showLoginWindow() throws IOException {

        Stage loginStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/login-view.fxml"));

        double screenWidth = Screen.getPrimary().getVisualBounds().getWidth() * 0.75;
        double screenHeight = Screen.getPrimary().getVisualBounds().getHeight() * 0.75;
        loginStage.setMinHeight(screenHeight);
        loginStage.setMinWidth(screenWidth);
        loginStage.setMaxHeight(screenHeight);
        loginStage.setMaxWidth(screenWidth);

        Scene scene = new Scene(fxmlLoader.load(), screenWidth, screenHeight);
        loginStage.setScene(scene);
        loginStage.setTitle("DrawAndGuess Online");
        loginStage.show();
    }

    private static final String BASE_URL = "http://localhost:8080";

    @FXML
    private TextField nicknameField;

    @FXML
    private TextField passwordField;

    // Method to handle login button click
    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

try {
    URL url = new URL(BASE_URL + "/login");
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("POST");
    connection.setRequestProperty("Content-Type", "application/json"); // Set content type to JSON
    connection.setDoOutput(true);

    // Backkend ei synkkaa jos ei lähetä json muodossa, joten pitää otttaa gson käyttöön. Ei jostain syystä onnistunu mun koneella, mutta
    // koodi on kunnossa ja depency lisätty, eli pitää vaan ajaa komento npm clean install ja sen jälkeen buildata uudestaan.
    Gson gson = new Gson();
    String jsonBody = gson.toJson(new User(nickname, password));

    // Send the JSON request body
    OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
    writer.write(jsonBody);
    writer.flush();

    // Handle response...
} catch (IOException e) {
    e.printStackTrace();
}

    // Method to handle register button click
    @FXML
    protected void onRegisterButtonClick(ActionEvent event) {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        try {
            URL url = new URL(BASE_URL + "/register");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Send request body
            String requestBody = "nickname=" + nickname + "&password=" + password;
            connection.getOutputStream().write(requestBody.getBytes());

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();

            System.out.println("Registration response: " + response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
    

    