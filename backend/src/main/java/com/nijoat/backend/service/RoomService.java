package com.nijoat.backend.service;

import com.nijoat.backend.model.Room;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class RoomService {
    private List<Room> rooms = new ArrayList<>();

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void removeRoom (Room room) {
        rooms.remove(room);
    }

    public List<String> getAllRooms(){
        List<String> roomList = new ArrayList<>();

        for (Room room : rooms) {
            roomList.add(room.getRoomName());
        }
        return roomList;
    }

}
