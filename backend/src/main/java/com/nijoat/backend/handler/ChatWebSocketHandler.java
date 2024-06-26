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


// Tämä luokka vastaa chat-viestien käsittelystä WebSocket-yhteyden kautta.
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // Sisältää listan huoneista ja niiden WebSocket-istunnoista.
    private final Map<String, List<WebSocketSession>> roomSessions = new HashMap<>();
    private ObjectMapper objectMapper;

    // Lista vastaanotetuista viesteistä
    private final List<Message> messages = new ArrayList<>();
    private boolean isCorrect;

    @Autowired
    private DrawingWebSocketHandler drawingWebSocketHandler;

    @Autowired
    private GameWebSocketHandler gameWebSocketHandler;

    // Injektoidaan ObjectMapper riippuvuus
    @Autowired
    public void setRoomWebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    // Käsittelee uuden WebSocket-yhteyden avaamisen.
    public void afterConnectionEstablished(WebSocketSession session) throws IOException {
        String roomId = extractRoomIdFromSession(session);
        if (!roomSessions.containsKey(roomId)) {
            roomSessions.put(roomId, new ArrayList<>());
        }
        roomSessions.get(roomId).add(session);
        System.out.println("New Websocket connection established for room: " + roomId);
    }

    // Käsittelee tekstiviestejä, jotka vastaanotetaan asiakkaalta.
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String roomId = extractRoomIdFromSession(session);
        String payload = message.getPayload();
        System.out.println("Received message in room: " + roomId + ": " + payload);
        Message receivedMessage = objectMapper.readValue(payload, Message.class);
        
        String randWord = gameWebSocketHandler.getRandomWord();
        String username = receivedMessage.getSender();
        
        // Tarkistaa, vastaako viestin sisältö pelin satunnaissanaa.
        if (receivedMessage.getContent().equals(randWord)) {
            System.out.println("Correcto!");
            isCorrect = true;
            gameWebSocketHandler.givePoints(username); // Anna pisteitä oikeasta arvauksesta
            gameWebSocketHandler.giveLocalPoints(username); // Lokaalit pisteet
            gameWebSocketHandler.generateRandomWord(); // Uusi sana
            drawingWebSocketHandler.sendClearCanvas(); // Tyhjentää taulun arvauksen jälkeen
        } else {
            isCorrect = false;
        }
        messages.add(receivedMessage);

        // Lähettää vastaanotetun viestin takaisin kaikkiin huoneen istuntoihin.
        broadcastChatMessage(roomId, message, isCorrect);
        isCorrect = false;
    }

    // Käsittelee WebSocket-yhteyden sulkemisen.
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String roomId = extractRoomIdFromSession(session);
        if (roomSessions.containsKey(roomId)) {
            roomSessions.get(roomId).remove(session);
            System.out.println("WebSocket connection closed for chat!");
        }
    }

    // Palauttaa huoneen tunnisteen WebSocket-istunnosta.
    public String extractRoomIdFromSession(WebSocketSession session) {
        return "room1";
    }

    // Lähettää chat-viestin kaikille huoneen jäsenille.
    private void broadcastChatMessage(String roomId, TextMessage message, boolean isCorrect) {
        List<WebSocketSession> sessions = roomSessions.get(roomId);
        if (sessions != null) {
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    try {
                        JsonObject jsonObject = new JsonParser().parse(message.getPayload()).getAsJsonObject();
                        jsonObject.addProperty("isCorrect", isCorrect);

                        String modifiedMessage = jsonObject.toString();
                        session.sendMessage(new TextMessage(modifiedMessage));
                    } catch (IOException e) {
                        System.err.println("Failed to send chat message to room member: " + e.getMessage());
                    }
                }
            }
        }
    }
}