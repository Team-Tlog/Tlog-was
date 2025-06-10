package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageDto;
import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageRequestDto;
import com.se.Tlog.domain.Social.Chat.message.dto.TeamChatSummaryDto;
import com.se.Tlog.domain.Social.Chat.read.ChatReadUsers;
import com.se.Tlog.domain.Social.Chat.read.repository.jpa.ChatReadStatusRepository;
import com.se.Tlog.domain.Social.Chat.read.repository.jpa.ChatReadUsersRepository;
import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomUserRepository;
import com.se.Tlog.domain.Social.Chat.session.repository.jpa.ConnectedUserSessionRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatConsumerService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatReadUsersRepository chatReadUsersRepository;
    private final ChatReadStatusRepository chatReadStatusRepository;
    private final ConnectedUserSessionRepository connectedUserSessionRepository;


    @Transactional
    public void receivedChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        User sender = userRepository.findById(chatMessageRequestDto.senderId())
                .orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequestDto.chatRoomId())
                .orElseThrow(() -> new CustomException(ErrorType.CHATROOM_NOT_FOUND));

        ChatMessage chatMessage = ChatMessage.create(sender, chatRoom, chatMessageRequestDto.content());
        ChatMessage savedChatMessage = chatMessageRepository.save(chatMessage);

        chatRoom.modifyLastMessage(chatMessage.getId());

        ChatReadUsers chatReadUsers = ChatReadUsers.create(sender, chatMessage);
        chatReadUsersRepository.save(chatReadUsers);

        messagingTemplate.convertAndSend(
                "/sub/chat/room/" + chatRoom.getId(),
                ChatMessageDto.from(sender, chatRoom, savedChatMessage)
        );

        // connectedUserIds = 현재 채팅방 구독중인 유저
        Set<UUID> connectedUserIds = connectedUserSessionRepository.findConnectedUserIdsByRoomId(chatRoom.getId());
        List<User> participants = chatRoomUserRepository.findParticipantsByChatRoomId(chatRoom.getId());

        // 모두가 구독중인 경우 실시간 채팅 목록 갱신해줄 필요 x
        boolean allConnected = participants.stream()
                .allMatch(user -> connectedUserIds.contains(user.getId()));
        if (allConnected) {
            return;
        }

        List<UUID> targetUserIds = participants.stream()
                .map(User::getId)
                .filter(userId -> !connectedUserIds.contains(userId))
                .toList();
        List<Object[]> readStatuses = chatReadStatusRepository.findLastReadMessageIdsForParticipants(targetUserIds, chatRoom.getId());
        Map<UUID, Long> lastReadMap = new HashMap<>();

        for (Object[] row : readStatuses) {
            UUID userId = (UUID) row[0];
            Long lastReadMessageId = (Long) row[1];
            lastReadMap.put(userId, lastReadMessageId);
        }

        for (UUID targetUserId : targetUserIds) {
            Long lastReadMessage = lastReadMap.getOrDefault(targetUserId, 0L);
            int unreadCount = chatMessageRepository.countUnreadMessages(chatRoom.getId(), lastReadMessage);
            messagingTemplate.convertAndSend(
                    "/sub/chat/room" + targetUserId,
                    TeamChatSummaryDto.from(targetUserId, chatRoom.getId(), savedChatMessage, unreadCount)
            );
        }
    }
}
