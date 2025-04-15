package com.portfolio.blog.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry){
        // endpoint 설정 : /stomp/chat
        // setAllowedOrigins("*")는 모든 ip에서 접속 가능하도록 해줌
        // withSockJS 낮은 버전 브라우저도 사용할 수 있도록.
        registry.addEndpoint("/stomp/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //클라이언트로 메세지를 응답 해 줄 때 prefix 정의 - 클라이언트가 메세지를 받을 때
        registry.enableSimpleBroker("/sub");
        //클라이언트에서 메세지 송신 시 붙일 prefix 정의 - 클라이언트가 메세지를 보낼때
        registry.setApplicationDestinationPrefixes("/pub");
    }

}
