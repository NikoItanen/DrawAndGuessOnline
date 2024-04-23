package com.nijoat.backend.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

// Tämä komponentti on määritelty kehittämään WebSocket-palvelimen toiminnallisuutta piirtoalustan hallintaan.
@Component
public class DrawingWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    private List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);

    }

    // Lähettää viestin yksittäiselle WebSocket-istunnolle.
    private void sendMessageToSession(WebSocketSession session, String message) throws IOException {
        session.sendMessage(new TextMessage(message));
    }

    private void sendMessageToAllSessions(String message) {
        for (WebSocketSession session : sessions) {
            try {
                sendMessageToSession(session, message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Vastaanottaa clear-viestin ja lähettää sen kaikille istunnoille.
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String receivedMessage = message.getPayload();
        if (receivedMessage.equals("clear")) {
            sendMessageToAllSessions(receivedMessage);
        }
        sendMessageToAllSessions(receivedMessage);
    }

    public void sendClearCanvas() {
        sendMessageToAllSessions("clear");
    }
}
