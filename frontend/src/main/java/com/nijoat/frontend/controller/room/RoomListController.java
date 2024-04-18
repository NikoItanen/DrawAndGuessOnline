package com.nijoat.frontend.controller.room;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.util.List;

public class RoomListController {
    @FXML
    private ListView<String> roomList;

    @FXML
    Button joinButton;

    private String selectedRoom;

    @FXML
    public void initialize() {
       List<String> availableRooms = fetchAvailableRoomsFromServer();

       roomList.getItems().addAll(availableRooms);

       roomList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
           selectedRoom = newValue;

       });
    }

    @FXML
    private void onJoinButtonClick() {
        String selectedUser = roomList.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            System.out.println("Joining lobby with user: " + selectedUser);
        }
    }

    private List<String> fetchAvailableRoomsFromServer() {
        return List.of("Room 1", "Room 2", "Room 3");
    }
}
