package com.se.Tlog.domain.Social.Chat.controller;

import com.se.Tlog.domain.Social.Chat.controller.dto.ChatRoomResponseDto;
import com.se.Tlog.domain.Social.Chat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    @PostMapping("/room/{hostId}")
    public ResponseEntity<?> createRoom(@PathVariable(name = "hostId") UUID hostId) {
        ChatRoomResponseDto chatRoomResponseDto = chatRoomService.create(hostId);
        return ResponseEntity.ok().body(chatRoomResponseDto);
    }

    @GetMapping("/room/{hostId}")
    public ResponseEntity<?> getRoomList(@PathVariable(name = "hostId") UUID hostId){
        return ResponseEntity.ok().body(chatRoomService.getRoomList(hostId));
    }
}
