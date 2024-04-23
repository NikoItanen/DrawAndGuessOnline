package com.nijoat.backend.handler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nijoat.backend.model.User;
import com.nijoat.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    private final List<String> gamewords = new ArrayList<>();
    private String randWord;
    private Map<String, Integer> playerPoints = new HashMap<>();
    private List<WebSocketSession> sessions = new ArrayList<>();
    private int lastSelectedUserIndex = -1;
    private String usernameback;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        String username = (String) session.getHandshakeHeaders().getFirst("Username");
        usernameback = username;
        playerPoints.put(usernameback, 0);
        generateRandomWord();
        sendAllUsersToSessions();
        sendPlayerPointsToSessions();
    }

    // Leaderboard
    public void sendAllUsersToSessions() throws IOException {
        List<User> allUsers = userRepository.findAll();
        JsonObject allUsersJson = new JsonObject();
        
        for (User user : allUsers) {
            allUsersJson.addProperty(user.getUsername(), user.getPoints());
        }
        
        String message = allUsersJson.toString();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                sendMessageToSession(session, message);
            }
        }
    }

    public void giveLocalPoints(String username) {

        if (playerPoints.containsKey(username)) {
            int currentPoints = playerPoints.get(username);
            currentPoints++;
            playerPoints.put(username, currentPoints);
        } else {
            playerPoints.put(username, 1);
        }

        try {
            sendPlayerPointsToSessions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Logiikka käyttäjien valitsemiseen sekä satunnaisen sanan valitsemiseksi
    // TODO: Parempi tapa valita satunnainen sana!
    public void generateRandomWord() {
        readWordsFromFile("backend/src/main/java/com/nijoat/backend/handler/words.txt");
        Random rand = new Random();
        int index = rand.nextInt(gamewords.size());
        randWord = gamewords.get(index);
        System.out.println("Random word generated: " + randWord);
    
        lastSelectedUserIndex++;
        if (lastSelectedUserIndex >= sessions.size()) {
            lastSelectedUserIndex = 0;
        }
    
        for (int i = 0; i < sessions.size(); i++) {
            WebSocketSession session = sessions.get(i);
            if (i == lastSelectedUserIndex) {
                try {
                    sendMessageToSession(session, randWord);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                StringBuilder blanks = new StringBuilder();
                for (int j = 0; j < randWord.length(); j++) {
                    blanks.append("_");
                }
                try {
                    sendMessageToSession(session, blanks.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readWordsFromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                gamewords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Viestin lähettäminen
    private void sendMessageToSession(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    public String getRandomWord() {
        return randWord;
    }

    // Pisteet jotka menee databaseen
    public void givePoints(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            user.incrementPoints();
            userRepository.save(user);
        } else {
            System.out.println("User not found!");
        }
    }

    public void sendPlayerPointsToSessions() throws IOException {
        JsonArray playerPointsArray = new JsonArray();
        for (Map.Entry<String, Integer> entry : playerPoints.entrySet()) {
            JsonObject playerPointsEntry = new JsonObject();
            playerPointsEntry.addProperty("username", entry.getKey());
            playerPointsEntry.addProperty("points", entry.getValue());
            playerPointsArray.add(playerPointsEntry);
        }
        String message = playerPointsArray.toString();
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                sendMessageToSession(session, message);
            }
        }
    }
}