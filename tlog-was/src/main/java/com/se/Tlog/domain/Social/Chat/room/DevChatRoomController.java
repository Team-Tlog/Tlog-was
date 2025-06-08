package com.se.Tlog.domain.Social.Chat.room;

import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.se.Tlog.domain.User.domain.Role;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@ConditionalOnProperty(name = "springdoc.swagger-ui.enabled", havingValue = "true") // 테스트 환경에서만 허용
@RequestMapping("/api/chat")
public class DevChatRoomController {
    private final AccessTokenProvider accessTokenProvider;
    private final UserRepository userRepository;    // 테스트 때문에 넣어둠 나중에 지우겠습니다!
    
    // test 편의성 때문에 토큰 생성 api 작성
    @PostMapping("/{userId}")
    @Operation(
            summary = "테스트 편의성을 위한 토큰 발급 API",
            description = "특정 유저에 대한 토큰을 발급합니다.",
            tags = {"[DEV]"},
            parameters = {@Parameter(name = "userId", description = "유저의 UUID 입니다.")},
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공")
            }
    )
    public ResponseEntity<?> createToken(@PathVariable(name = "userId") UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        String snsId = user.getSnsId();
        return ResponseEntity.ok()
                .body(accessTokenProvider.generateToken(
                        userId.toString(),
                        Role.USER.getValue(),
                        snsId == null ? "" : snsId,
                        user.getName()
                ));
    }
}
