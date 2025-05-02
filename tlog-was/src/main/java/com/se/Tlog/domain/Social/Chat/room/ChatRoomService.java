package com.se.Tlog.domain.Social.Chat.room;

import com.se.Tlog.domain.Social.Chat.room.dto.ChatRoomListResponseDto;
import com.se.Tlog.domain.Social.Chat.room.dto.RequestInviteList;
import com.se.Tlog.domain.Social.Chat.message.ChatMessage;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.room.dto.ChatRoomResponseDto;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomUserRepository;
import com.se.Tlog.domain.Social.Chat.read.ChatReadStatusService;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.domain.service.UserDomainService;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
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
    private final UserRepository userRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final UserDomainService userDomainService;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadStatusService chatReadStatusService;


    public ChatRoomResponseDto create(UUID hostId, RequestInviteList requestInviteList) {
        ChatRoom chatRoom = ChatRoom.create(hostId);
        chatRoomRepository.save(chatRoom);

        Set<UUID> allUserIds = new HashSet<>(requestInviteList.inviteList());
        allUserIds.add(hostId);

        List<User> users = userRepository.findAllById(allUserIds);
        if(users.size() != allUserIds.size()){
            throw new CustomException(ErrorType.INVITE_USER_NOT_FOUND);
        }

        List<ChatRoomUser> chatRoomUsers = users.stream()
                .map(user -> ChatRoomUser.join(chatRoom, user))
                .toList();
        chatRoomUserRepository.saveAll(chatRoomUsers);

        return ChatRoomResponseDto.from(chatRoom.getId(),chatRoom.getHostId(),users);
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
