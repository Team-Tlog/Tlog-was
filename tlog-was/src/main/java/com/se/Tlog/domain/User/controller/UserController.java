package com.se.Tlog.domain.User.controller;

import com.se.Tlog.domain.User.application.UserService;
import com.se.Tlog.domain.User.controller.dto.EmailUpdateRequest;
import com.se.Tlog.domain.User.controller.dto.SnsIdUpdateRequest;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import com.se.Tlog.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /*
    * 이메일 관련 코드가 현재로써는 없어도 될 것 같아 주석처리 하겠습니다.
    * 확장시 이메일이 꼭 필요하게 된다면 그대로 사용하고 아니면 삭제하는 방향으로
    * 잡으면 될 것 같아요.
    * */
    /*@PatchMapping("/email")
    @Operation(
            summary = "사용자 email 업데이트",
            description = "사용자 본인의 이메일을 설정 또는 수정합니다.",
            tags = {"User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자 이메일 업데이트 합니다.",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "이메일 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> updateEmail(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody EmailUpdateRequest request) {

        userService.updateEmail(UUID.fromString(user.getId()),request.email());
        return ResponseEntity
                .status(SuccessType.OK.getStatus())
                .body(SuccessRes.from(SuccessType.OK));
    }*/

    @PatchMapping("/snsId")
    @Operation(
            summary = "SNS ID(유저명) 업데이트",
            description = """
        사용자 본인의 SNS ID(서비스 내 고유 사용자명)를 설정 또는 수정합니다.  
        중복된 SNS ID는 사용할 수 없습니다. 
        """,
            tags = {"User"},
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "SNS ID 업데이트 요청 (snsId 필드 필수)",
                    required = true
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "SNS ID 업데이트 성공"),
                    @ApiResponse(responseCode = "404", description = "사용자 정보 없음"),
                    @ApiResponse(responseCode = "409", description = "이미 존재하는 SNS ID"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> updateSnsId(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody SnsIdUpdateRequest request) {

        String newAccessToken = userService.updateSnsId(UUID.fromString(user.getId()), request.snsId());
        return ResponseEntity
                .status(SuccessType.OK.getStatus())
                .header("Authorization", newAccessToken)
                .body(SuccessRes.from(SuccessType.OK));
    }
}
