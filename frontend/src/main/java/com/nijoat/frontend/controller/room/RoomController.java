package com.nijoat.frontend.controller.room;

import com.nijoat.frontend.controller.messaging.MessageController;
import com.nijoat.frontend.websocket.MenuWebSocket;
import com.nijoat.frontend.websocket.RoomWebSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.eclipse.jetty.websocket.client.WebSocketClient;

import java.io.IOException;
import java.net.URI;

public class RoomController {
    @FXML
    private Label roomNameLabel;
    @FXML
    private Button exitButton;

    private WebSocketClient client;
    private RoomWebSocket socket;

    public void initializeRoomWebSocket() {
        client = new WebSocketClient();
        try {
            client.start();
            URI echoURI = new URI("ws://localhost:8080/websocket/room");
            socket = new RoomWebSocket();
            client.connect(socket, echoURI);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void setRoomName(String roomName) {
        roomNameLabel.setText(roomName);
    }

    @FXML
    protected void onExitButtonClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/nijoat/frontend/menu-view.fxml"));
            Parent root = fxmlLoader.load();
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.getScene().setRoot(root);
            closeRoomWebSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeRoomWebSocket() {
        try {
            client.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
