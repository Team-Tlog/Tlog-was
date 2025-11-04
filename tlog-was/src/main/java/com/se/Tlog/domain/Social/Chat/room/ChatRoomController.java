package com.se.Tlog.domain.Social.Chat.room;

import com.se.Tlog.domain.Social.Chat.message.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.se.Tlog.global.response.success.SuccessRes;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final ChatService chatService;

    @GetMapping("/room/{hostId}")
    @Operation(
            summary = "유저 채팅방 리스트 조회",
            description = "특정 유저가 참여하고 있는 채팅방 리스트 조회합니다.",
            tags = {"ChatRoom 관리"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            parameters = {@Parameter(name = "hostId", description = "채팅방 리스트 조회하는 유저의 UUID 입니다.")},
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse( responseCode = "404", description = "존재하지 않는 사용자 입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> getRoomList(@PathVariable(name = "hostId") UUID hostId) {
        return ResponseEntity.ok().body(SuccessRes.from(chatRoomService.getRoomList(hostId)));
    }

    @GetMapping("/room/{roomId}/messages")
    @Operation(
            summary = "채팅방 메시지 히스토리 조회",
            description = "채팅방 입장 시 과거 메시지를 페이지네이션으로 조회합니다.",
            tags = {"ChatRoom 관리"},
            parameters = {
                    @Parameter(name = "roomId", description = "채팅방 ID"),
                    @Parameter(name = "size", description = "한 페이지당 메시지 수 (기본값: 50)")
            }
    )
    public ResponseEntity<?> getChatHistory(
            @PathVariable Long roomId,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Long beforeMessageId
    ) {
        return ResponseEntity.ok()
                .body(SuccessRes.from(
                        chatService.getChatHistory(roomId, beforeMessageId, size)
                ));
    }

}
