package com.nijoat.frontend.controller.room;

import com.nijoat.frontend.controller.messaging.MessageController;
import com.nijoat.frontend.websocket.ProgramWebSocket;
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
    private ProgramWebSocket socket;
    private MessageController messageController;

    public void setRoomName(String roomName) {
        roomNameLabel.setText(roomName);
    }

    public ProgramWebSocket getRoomSocket() {
        return socket;
    }

    public void setMessageController(MessageController messageController) {
        this.messageController = messageController;
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

    public void initializeRoomWebSocket() {
        client = new WebSocketClient();
        try {
            client.start();
            URI echoURI = new URI("ws://localhost:8080/websocket/room");
            socket = new ProgramWebSocket();
            messageController.setWebSocket(socket);
            socket.setMessageHandler(message -> messageController.processMessage(message));
            client.connect(socket, echoURI);
        } catch (Throwable e) {
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
