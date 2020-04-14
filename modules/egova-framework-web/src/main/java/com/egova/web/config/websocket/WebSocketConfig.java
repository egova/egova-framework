package com.egova.web.config.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {

        messageConverters.add(new MappingJackson2MessageConverter() {
            {
                setObjectMapper(objectMapper);
            }
        });
        return true;
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {

                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                assert accessor != null;
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    //第一次连接，用来处理用户数据，做验证
                    Principal user = new UserPrincipal(accessor.getLogin());
                    accessor.setUser(user);
                }
                return message;
            }
        });
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                log.info("send: " + message);
                return super.preSend(message, channel);
            }
        });
    }


    /**
     * 配置名为stomp的endpoint端点
     *
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/stomp-websocket").setAllowedOrigins("*").withSockJS();

    }


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        Set<String> destinationPrefixes = new HashSet<>();
        destinationPrefixes.add("/topic");
        destinationPrefixes.add("/queue");
        destinationPrefixes.add("/user");
        // 推送到前端的前缀
        registry.enableSimpleBroker(destinationPrefixes.toArray(new String[destinationPrefixes.size()]));
        //        registry.enableSimpleBroker("/face");
        // 客户端向后台发送时需要指定的前缀,客户端发送请求以/app开始
        registry.setApplicationDestinationPrefixes("/app");
        // 可以已“.”来分割路径，看看类级别的@messageMapping和方法级别的@messageMapping
        //        registry.setPathMatcher(new AntPathMatcher("."));
        // 指定用户发送（一对一）的主题前缀是“/user/”
        registry.setUserDestinationPrefix("/user/");
    }

}
