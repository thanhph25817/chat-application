package com.example.demo.config;

import com.example.demo.chat.ChatMessage;
import com.example.demo.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j // nghi nhat ky thong tin nguoi dung khi roi nhom chat
public class WebSocketEventListener {

     private final SimpMessageSendingOperations messageTemplate;
    @EventListener
    public void handleWebSocketDisconnectListener(
        SessionDisconnectEvent event // thong bao cho nguoi dung la A da roi nhom
        ){
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
        String userName= (String) headerAccessor.getSessionAttributes().get("username");
        if(userName !=null){
            log.info("User disconnected:{}",userName);
            var chatMessage= ChatMessage.builder().type(MessageType.LEAVE)
                    .sender(userName)
                    .build();
        messageTemplate.convertAndSend("/topic/public",chatMessage);
        }
    }
}
