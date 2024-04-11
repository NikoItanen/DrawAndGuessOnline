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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

@Component
public class RoomWebSocketHandler extends TextWebSocketHandler {
    private final Map<String, WebSocketSession> roomSession = new HashMap<>();
    private final ObjectMapper objectMapper;

    private final List<Message> messages = new ArrayList<>();
    private List<String> gamewords = new ArrayList<>();
    private String randWord;
    private boolean isCorrect;
    

    @Autowired
    public RoomWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        gamewordsInitialize();
    }

    public void readWordsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                gamewords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void gamewordsInitialize() {
        readWordsFromFile("backend/src/main/java/com/nijoat/backend/handler/words.txt");
        Random rand = new Random();
        int index = rand.nextInt(gamewords.size());
        randWord = gamewords.get(index);
        System.out.println(randWord);
    }

    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        System.out.println("Test");
        String roomId = extractRoomIdFromSession(session);
        roomSession.put(roomId, session);
        System.out.println("New Websocket connection established for room: " + roomId);
    }

    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String roomId = extractRoomIdFromSession(session);
        String payload = message.getPayload();
        System.out.println("Received message in room" + roomId + ": " + payload);
        Message receivedMessage = objectMapper.readValue(payload, Message.class);
        if (receivedMessage.getContent().equals(randWord)) {
            System.out.println("Correcto!");
            isCorrect = true;
        } else {
            isCorrect = false; }
        messages.add(receivedMessage);

        broadcastChatMessage(roomId, message, isCorrect);
        isCorrect = false;
    }

    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomIdFromSession(session);
        roomSession.remove(roomId);
        System.out.println("WebSocket connection closed for room!");
    }

    public String extractRoomIdFromSession(WebSocketSession session) {
        return "room1";
    }


    public void sendMessageToRoom(String roomId, String message) {
        WebSocketSession session = roomSession.get(roomId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage(message));
                System.out.println("Message sent to room " + roomId + ": " + message);
            } catch (IOException e) {
                System.err.println("Failed to send message to room" + roomId + ": " + e.getMessage());
            }
        } else {
            System.err.println("WebSocket session is closed or not available for room " + roomId);
        }
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
