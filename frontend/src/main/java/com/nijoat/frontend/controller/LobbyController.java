package com.nijoat.frontend.controller;

import com.almasb.fxgl.entity.action.Action;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class LobbyController {

    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    @FXML
    protected void onLogoutButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/login-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void testRoomButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/game-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.getScene().setRoot(root);
        
            FXMLLoader messageLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/message-view.fxml"));
            VBox messageRoot = messageLoader.load();
        
            MessageController messageController = messageLoader.getController();
            messageController.openSecondWindow(username);
        
            VBox gameRoot = (VBox) root;
            gameRoot.getChildren().add(messageRoot);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}