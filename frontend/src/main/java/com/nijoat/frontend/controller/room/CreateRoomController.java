package com.nijoat.frontend.controller.room;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class CreateRoomController {
    @FXML
    private ListView<String> connectedUsersList;

    @FXML
    private TextField roomNameInput;

    private Consumer<String> onRoomCreatedCallback;

    public void setOnRoomCreated(Consumer<String> callback) {
        this.onRoomCreatedCallback = callback;
    }

    @FXML
    protected void handleCreateRoom(ActionEvent event) throws IOException {
        String roomName = roomNameInput.getText();
        if (!roomName.isEmpty()) {
            System.out.println("Creating new room: " + roomName);
            if (onRoomCreatedCallback != null) {
                onRoomCreatedCallback.accept(roomName);
            }

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();
        } else {
            showAlert("Invalid Room Name", "Please enter a valid room name");
        }
    }

    private void closeDialog(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}