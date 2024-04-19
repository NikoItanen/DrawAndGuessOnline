package com.nijoat.backend.config;

import com.nijoat.backend.handler.ChatWebSocketHandler;
import com.nijoat.backend.handler.MainWebSocketHandler;
import com.nijoat.backend.handler.RoomWebSocketHandler;
import com.nijoat.backend.handler.GameWebSocketHandler;
import com.nijoat.backend.handler.DrawingWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;



@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MainWebSocketHandler menuWebSocketHandler;
    private final RoomWebSocketHandler roomWebSocketHandler;
    private final ChatWebSocketHandler chatWebSocketHandler;
    private final GameWebSocketHandler gameWebSocketHandler;
    private final DrawingWebSocketHandler drawingWebSocketHandler;

    @Autowired
    public WebSocketConfig(MainWebSocketHandler MenuWebSocketHandler, RoomWebSocketHandler roomWebSocketHandler, ChatWebSocketHandler chatWebSocketHandler, GameWebSocketHandler gameWebSocketHandler, DrawingWebSocketHandler drawingWebSocketHandler) {
        this.menuWebSocketHandler = MenuWebSocketHandler;
        this.roomWebSocketHandler = roomWebSocketHandler;
        this.chatWebSocketHandler = chatWebSocketHandler;
        this.gameWebSocketHandler = gameWebSocketHandler;
        this.drawingWebSocketHandler = drawingWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(menuWebSocketHandler, "/websocket/menu").setAllowedOrigins("*");
        registry.addHandler(roomWebSocketHandler, "/websocket/room").setAllowedOrigins("*");
        registry.addHandler(chatWebSocketHandler, "/websocket/chat").setAllowedOrigins("*");
        registry.addHandler(gameWebSocketHandler, "/websocket/game").setAllowedOrigins("*");
        registry.addHandler(drawingWebSocketHandler, "/websocket/drawing").setAllowedOrigins("*");
    }

}