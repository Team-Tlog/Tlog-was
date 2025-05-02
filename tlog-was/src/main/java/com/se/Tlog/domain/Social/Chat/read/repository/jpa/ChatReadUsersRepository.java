package com.se.Tlog.domain.Social.Chat.read.repository.jpa;

import com.se.Tlog.domain.Social.Chat.message.ChatMessage;
import com.se.Tlog.domain.Social.Chat.read.ChatReadUsers;
import com.se.Tlog.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatReadUsersRepository extends JpaRepository<ChatReadUsers,Long> {
    Boolean existsByUserAndMessage(User user, ChatMessage message);
}
