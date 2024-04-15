package com.nijoat.frontend.model;

import com.nijoat.frontend.util.UserSession;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomName;
    private List<Player> connectedPlayers;

    public Room(String roomName) {
        this.roomName = roomName;
        this.connectedPlayers = new ArrayList<>();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public List<Player> getConnectedPlayers() {
        return connectedPlayers;
    }

    public List<String> getConnectedPlayersNames() {
        List <String> connectedPlayersNames = new ArrayList<>();
        for (Player player : connectedPlayers) {
            connectedPlayersNames.add(player.getUsername());
        }
        return connectedPlayersNames;
    }

    public void addPlayer(Player player) {
        connectedPlayers.add(player);
    }
    public void removePlayer(Player player) {
        connectedPlayers.remove(player);
    }
}
