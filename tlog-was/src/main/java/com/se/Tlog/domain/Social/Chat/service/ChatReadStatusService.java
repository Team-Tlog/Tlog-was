package com.se.Tlog.domain.Social.Chat.service;

import com.se.Tlog.domain.Social.Chat.domain.ChatReadStatus;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.repository.jpa.ChatReadStatusRepository;
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

    @Transactional
    public void updateReadStatus(UUID userId, Long roomId, Long lastReadMessageId) {
        ChatReadStatus status = chatReadStatusRepository.findByUserIdAndChatRoomId(userId, roomId);
        status.updateRead(lastReadMessageId);
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
