package com.se.Tlog.domain.Social.Chat.service;

import com.se.Tlog.domain.Social.Chat.domain.ChatRoom;
import com.se.Tlog.domain.Social.Chat.domain.ChatRoomUser;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatRoomUserRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;

    public ChatRoom create(UUID hostId) {
        ChatRoom chatRoom = ChatRoom.create(hostId);
        chatRoomRepository.save(chatRoom);
        User host = userRepository.findById(hostId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        ChatRoomUser chatRoomUser = ChatRoomUser.join(chatRoom,host);
        chatRoomUserRepository.save(chatRoomUser);

        return chatRoom;
    }

}
