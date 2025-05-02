package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatConsumerService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    @Transactional
    public void receivedChatMessage(ChatMessage chatMessage) {
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        ChatRoom chatRoom = chatRoomRepository.findById(savedChatMessage.getChatRoom().getId())
                .orElseThrow(() -> new CustomException(ErrorType.CHATROOM_NOT_FOUND));

        chatRoom.modifyLastMessage(chatMessage.getId());

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + chatRoom.getId(),
                savedChatMessage
        );
    }
}
