package com.se.Tlog.domain.Social.Chat.controller;

import com.se.Tlog.domain.Social.Chat.controller.dto.ChatRoomResponseDto;
import com.se.Tlog.domain.Social.Chat.controller.dto.RequestInviteList;
import com.se.Tlog.domain.Social.Chat.service.ChatRoomService;
import com.se.Tlog.domain.User.domain.Role;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final AccessTokenProvider accessTokenProvider;

    @PostMapping("/room/{hostId}")
    public ResponseEntity<?> createRoom(@PathVariable(name = "hostId") UUID hostId,
                                        @RequestBody RequestInviteList requestInviteList) {
        ChatRoomResponseDto chatRoomResponseDto = chatRoomService.create(hostId, requestInviteList);

        return ResponseEntity.ok().body(chatRoomResponseDto);
    }

    @GetMapping("/room/{hostId}")
    public ResponseEntity<?> getRoomList(@PathVariable(name = "hostId") UUID hostId) {
        return ResponseEntity.ok().body(chatRoomService.getRoomList(hostId));
    }
    // test 편의성 때문에 토큰 생성 api 작성
    @PostMapping("/{userId}")
    public ResponseEntity<?> createToken(@PathVariable(name = "userId") UUID userId) {
        return ResponseEntity.ok().body(accessTokenProvider.generateToken(userId.toString(), Role.USER.getValue()));
    }
}
