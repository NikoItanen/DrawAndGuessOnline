package com.nijoat.backend.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Room {
    @Getter
    @Setter
    private String roomId;
    @Getter
    @Setter
    private String roomName;
    @Getter

    private List<String> connectedUsers;

    public Room(String roomId) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.connectedUsers = new ArrayList<>();
    }

    public void addUser(String username) {
        connectedUsers.add(username);
    }

    public void removeUser(String username) {
        connectedUsers.remove(username);
    }

}
