package com.nijoat.frontend.controller.room;

import com.nijoat.frontend.controller.messaging.MessageController;
import com.nijoat.frontend.model.Player;
import com.nijoat.frontend.model.Room;
import com.nijoat.frontend.model.User;
import com.nijoat.frontend.util.UserSession;
import com.nijoat.frontend.websocket.MenuWebSocket;
import com.nijoat.frontend.websocket.RoomWebSocket;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
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

    private Room room;
    @FXML
    private ListView<String> userList;


    public void initializeRoomWebSocket(String roomName) {
        this.room = new Room(roomName);
        roomNameLabel.setText(room.getRoomName());
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

    @FXML
    public void showUserList() {
        if (userList != null) {
            userList.getItems().addAll(room.getConnectedPlayersNames());
        } else {
            System.err.println("userList is null");
        }
    }

    public void addPlayerToRoom(Player player) {
        room.addPlayer(player);
    }
}
