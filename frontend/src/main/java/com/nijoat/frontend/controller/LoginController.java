package com.nijoat.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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

    @FXML
    private TextField nicknameField;

    @FXML
    private TextField passwordField;

    // Näihin ylläoleviin pitää tehdä vielä jotain muutoksia xml fileen, jotta toimii oikein. En osannut vielä tehdä niitä.

    private static final String BASE_URL = "http://localhost:8080"; // Change this to your backend URL

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String nickname = nicknameField.getText();
        String password = passwordField.getText();

        try {
            URL url = new URL(BASE_URL + "/login?nickname=" + nickname + "&password=" + password);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = in.readLine();
            in.close();

            if ("Login successful".equals(response)) {
                // Successful login, navigate to lobby or another screen
                System.out.println("Login successful");
            } else {
                // Failed login, display error message
                System.out.println("Login failed: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    // Alla oleva koodi on vielä kesken, mutta se on tarkoitus tehdä vastaavasti kuin ylläoleva koodi, mutta rekisteröintiä varten.
    }
    @FXML
    protected void onRegisterButtonClick() {
        System.out.println("Rekisteri nappi toimii!");
    }
}