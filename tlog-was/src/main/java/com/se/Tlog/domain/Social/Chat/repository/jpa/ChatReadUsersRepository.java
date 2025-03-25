package com.se.Tlog.domain.Social.Chat.repository.jpa;

import com.se.Tlog.domain.Social.Chat.domain.ChatMessage;
import com.se.Tlog.domain.Social.Chat.domain.ChatReadUsers;
import com.se.Tlog.domain.User.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatReadUsersRepository extends JpaRepository<ChatReadUsers,Long> {
    Boolean existsByUserAndMessage(User user, ChatMessage message);
}
