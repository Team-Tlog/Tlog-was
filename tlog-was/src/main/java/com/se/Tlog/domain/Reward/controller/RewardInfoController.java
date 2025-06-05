package com.se.Tlog.domain.Reward.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Reward.application.RewardInfoService;
import com.se.Tlog.domain.Reward.controller.dto.CreateRewardInfoRequest;
import com.se.Tlog.domain.Reward.controller.dto.RewardInfoDto;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/reward-info")
@RequiredArgsConstructor
public class RewardInfoController {
	private final RewardInfoService rewardInfoService;
	
	@GetMapping
	@Operation (
			summary = "전체 보상 형식 가져오기",
    		description = "서비스 내 모든 보상 형식을 반환합니다.",
			tags = {"보상 형식 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "서비스 내 모든 보상 형식을 반환합니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 보상 형식을 조회하는데 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<RewardInfoDto>>> getAllRewardInfo() {
		return ResponseEntity
				.ok()
				.body(SuccessRes.from(
						rewardInfoService.getAllRewardInfo()
						.stream().map(RewardInfoDto::fromEntity)
						.toList()));
	}
	
	@PostMapping
    @Operation (
    		summary = "새 보상 형식 등록하기",
    		description = "새로운 보상 형식을 생성하여 등록합니다.",
			tags = {"보상 형식 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "성공, 새 보상 형식이 서비스에 추가됩니다."),
					@ApiResponse(responseCode = "400", description = "실패, 새 보상 형식의 조건이 잘못되었습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class))),
					@ApiResponse(responseCode = "500", description = "실패, 서버 내부 오류",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> createRewardInfo(@RequestBody CreateRewardInfoRequest requestData) {
		rewardInfoService.addNewRewardInfo(requestData);
		return ResponseEntity.ok().body(SuccessRes.from(SuccessType.CREATED)); 
	}
	
	@DeleteMapping("/{rewardInfoId}")
    @Operation (
    		summary = "보상 형식 삭제하기",
    		description = "특정 보상 형식을 서비스에서 제거합니다.",
			tags = {"보상 형식 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			parameters = {
					@Parameter(name = "rewardInfoId", description = "삭제할 보상 형식의 id")
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "성공, 보상 형식이 서비스에서 삭제되었습니다."),
					@ApiResponse(responseCode = "500", description = "실패, 서버 내부 오류",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> deleteRewardInfo(@PathVariable(name = "rewardInfoId") Long rewardInfoId) {
		rewardInfoService.deleteRewardInfo(rewardInfoId);
		return ResponseEntity.ok().body(SuccessRes.from(SuccessType.OK));
	}
}
