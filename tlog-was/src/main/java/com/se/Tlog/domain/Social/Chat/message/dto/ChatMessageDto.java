package com.se.Tlog.domain.Social.Chat.message.dto;

import com.se.Tlog.domain.Social.Chat.message.ChatMessage;
import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import com.se.Tlog.domain.User.domain.User;

import java.time.LocalDateTime;
import java.util.UUID;

public record ChatMessageDto(
        UUID senderId,
        String senderName,
        Long chatRoomId,
        String content,
        LocalDateTime sendAt
) {
    static public ChatMessageDto from(User user, ChatRoom chatRoom, ChatMessage chatMessage) {
        return new ChatMessageDto(user.getId(), user.getName(), chatRoom.getId(), chatMessage.getContent(), chatRoom.getCreateAt());
    }
}
