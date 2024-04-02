package com.nijoat.frontend.controller;

import com.almasb.fxgl.entity.action.Action;
import com.nijoat.frontend.model.User;
import com.nijoat.frontend.util.UserSession;
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

        System.out.println(UserSession.getUsername());

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/game-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            currentStage.getScene().setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}