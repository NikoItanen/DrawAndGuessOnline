package com.nijoat.backend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Component
public class RoomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> roomSession = new HashMap<>();
    private final ObjectMapper objectMapper;

    

    @Autowired
    public RoomWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String roomId = extractRoomIdFromSession(session);
        roomSession.put(roomId, session);
        System.out.println("New Websocket connection established for room: " + roomId);
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomIdFromSession(session);
        roomSession.remove(roomId);
        System.out.println("WebSocket connection closed for room!");
    }

    public String extractRoomIdFromSession(WebSocketSession session) {
        return "room1";
    }
}
