package com.nijoat.frontend.model;

import java.util.List;

public class Player {
    private String username;
    private List<String> joinedRooms;

    public Player(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void joinRoom(String roomName) {
        if (!joinedRooms.contains(roomName)) {
            joinedRooms.add(roomName);
        }
    }

    public void leaveRoom(String roomName) {
        joinedRooms.remove(roomName);
    }
}
