package com.nijoat.frontend.controller.room;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class RoomListController {
    @FXML
    private ListView<String> userList;

    @FXML
    Button joinButton;

    @FXML
    public void initialize() {
        userList.getItems().addAll("User1", "User2", "User3");
    }

    @FXML
    private void onJoinButtonClick() {
        String selectedUser = userList.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            System.out.println("Joining lobby with user: " + selectedUser);

        }
    }
}
