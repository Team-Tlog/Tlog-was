package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageReadDto;
import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageRequestDto;
import com.se.Tlog.domain.Social.Chat.read.ChatReadUsers;
import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.read.repository.jpa.ChatReadUsersRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadUsersRepository chatReadUsersRepository;
    private final RabbitPublisher rabbitPublisher;

    @Transactional
    public void sendChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User sender = userRepository.findById(chatMessageRequestDto.senderId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequestDto.chatRoomId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.create(sender, chatRoom, chatMessageRequestDto.content());

        chatRoom.modifyLastMessage(chatMessage.getId());

        String routingKey = "recipient." + chatRoom.getId();
        rabbitPublisher.publish(routingKey, chatMessage);

    }

    @Transactional
    public void checkMessage(ChatMessageReadDto chatMessageCheckDto) {
        User user = userRepository.findById(chatMessageCheckDto.readerId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        ChatMessage chatMessage = chatMessageRepository.findById(chatMessageCheckDto.messageId())
                .orElseThrow(() -> new CustomException(ErrorType.MESSAGE_NOT_FOUND));

        Boolean alreadyRead = chatReadUsersRepository.existsByUserAndMessage(user, chatMessage);
        if(!alreadyRead){
            ChatReadUsers chatReadUsers = ChatReadUsers.create(user, chatMessage);
            chatReadUsersRepository.save(chatReadUsers);
        }
    }
}
