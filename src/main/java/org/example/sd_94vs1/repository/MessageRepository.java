package org.example.sd_94vs1.repository;


import org.example.sd_94vs1.entity.Message;
import org.example.sd_94vs1.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    // Lấy danh sách tin nhắn của một user (gửi hoặc nhận)
    @Query("SELECT m FROM Message m WHERE m.sender.userCode = :userCode OR (m.receiver IS NOT NULL AND m.receiver.userCode = :userCode)")
    List<Message> findBySenderUserCodeOrReceiverUserCode(String userCode);

    // Lấy danh sách người dùng đã gửi hoặc nhận tin nhắn (bao gồm cả người nhận nếu có)
    @Query("SELECT DISTINCT u FROM User u WHERE u.userCode IN " +
            "(SELECT m.sender.userCode FROM Message m UNION SELECT m.receiver.userCode FROM Message m WHERE m.receiver IS NOT NULL)")
    List<User> findDistinctUsersWithMessagesIncludingReceivers();

    // Lấy danh sách người gửi tin nhắn (không trùng lặp)
    @Query("SELECT DISTINCT m.sender FROM Message m")
    List<User> findDistinctUsersWithMessages();
}