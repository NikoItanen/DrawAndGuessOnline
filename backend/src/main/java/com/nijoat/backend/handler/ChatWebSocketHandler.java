package com.nijoat.backend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nijoat.backend.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> roomSession = new HashMap<>();
    private ObjectMapper objectMapper;

    private final List<Message> messages = new ArrayList<>();
    private boolean isCorrect;

    @Autowired
    private GameWebSocketHandler gameWebSocketHandler;

    @Autowired
    public void setRoomWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String roomId = extractRoomIdFromSession(session);
        roomSession.put(roomId, session);
        System.out.println("New Websocket connection established for room: " + roomId);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String roomId = extractRoomIdFromSession(session);
        String payload = message.getPayload();
        System.out.println("Received message in room: " + roomId + ": " + payload);
        Message receivedMessage = objectMapper.readValue(payload, Message.class);
        
        String randWord = gameWebSocketHandler.getRandomWord();
        
        if (receivedMessage.getContent().equals(randWord)) {
            System.out.println("Correcto!");
            isCorrect = true;
            gameWebSocketHandler.generateRandomWord();
        } else {
            isCorrect = false;
        }
        messages.add(receivedMessage);

        broadcastChatMessage(roomId, message, isCorrect);
        isCorrect = false;
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomIdFromSession(session);
        roomSession.remove(roomId);
        System.out.println("WebSocket connection closed for chat!");
    }

    public String extractRoomIdFromSession(WebSocketSession session) {
        return "room1";
    }

    private void broadcastChatMessage(String roomId, TextMessage message, boolean isCorrect) {
        for (WebSocketSession session : roomSession.values()) {
            if (session.isOpen()) {
                try {
                    JsonObject jsonObject = new JsonParser().parse(message.getPayload()).getAsJsonObject();

                    System.out.println(isCorrect);
                    jsonObject.addProperty("isCorrect", isCorrect);

                    String modifiedMessage = jsonObject.toString();
                    System.out.println(modifiedMessage);

                    session.sendMessage(new TextMessage(modifiedMessage));
                } catch (IOException e) {
                    System.err.println("Failed to send chat message to room member: " + e.getMessage());
                }
            }
        }
    }
}