package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageRequestDto;
import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
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
    private final UserRepository userRepository;

    @Transactional
    public void receivedChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User sender = userRepository.findById(chatMessageRequestDto.senderId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequestDto.chatRoomId())
                .orElseThrow(() -> new CustomException(ErrorType.CHATROOM_NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.create(sender, chatRoom, chatMessageRequestDto.content());
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        chatRoom.modifyLastMessage(chatMessage.getId());

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + chatRoom.getId(),
                savedChatMessage
        );
    }
}
