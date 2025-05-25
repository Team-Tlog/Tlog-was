package com.se.Tlog.domain.Search.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.se.Tlog.domain.Search.application.SearchService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/search")
@RequiredArgsConstructor
@Tag(name = "검색")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class SearchController {
	private final SearchService searchService;
	
	@GetMapping("/destination/by-name")
	@Operation (
			summary = "여행지 이름 자동완성 검색",
    		description = "여행지 이름에 대한 자동완성 검색 결과를 반환합니다.",
			parameters = { @Parameter(name = "name", description = "자동 완성을 요청할 미완성 문구입니다.") },
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공, 검색된 결과를 리스트로 반환합니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<DestinationSummaryRes>>> searchDestinationByName(@RequestParam String name) {
		return ResponseEntity.ok(SuccessRes.from(
		        searchService.autoCompleteDestinationByName(name)));
	}
	
	@GetMapping("/destination/by-address")
    @Operation (
            summary = "여행지 주소 자동완성 검색",
            description = "여행지 주소에 대한 자동완성 검색 결과를 반환합니다. (현재 결과에 address가 직접 표시되지는 않음)",
            parameters = { @Parameter(name = "address", description = "자동 완성을 요청할 미완성 문구입니다.") },
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공, 검색된 결과를 리스트로 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<DestinationSummaryRes>>> searchDestinationByAddress(@RequestParam String address) {
	    return ResponseEntity.ok(SuccessRes.from(
                searchService.autoCompleteDestinationByAddress(address)));
    }
	
	@GetMapping("/destination/by-city")
    @Operation (
            summary = "여행지 도시 필터링 검색",
            description = "도시 필터링한 여행지 검색 결과를 반환합니다.",
            parameters = { 
                    @Parameter(name = "pageable", description = "페이징 정보"),
                    @Parameter(name = "city", description = "도시 이름")},
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공, 검색된 결과를 리스트로 반환합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류. 조회에 실패했습니다.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<Page<DestinationSummaryRes>>> searchDestinationByAddress(@PageableDefault Pageable pageable, @RequestParam String city) {
        return ResponseEntity.ok(SuccessRes.from(
                searchService.searchDestinationByCity(pageable, city)));
    }
}
