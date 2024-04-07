package com.nijoat.backend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nijoat.backend.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class WebSocketHandler extends TextWebSocketHandler {
    private final List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private final List<Message> messages = new ArrayList<>();
    private final ObjectMapper objectMapper;

    @Autowired
    public WebSocketHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        Message welcomeChat = new Message();
        welcomeChat.setSender("Server");
        welcomeChat.setContent("Welcome to chat!");
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(welcomeChat)));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        String payload = textMessage.getPayload();
        Message receivedMessage = objectMapper.readValue(payload, Message.class);
        messages.add(receivedMessage);

        broadcastMessage(textMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        Message disconnectChat = new Message();
        disconnectChat.setSender("Server");
        disconnectChat.setContent("User left from the game.");
        session.sendMessage(new TextMessage(objectMapper.writeValueAsString(disconnectChat)));
    }

    public void broadcastMessage(TextMessage message) throws Exception {
        for (WebSocketSession userSession : sessions) {
            if (userSession.isOpen()) {
                try {
                    userSession.sendMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
