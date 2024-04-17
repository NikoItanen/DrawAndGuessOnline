package com.nijoat.backend.handler;

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
import java.util.List;
import java.util.Random;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private final List<String> gamewords = new ArrayList<>();
    private String randWord;

    private List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        generateRandomWord();
    }

    public void generateRandomWord() {
        readWordsFromFile("backend/src/main/java/com/nijoat/backend/handler/words.txt");
        Random rand = new Random();
        int index = rand.nextInt(gamewords.size());
        randWord = gamewords.get(index);
        System.out.println("Random word generated: " + randWord);
        sendMessageToAllSessions(randWord);
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

    private void sendMessageToSession(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }


    // Tää koska ChatWebSocketHandleristä kutsuminen sekoittaa ton ylläolevan session -systeemin
    private void sendMessageToAllSessions(String message) {
        for (WebSocketSession session : sessions) {
            try {
                sendMessageToSession(session, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getRandomWord() {
        return randWord;
    }
}