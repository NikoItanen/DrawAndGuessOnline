package com.nijoat.backend.config;

import com.nijoat.backend.handler.MainWebSocketHandler;
import com.nijoat.backend.handler.RoomWebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;



@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MainWebSocketHandler mainWebSocketHandler;
    private final RoomWebSocketHandler roomWebSocketHandler;

    @Autowired
    public WebSocketConfig(MainWebSocketHandler mainWebSocketHandler, RoomWebSocketHandler roomWebSocketHandler) {
        this.mainWebSocketHandler = mainWebSocketHandler;
        this.roomWebSocketHandler = roomWebSocketHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(mainWebSocketHandler, "/websocket/mainmenu").setAllowedOrigins("*");
        registry.addHandler(roomWebSocketHandler, "/websocket/room").setAllowedOrigins("*");
    }

}