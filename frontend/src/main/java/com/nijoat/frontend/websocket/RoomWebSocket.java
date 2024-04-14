package com.nijoat.frontend.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketListener;

import java.io.IOException;
import java.util.function.Consumer;

public class RoomWebSocket implements WebSocketListener {

    private Session session;
    private Consumer<String> messageHandler;

    @Override
    public void onWebSocketConnect(Session session) {
        this.session = session;
        System.out.println("Room WebSocket connection opened!");
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


    public void disconnect() {
        if (session != null) {
            session.close();
        }
    }

    @Override
    public void onWebSocketBinary(byte[] bytes, int i, int i1) {

    }

    @Override
    public void onWebSocketText(String s) {

    }
}
