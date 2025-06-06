package com.se.Tlog.domain.User.controller;

import com.se.Tlog.domain.Tbti.controller.dto.TbtiInfoRes;
import com.se.Tlog.domain.User.application.UserService;

import com.se.Tlog.domain.User.controller.dto.ProfileImageRequest;
import com.se.Tlog.domain.User.controller.dto.SnsIdUpdateRequest;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import com.se.Tlog.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Encoding;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
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
            description = "사용자 본인의 이메일을 설정 또는 수정합니다.<br><br><b>인증 토큰이 필요합니다!</b>",
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
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);

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
        <br><br><b>인증 토큰이 필요합니다!</b>
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
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);

        String newAccessToken = userService.updateSnsId(UUID.fromString(user.getId()), request.snsId());
        return ResponseEntity
                .status(SuccessType.OK.getStatus())
                .header("Authorization", newAccessToken)
                .body(SuccessRes.from(SuccessType.OK));
    }

    @Operation(
            summary = "프로필 이미지 업로드",
            description = "사용자의 프로필 이미지를 업로드하고 Firebase Storage에 저장된 CDN URL을 반환합니다."
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "multipart/form-data",
                            schema = @Schema(type = "object", requiredProperties = { "file" }),
                            encoding = @Encoding(name = "file", contentType = "image/*")
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "이미지 업로드 성공 (imageUrl 반환)"),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 데이터 입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            },
            tags = { "User Profile" }
    )
    @PostMapping("/profile-image")
    public ResponseEntity<?> uploadProfileImage(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestBody ProfileImageRequest request
    ) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
        userService.uploadProfileImage(user.getId(), request.imageUrl());

        return ResponseEntity
                .status(SuccessType.OK.getStatus())
                .body(SuccessRes.from(SuccessType.OK));
    }

    @PostMapping("/tbti")
    @Operation(
            summary = "사용자 TBTI 변경",
            description = "사용자의 TBTI를 변경합니다.<br><b>갱신된 TBTI의 설명을 반환합니다.</b>"
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            tags = { "User Profile" },
            parameters = @Parameter(name = "tbti", example = "00127623", description = "TBTI 숫자 코드입니다. (0~99999999)"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "사용자 TBTI 업데이트 성공"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<TbtiInfoRes>> uploadProfileImage(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(name = "tbti") int tbtiValue) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);
        return ResponseEntity.ok(SuccessRes.from(
                userService.updateUserTbti(UUID.fromString(user.getId()), tbtiValue)));
    }
}
