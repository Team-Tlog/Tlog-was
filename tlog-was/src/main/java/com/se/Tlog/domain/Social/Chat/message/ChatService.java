package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.message.dto.ChatHistoryResponse;
import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageDto;
import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageReadDto;
import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageRequestDto;
import com.se.Tlog.domain.Social.Chat.read.ChatReadUsers;
import com.se.Tlog.domain.Social.Chat.room.ChatRoom;
import com.se.Tlog.domain.Social.Chat.message.repository.jpa.ChatMessageRepository;
import com.se.Tlog.domain.Social.Chat.read.repository.jpa.ChatReadUsersRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomRepository;
import com.se.Tlog.domain.Social.Chat.room.repository.jpa.ChatRoomUserRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.domain.service.UserDomainService;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ChatService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatReadUsersRepository chatReadUsersRepository;
    private final RabbitPublisher rabbitPublisher;
    private final UserDomainService userDomainService;
    private final ChatRoomUserRepository chatRoomUserRepository;

    @Transactional
    public void sendChatMessage(ChatMessageRequestDto chatMessageRequestDto) {
        userDomainService.validateExists(chatMessageRequestDto.senderId());

        ChatRoom chatRoom = chatRoomRepository.findById(chatMessageRequestDto.chatRoomId())
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));

        String routingKey = "recipient." + chatRoom.getId();
        rabbitPublisher.publish(routingKey, chatMessageRequestDto);
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

    @Transactional(readOnly = true)
    public ChatHistoryResponse getChatHistory(Long roomId, Long beforeMessageId, int size) {
        // 채팅방 존재 확인
        if (!chatRoomRepository.existsById(roomId)) {
            throw new CustomException(ErrorType.CHATROOM_NOT_FOUND);
        }

        // 채팅방 전체 참여자 수
        int totalParticipants = chatRoomUserRepository.countChatRoomJoinUsers(roomId);

        List<ChatMessage> messages;
        if (beforeMessageId == null) {
            // 첫 요청: 최신 메시지부터 size개 가져온다.
            messages = chatMessageRepository.findRecentMessages(
                    roomId,
                    PageRequest.of(0, size) // 0페이지 , size개
            );
        } else {
            /*
            * 추가 요청 (스크롤해서 과거 메시지 로드)
            * - beforeMessageId 보다 오래된 메시지 size개 가져옴
            * */
            messages = chatMessageRepository.findMessagesBeforeCursor(
                    roomId,
                    beforeMessageId,
                    PageRequest.of(0, size)
            );
        }

        List<Long> messageIds = messages.stream()
                .map(ChatMessage::getId)
                .toList();

        List<Object[]> readCounts = chatReadUsersRepository.countReadsByMessageIds(messageIds);
        Map<Long, Integer> readCountMap = new HashMap<>();
        for (Object[] row : readCounts) {
            Long messageId = (Long) row[0];
            Long count = (Long) row[1];
            readCountMap.put(messageId, count.intValue());
        }

        List<ChatMessageDto> messageDtos = messages.stream()
                .map(message -> {
                    int readCount = readCountMap.getOrDefault(message.getId(), 0);
                    return ChatMessageDto.from(message, readCount, totalParticipants);
                })
                .toList();
        /*
        * 다음 커서 계산
        * - 가져온 메시지 중 가장 오래된 메시지의 ID
        * - 다음 요청 시 이 ID를 beforeMessageId로 사용
        * */
        Long nextCursor = messages.isEmpty() ? null : messages.get(messages.size() - 1).getId();
        boolean hasMore = messages.size() == size;

        return ChatHistoryResponse.from(
                messageDtos,
                nextCursor,
                hasMore
        );
    }
}
