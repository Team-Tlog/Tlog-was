package com.se.Tlog.domain.Social.Chat.room;

import com.se.Tlog.domain.Social.Chat.room.dto.RequestInviteList;
import com.se.Tlog.domain.Social.Chat.room.dto.ChatRoomResponseDto;
import com.se.Tlog.domain.User.domain.Role;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(
            summary = "채팅방을 생성합니다.",
            description = "특정 유저가 참여하고 있는 채팅방 리스트 조회합니다.",
            tags = {"ChatRoom 관리"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            parameters = {@Parameter(name = "hostId", description = "채팅방 리스트 조회하는 유저의 UUID 입니다.")},
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse( responseCode = "400", description = "초대하려는 유저 중 존재하지 않는 유저가 있습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> createRoom(@PathVariable(name = "hostId") UUID hostId,
                                        @RequestBody RequestInviteList requestInviteList) {
        ChatRoomResponseDto chatRoomResponseDto = chatRoomService.create(hostId, requestInviteList);
        return ResponseEntity.ok().body(SuccessRes.of(SuccessType.CHATROOM_CREATE,chatRoomResponseDto));
    }

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
    // test 편의성 때문에 토큰 생성 api 작성
    @PostMapping("/{userId}")
    public ResponseEntity<?> createToken(@PathVariable(name = "userId") UUID userId) {
        return ResponseEntity.ok().body(accessTokenProvider.generateToken(userId.toString(), Role.USER.getValue()));
    }
}
