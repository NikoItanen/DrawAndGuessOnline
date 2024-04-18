package com.nijoat.frontend.controller.menu;

import com.nijoat.frontend.controller.messaging.MessageController;
import com.nijoat.frontend.controller.room.CreateRoomController;
import com.nijoat.frontend.controller.room.RoomController;
import com.nijoat.frontend.model.Player;
import com.nijoat.frontend.model.User;
import com.nijoat.frontend.util.UserSession;
import com.nijoat.frontend.websocket.MenuWebSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;

import org.eclipse.jetty.websocket.client.WebSocketClient;

public class MenuController {

    private WebSocketClient client;
    private MenuWebSocket socket;


    public void initializeWebSocket() {
        client = new WebSocketClient();
        try {
            client.start();
            URI echoUri = new URI("ws://localhost:8080/websocket/menu");
            socket = new MenuWebSocket();
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
        try {
            FXMLLoader createRoomLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/create-room.fxml"));
            Parent createRoomRoot = createRoomLoader.load();
            CreateRoomController createRoomController = createRoomLoader.getController();

            createRoomController.setOnRoomCreated(roomName -> {
                try {
                    FXMLLoader roomLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/room-view.fxml"));
                    Parent roomRoot = roomLoader.load();
                    RoomController roomController = roomLoader.getController();
                    FXMLLoader messageLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/message-view.fxml"));
                    MessageController messageController = messageLoader.getController();
                    roomController.initializeRoomWebSocket(roomName);

                    Player hostUser = new Player(UserSession.getUsername());
                    roomController.addPlayerToRoom(hostUser);
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.getScene().setRoot(roomRoot);
                    roomController.showUserList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            Stage createRoomStage = new Stage();
            createRoomStage.setScene(new Scene(createRoomRoot));
            createRoomStage.setTitle("Create New Room");
            createRoomStage.show();

        } catch (IOException e) {
            e.printStackTrace();
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