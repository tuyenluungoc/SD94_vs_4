package org.example.sd_94vs1.config.websocket;


import org.example.sd_94vs1.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    // Phương thức này sẽ nhận tin nhắn và gửi tới tất cả admin
    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload Message message) {
        // Gửi tin nhắn tới tất cả admin qua WebSocket
        simpMessagingTemplate.convertAndSend("/topic/admins", message.getContent());
    }

    // Khi admin trả lời tin nhắn
    @MessageMapping("/sendMessageToUser")
    public void sendMessageToUser(Message message) {
        // Gửi tin nhắn trả lời tới người dùng
        simpMessagingTemplate.convertAndSend("/topic/users", message.getContent());
    }
}
