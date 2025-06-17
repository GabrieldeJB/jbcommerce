package com.meddoc.meddoc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket") // Este endpoint precisa estar igual no JS
                .setAllowedOriginPatterns("*")
                .withSockJS(); // Necessário se estiver usando SockJS
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic"); // onde as mensagens serão publicadas
        registry.setApplicationDestinationPrefixes("/app"); // prefixo para onde o front envia
    }
}
