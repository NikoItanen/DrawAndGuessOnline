package com.nijoat.backend.config;

import com.nijoat.backend.handler.ChatWebSocketHandler;
import com.nijoat.backend.handler.MainWebSocketHandler;
import com.nijoat.backend.handler.RoomWebSocketHandler;
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

    @Autowired
    public WebSocketConfig(MainWebSocketHandler MenuWebSocketHandler, RoomWebSocketHandler roomWebSocketHandler, ChatWebSocketHandler chatWebSocketHandler) {
        this.menuWebSocketHandler = MenuWebSocketHandler;
        this.roomWebSocketHandler = roomWebSocketHandler;
        this.chatWebSocketHandler = chatWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(menuWebSocketHandler, "/websocket/menu").setAllowedOrigins("*");
        registry.addHandler(roomWebSocketHandler, "/websocket/room").setAllowedOrigins("*");
        registry.addHandler(chatWebSocketHandler, "/websocket/chat").setAllowedOrigins("*");
    }

}