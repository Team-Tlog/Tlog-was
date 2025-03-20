package com.se.Tlog.domain.Social.Chat.service;

import com.se.Tlog.domain.Social.Chat.controller.dto.ChatMessageRequestDto;
import com.se.Tlog.domain.Social.Chat.domain.ChatMessage;
import com.se.Tlog.domain.Social.Chat.domain.ChatRoom;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    public ChatMessage createMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User sender = userRepository.findById(chatMessageRequestDto.senderId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequestDto.chatRoomId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        return ChatMessage.create(sender, chatRoom, chatMessageRequestDto.content());
    }


}
