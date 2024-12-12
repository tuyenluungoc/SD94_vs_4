package org.example.sd_94vs1.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID tự động tăng
    Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sender_code", referencedColumnName = "user_code", nullable = false)
    User sender; // Người gửi tin nhắn

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_code", referencedColumnName = "user_code")
    User receiver; // Người nhận tin nhắn (admin hoặc người dùng khác)

    @Column(nullable = false)
    String content; // Nội dung tin nhắn

    @Column(nullable = false)
    LocalDateTime timestamp; // Thời gian gửi tin nhắn

    @Column(nullable = false)
    Boolean isRead = false; // Trạng thái đã đọc (chỉ dùng cho admin)

}
