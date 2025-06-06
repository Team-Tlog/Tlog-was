package com.se.Tlog.domain.Team.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Team.application.TeamProfileService;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/team")
@RequiredArgsConstructor
@Tag(name = "팀 프로필 관리")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class TeamProfileController {
	private final TeamProfileService teamProfileService;
	
	@PostMapping("/{teamId}/tbti")
	@Operation (
			summary = "팀 TBTI 변경",
    		description = "팀의 TBTI를 변경합니다.<br><b>변경된 TBTI를 반환합니다. (SENA, ROLA 등)</b>",
	        parameters = @Parameter(name = "tbti", example = "00127623", description = "TBTI 숫자 코드입니다. (0~99999999)"),
			responses = {
					@ApiResponse(responseCode = "200", description = "팀 TBTI 변경됨, 변경된 TBTI가 반환됩니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<String>> createTeam(
	        @PathVariable(name = "teamId") UUID teamId,
	        @RequestParam(name = "tbti") int tbtiValue) {
		return ResponseEntity.ok(SuccessRes.of(SuccessType.OK,
		        teamProfileService.updateTeamTbti(teamId, tbtiValue)));
	}
}
