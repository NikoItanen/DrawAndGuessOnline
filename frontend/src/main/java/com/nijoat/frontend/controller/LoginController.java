package com.nijoat.frontend.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Screen;
import javafx.scene.Scene;
import javafx.stage.Stage;

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

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/lobby-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.getScene().setRoot(root);

            currentStage.setTitle("DrawAndGuess Online - Lobby");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    protected void onRegisterButtonClick() {
        System.out.println("Rekisteri nappi toimii!");
    }
}