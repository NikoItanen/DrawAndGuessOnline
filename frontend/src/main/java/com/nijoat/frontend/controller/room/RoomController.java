package com.nijoat.frontend.controller.room;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class RoomController {
    @FXML
    private Label roomNameLabel;

    private String roomName;

    public void setRoomName(String roomName) {
        roomNameLabel.setText(roomName);
    }
}
