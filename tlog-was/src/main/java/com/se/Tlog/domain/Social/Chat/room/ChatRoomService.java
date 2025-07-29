package com.se.Tlog.domain.Social.Chat.room;

import com.se.Tlog.domain.Social.Chat.room.dto.ChatRoomListResponseDto;
import com.se.Tlog.domain.Social.Chat.message.ChatMessage;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomUserRepository;
import com.se.Tlog.domain.Social.Chat.read.ChatReadStatusService;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.domain.service.UserDomainService;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserDomainService userDomainService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadStatusService chatReadStatusService;

    public Long create(User user,UUID teamId) {
        ChatRoom chatRoom = ChatRoom.create(user.getId(), teamId);
        chatRoomRepository.save(chatRoom);

        ChatRoomUser chatRoomUser = ChatRoomUser.join(chatRoom, user);

        chatRoomUserRepository.save(chatRoomUser);
        return chatRoom.getId();
    }

    public void joinChatRoom(User user, UUID teamId) {
        ChatRoom chatRoom = chatRoomRepository.findByTeamId(teamId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        ChatRoomUser chatRoomUser = ChatRoomUser.join(chatRoom, user);
        chatRoomUserRepository.save(chatRoomUser);
    }

    public List<ChatRoomListResponseDto> getRoomList(UUID userId) {

        userDomainService.validateExists(userId);
        List<ChatRoomUser> chatRoomUserList = chatRoomUserRepository.findChatRoomByUserId(userId); // 유저가 참여중인 ChatRoomList
        List<ChatRoom> rooms = chatRoomUserList.stream().map(ChatRoomUser::getChatRoom).toList();

        List<Long> roomIds = rooms.stream().map(ChatRoom::getId).toList();

        Map<Long, Integer> unreadCountMap = chatReadStatusService.unreadMessagesCount(userId, roomIds);

        return rooms.stream().map(room -> toChatRoomListDto(room, unreadCountMap)).toList();
    }

    private ChatRoomListResponseDto toChatRoomListDto(ChatRoom room, Map<Long, Integer> unreadCountMap) {
        ChatMessage lastMessage = room.getLastChatId() != null ?
                chatMessageRepository.findById(room.getLastChatId()).orElse(null) : null;
        String content = lastMessage != null ? lastMessage.getContent() : null;
        LocalDateTime lastMessageSentAt = lastMessage != null ? lastMessage.getSendAt() : null;
        int unreadCount = unreadCountMap.getOrDefault(room.getId(), 0);
        int countChatRoomJoinUsers = chatRoomUserRepository.countChatRoomJoinUsers(room.getId());

        return ChatRoomListResponseDto.from(room.getId(), content, lastMessageSentAt,countChatRoomJoinUsers,unreadCount);
    }
}
