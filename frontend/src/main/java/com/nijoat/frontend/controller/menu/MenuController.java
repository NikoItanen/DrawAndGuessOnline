package com.nijoat.frontend.controller.menu;

import com.nijoat.frontend.controller.room.CreateRoomController;
import com.nijoat.frontend.util.UserSession;
import com.nijoat.frontend.websocket.ProgramWebSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import org.eclipse.jetty.websocket.client.WebSocketClient;

public class MenuController {

    private WebSocketClient client;
    private ProgramWebSocket socket;


    public void initializeWebSocket() {
        client = new WebSocketClient();
        try {
            client.start();
            URI echoUri = new URI("ws://localhost:8080/websocket/mainmenu");
            socket = new ProgramWebSocket();
            client.connect(socket, echoUri);
        } catch (Throwable t) {
            t.printStackTrace();
        }
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
    protected void joinLobby (ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/roomlist-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage lobbyStage = new Stage();
            lobbyStage.setScene(new Scene(root));
            lobbyStage.setTitle("Room List");
            lobbyStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void createRoom(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Room");
        dialog.setHeaderText("Enter Room Name:");
        dialog.setContentText("Room Name:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isEmpty()) {
            String roomName = result.get();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/create-room.fxml"));
                Parent root = fxmlLoader.load();

                CreateRoomController createRoomController = fxmlLoader.getController();
                createRoomController.createRoom(roomName, event);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            showAlert("Invalid Room Name", "Please enter a valid room name");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error");
        alert.setHeaderText(title);
        alert.setContentText(message);
        alert.showAndWait();
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