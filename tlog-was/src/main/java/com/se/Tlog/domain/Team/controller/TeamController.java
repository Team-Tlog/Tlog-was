package com.se.Tlog.domain.Team.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Team.application.TeamService;
import com.se.Tlog.domain.Team.controller.dto.CreateTeamRequestDto;
import com.se.Tlog.domain.Team.controller.dto.TeamResponseDto;
import com.se.Tlog.domain.Team.controller.dto.TeamUserRequestDto;
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
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {
	private final TeamService teamService;
	
	@PostMapping
	@Operation (
			summary = "팀 생성",
    		description = "새로운 여행 팀을 생성합니다.",
			tags = {"팀 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 생성된 팀의 id를 반환합니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 팀 생성에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<UUID>> createTeam(@RequestBody CreateTeamRequestDto request) {
		return ResponseEntity.ok(SuccessRes.from(
				teamService.createTeam(request)));
	}
	
	@GetMapping
	@Operation (
			summary = "팀 조회",
    		description = "특정 유저가 속한 팀을 모두 표시합니다.",
			tags = {"팀 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 팀 생성에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<TeamResponseDto>>> getMyTeam(@RequestParam(name = "userId") UUID userId) {
		return ResponseEntity.ok(SuccessRes.from(
				teamService.getTeamOfUser(userId)));
	}

	@GetMapping("/{teamId}/details")
	@Operation(
			summary = "특정 팀 상세 정보 조회",
			description = "특정 팀의 상세 정보와 장바구니 여행지 목록을 함께 반환합니다.",
			tags = {"팀 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme",
					scopes = {"scope1", "scope2"}
			),
			parameters = {
					@Parameter(name = "teamId", description = "조회할 팀의 UUID", required = true)
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "팀 상세 정보 반환 성공"),
					@ApiResponse(responseCode = "404", description = "존재하지 않는 팀입니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류")
			}
	)
	public ResponseEntity<?> getTeamDetails(@PathVariable UUID teamId) {
		return ResponseEntity.
				ok(SuccessRes.from(teamService.getTeamDetails(teamId)));
	}

	@DeleteMapping("/{teamId}")
	@Operation (
			summary = "팀 삭제",
    		description = "기존 팀을 삭제합니다.",
			tags = {"팀 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			parameters = {
					@Parameter(name = "teamId", description = "삭제할 팀의 id")
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 팀 삭제에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> deleteTeam(@PathVariable(name = "teamId") UUID teamId) {
		teamService.deleteTeam(teamId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}

	/*@PostMapping("/member/invite")
	@Operation (
			summary = "팀원 초대",
    		description = "새로운 팀원을 초대합니다.",
			tags = {"팀 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 유저에게 초대 요청을 보냅니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 유저 초대에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> inviteUser(@RequestBody TeamUserRequestDto request) {
		teamService.inviteUser(request);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}*/

	@PostMapping("/join")
	@Operation (
			summary = "팀원 가입",
    		description = "특정 유저를 팀에 추가합니다.",
			tags = {"팀 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 유저에게 초대 요청을 보냅니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 유저 초대에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> addUser(@RequestBody TeamUserRequestDto request) {
		teamService.joinTeamByInviteCode(request);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}

	@DeleteMapping("/member/{teamId}/{userId}")
	@Operation (
			summary = "팀원 삭제",
    		description = "팀에서 특정 유저를 삭제합니다.",
			tags = {"팀 관리"},
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			parameters = {
					@Parameter(name = "teamId", description = "팀원이 제거될 팀의 id"),
					@Parameter(name = "userId", description = "제거될 유저의 id")
			},
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 팀 삭제에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> deleteTeam(@PathVariable(name = "teamId") UUID teamId, @PathVariable(name = "userId") UUID userId) {
		teamService.leaveTeam(teamId, userId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}
}
