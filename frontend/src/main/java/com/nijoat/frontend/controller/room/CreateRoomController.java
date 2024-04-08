package com.nijoat.frontend.controller.room;

import com.nijoat.frontend.websocket.ProgramWebSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

public class CreateRoomController {
    @FXML
    private ListView<String> connectedUsersList;

    @FXML
    protected void handleCreateRoom(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Room");
        dialog.setHeaderText("Enter Room Name:");
        dialog.setContentText("Room Name:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            String roomName = result.get();
            System.out.println("Creating new room: " + roomName);
            openNewRoom(roomName, event);
        } else {
            showAlert("Invalid Room Name", "Please enter a valid room name");
        }
    }

    public void createRoom(String roomName, ActionEvent event) {
        openNewRoom(roomName, event);
    }

    protected void openNewRoom(String roomName, ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/room-view.fxml"));
            Parent root = fxmlLoader.load();

            RoomController roomController = fxmlLoader.getController();
            roomController.setRoomName(roomName);

            Stage roomStage = new Stage();
            roomStage.setTitle("New Room: " + roomName);
            roomStage.setScene(new Scene(root));

            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();

            roomStage.show();

        } catch (IOException e) {
            e.printStackTrace();
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