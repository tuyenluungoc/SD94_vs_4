package org.example.sd_94vs1.service;

import org.example.sd_94vs1.entity.Message;
import org.example.sd_94vs1.entity.User;
import org.example.sd_94vs1.repository.MessageRepository;
import org.example.sd_94vs1.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Constructor injection
    public MessageService(MessageRepository messageRepository, UserRepository userRepository, SimpMessagingTemplate messagingTemplate) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.messagingTemplate = messagingTemplate; // Đảm bảo SimpMessagingTemplate được inject
    }

    // Lưu tin nhắn và gửi tin nhắn tới các admin
    public Message saveMessage(String senderCode, String receiverCode, String content) {
        User sender = new User();
        sender.setUserCode(senderCode);

        User receiver = null;
        if (receiverCode != null) {
            receiver = new User();
            receiver.setUserCode(receiverCode);
        }

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .build();

        messageRepository.save(message);

        // Gửi tin nhắn đến tất cả admin
        List<User> admins = userRepository.findByRoleTrue(); // Lấy tất cả admin
        for (User admin : admins) {
            messagingTemplate.convertAndSend("/topic/admins", content); // Gửi tin nhắn tới các admin
        }

        return message;
    }

    // Lấy tất cả tin nhắn liên quan đến một user (gửi hoặc nhận)
    public List<Message> getMessagesByUser(String userCode) {
        return messageRepository.findBySenderUserCodeOrReceiverUserCode(userCode);
    }

    // Lấy danh sách người dùng đã gửi hoặc nhận tin nhắn
    public List<User> getUsersWithMessages() {
        return messageRepository.findDistinctUsersWithMessages();
    }

    // Đánh dấu tin nhắn là đã đọc
    public void markMessageAsRead(Long messageId) {
        Message message = messageRepository.findById(messageId).orElseThrow(() ->
                new IllegalArgumentException("Không tìm thấy tin nhắn với ID: " + messageId));
        message.setIsRead(true);
        messageRepository.save(message);
    }

    // Lấy danh sách người dùng đã gửi hoặc nhận tin nhắn (bao gồm cả người nhận)
    public List<User> getUsersWithMessagesIncludingReceivers() {
        return messageRepository.findDistinctUsersWithMessagesIncludingReceivers();
    }

    public List<User> getAdmins() {
        return userRepository.findByRoleTrue();  // Gọi phương thức từ repository
    }

}
