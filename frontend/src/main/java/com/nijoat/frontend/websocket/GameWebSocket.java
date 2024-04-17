package com.nijoat.frontend.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.io.IOException;
import java.util.function.Consumer;

public class GameWebSocket implements WebSocketListener {

    private Session session;
    private Consumer<String> messageHandler;

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        System.out.println("Game WebSocket connection opened!");
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String message) {
        if (messageHandler != null) {
            messageHandler.accept(message);
        }
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        System.out.println("WebSocket connection closed!");
    }

    @Override
    public void onWebSocketError(Throwable throwable) {
        System.out.println("WebSocket Error Occurred!");
        throwable.printStackTrace();
    }

    public void setMessageHandler(Consumer<String> handler) {
        this.messageHandler = handler;
    }

    public void sendMessage(String message) {
        try {
            if (message == null) {
                System.err.println("Message is null. Cannot send.");
                return;
            }
            assert session != null;
            if (session.isOpen()) {
                session.getRemote().sendString(message);
            }
        } catch (IOException e ) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (session != null) {
            session.close();
        }
    }
}