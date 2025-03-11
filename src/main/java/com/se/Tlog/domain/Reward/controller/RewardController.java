package com.se.Tlog.domain.Reward.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Reward.Service.RewardService;
import com.se.Tlog.domain.Reward.dto.RewardInfoDto;
import com.se.Tlog.domain.Reward.dto.AddRewardToUserRequest;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/reward")
@RequiredArgsConstructor
public class RewardController {
	private final RewardService rewardService;
	
	@PostMapping
	@Operation (
			summary = "사용자에게 보상 지급",
    		description = "사용자에게 보상 지급을 요청합니다.",
			tags = {"사용자 보상 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공, 사용자의 보상 지급 결과를 반환합니다."),
					@ApiResponse(responseCode = "404", description = "요청한 사용자나 보상 형식이 존재하지 않습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class))),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 보상 지급 처리에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<Boolean>> addRewardToUser(@RequestBody AddRewardToUserRequest request) {
		try {
			return ResponseEntity
					.ok(SuccessRes.from(rewardService.addRewardToUser(request.userId(), request.rewardInfoId())));
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorType.NOT_FOUND);
		} catch (RuntimeException e) {
			throw e;
		}
	}
	
	@GetMapping
	@Operation (
			summary = "사용자가 보유한 보상 조회",
    		description = "특정 사용자가 보유한 보상들을 조회합니다.",
			tags = {"사용자 보상 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공, 주어진 사용자가 보유한 보상 리스트를 반환합니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<RewardInfoDto>>> getRewardsByUser(@RequestParam UUID userId) {
		return ResponseEntity.ok(
				SuccessRes.from(
						rewardService.getAllRewardOfUser(userId)
						.stream().map(RewardInfoDto::fromEntity)
						.toList()));
	}
}
