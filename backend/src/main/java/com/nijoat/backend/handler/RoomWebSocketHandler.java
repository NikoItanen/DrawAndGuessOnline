package com.nijoat.backend.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nijoat.backend.model.Message;
import com.nijoat.backend.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Component
public class RoomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> roomSession = new HashMap<>();
    private final ObjectMapper objectMapper;

    private Room room;


    @Autowired
    public RoomWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String roomId = GenerateRandomRoomId(session);
        roomSession.put(roomId, session);
        room = new Room(roomId);
        System.out.println("New Websocket connection established for room: " + roomId);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = GenerateRandomRoomId(session);
        roomSession.remove(roomId);
        System.out.println("WebSocket connection closed for room!");
    }

    public String GenerateRandomRoomId(WebSocketSession session) {
        Random random = new Random();
        StringBuilder roomIdBuilder = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            roomIdBuilder.append(random.nextInt(10));
        }
        return roomIdBuilder.toString();
    }


    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        room.setRoomName(payload);
        System.out.println("New Roomname set: " + payload);
    }

    public void joinRoom(String roomId, WebSocketSession session) throws IOException {
        roomSession.put(roomId, session);

        TextMessage message = new TextMessage("You have joined room: " + roomId);
        session.sendMessage(message);

        System.out.println("User joined room: " + roomId);
    }
}


