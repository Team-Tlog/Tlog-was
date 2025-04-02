package com.se.Tlog.domain.Social.Chat.service;

import com.se.Tlog.domain.Social.Chat.domain.ChatReadStatus;
import com.se.Tlog.domain.Social.Chat.domain.ChatRoom;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatReadStatusRepository;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatReadStatusService {

    private final ChatReadStatusRepository chatReadStatusRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public void updateReadStatus(UUID userId, Long roomId, Long lastReadMessageId) {
        ChatReadStatus status = chatReadStatusRepository.findByUserIdAndChatRoomId(userId, roomId);

        if (status == null) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

            ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                    .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

            status = ChatReadStatus.create(user, chatRoom, lastReadMessageId);
        } else {
            status.updateRead(lastReadMessageId);
        }

        chatReadStatusRepository.save(status);
    }

    public Map<Long, Integer> unreadMessagesCount(UUID userId, List<Long> roomIds) {

        // user가 채팅방마다 마지막으로 읽은 메시지 status 리스트
        List<ChatReadStatus> statuses = chatReadStatusRepository.findByUserIdAndChatRoomIds(userId, roomIds);

        Map<Long, Integer> unreadCountMap = new HashMap<>();

        for (ChatReadStatus status : statuses) {
            Long roomId = status.getChatRoom().getId();
            Long lastReadMessageId = status.getLastReadMessageId();

            int countUnreadMessages = chatMessageRepository.countUnreadMessages(roomId,lastReadMessageId);

            unreadCountMap.put(roomId, countUnreadMessages);
        }
        return unreadCountMap;

    }
}
