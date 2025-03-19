package com.se.Tlog.domain.Search.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Search.application.SearchService;
import com.se.Tlog.domain.Search.controller.dto.SearchResultDto;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
	private final SearchService searchService;
	
	@GetMapping("/destination")
	@Operation (
			summary = "여행지 이름, 주소 자동완성 검색",
    		description = "여행지 이름, 주소에 대한 자동완성 검색 결과를 반환합니다.",
			tags = {"검색"},
			parameters = { @Parameter(name = "searchText", description = "자동 완성을 요청할 미완성 문구입니다.") },
			security = @SecurityRequirement(
					name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
					scopes = {"scope1", "scope2"}),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공, 검색된 결과를 리스트로 반환합니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<SearchResultDto>>> searchDestinationUsingAutoComplete(@RequestParam String searchText) {
		List<SearchResultDto> result = searchService.autoComplete(searchText)
				.stream().map((entity) -> new SearchResultDto(entity.getId(), entity.getName(), entity.getAddress()))
				.toList();
		return ResponseEntity.ok(SuccessRes.from(result));
	}
}
