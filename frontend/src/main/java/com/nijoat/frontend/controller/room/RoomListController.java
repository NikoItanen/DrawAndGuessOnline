package com.nijoat.frontend.controller.room;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nijoat.frontend.model.Room;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private List<String> fetchAvailableRoomsFromServer() {
        List<String> availableRooms = new ArrayList<>();

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/rooms"))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<Room> rooms = objectMapper.readValue(response.body(), new TypeReference<List<Room>>() {});
                availableRooms = rooms.stream().map(Room::getRoomName).collect(Collectors.toList());
            } else {
                System.out.println("Failed to fetch rooms. Status code: " + response.statusCode());
            }
        } catch (InterruptedException | IOException e) {
            System.out.println("Error occurred while fetching rooms: " + e.getMessage());
        }

        return availableRooms;
    }

    @FXML
    private void onJoinButtonClick() {
        String selectedUser = roomList.getSelectionModel().getSelectedItem();
        if(selectedUser != null) {
            System.out.println("Joining lobby with user: " + selectedUser);
        }
    }
}
