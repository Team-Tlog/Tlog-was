package com.se.Tlog.domain.Social.Chat.message;

import com.se.Tlog.domain.Social.Chat.message.dto.ChatMessageReadDto;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat/message")
public class ChatMessageController {    //Rest 전용

    private final ChatService chatService;

    @PatchMapping("/read")
    @Operation(
            summary = "채팅 메시지 읽음 처리",
            description = "특정 사용자가 특정 메시지를 읽었다고 서버에 알립니다.",
            tags = {"Chat 관리"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "메시지 읽음 처리 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자 또는 메시지를 찾을 수 없습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> checkMessage(@RequestBody ChatMessageReadDto chatMessageCheckDto) {
        chatService.checkMessage(chatMessageCheckDto);
        return ResponseEntity.ok().body(SuccessRes.from(SuccessType.MESSAGE_READ_SUCCESS));
    }
}
