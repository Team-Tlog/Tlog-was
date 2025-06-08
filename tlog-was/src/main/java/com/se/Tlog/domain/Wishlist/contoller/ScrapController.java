package com.se.Tlog.domain.Wishlist.contoller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Wishlist.application.ScrapService;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistDestinationRes;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/scrap")
@RequiredArgsConstructor
@Tag(name = "여행지 스크랩(찜목록)")
@SecurityRequirement(
		name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
		scopes = {"scope1", "scope2"})
public class ScrapController {
	private final ScrapService scrapService;
	
	@GetMapping("/user/{userId}")
	@Operation (
			summary = "스크랩 된 여행지 조회",
    		description = "특정 사용자의 스크랩 목록을 조회합니다.",
			parameters = { @Parameter(name = "userId", description = "조회할 유저의 Id") },
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 유저의 스크랩한 여행지 데이터가 반환됩니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<WishlistDestinationRes>>> getScrapList(@PathVariable(name = "userId") UUID userId) {
		return ResponseEntity.ok(SuccessRes.from(
				scrapService.getScrapData(userId)));
	}

	@PutMapping("/user/{userId}")
	@Operation (
			summary = "여행지 스크랩 추가",
    		description = "특정 사용자의 스크랩 목록에 여행지를 추가합니다.",
			parameters = { @Parameter(name = "userId", description = "스크랩을 추가할 유저의 Id") },
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "스크랩 할 여행지의 id<br/>쌍따옴표 불필요!",
				content = { @Content(examples = @ExampleObject(value = "여행지 ID, 쌍따옴표 필요 없어요")) }),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 여행지가 스크랩 목록에 추가되었습니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> addScrap(@PathVariable(name = "userId") UUID userId, @RequestBody String destinationId) {
		scrapService.scrapDestination(userId, destinationId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}

	@DeleteMapping("/user/{userId}/destination/{destId}")
	@Operation (
			summary = "여행지 스크랩 삭제",
    		description = "특정 사용자의 스크랩 목록에서 여행지를 삭제합니다.",
			parameters = { 
					@Parameter(name = "userId", description = "스크랩을 삭제할 유저의 Id"),
					@Parameter(name = "destId", description = "스크랩 취소할 여행지의 Id")},
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 여행지가 스크랩 목록에서 제거되었습니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> deleteScrap(@PathVariable(name = "userId") UUID userId, @PathVariable(name = "destId") String destinationId) {
		scrapService.unscrapDescription(userId, destinationId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}
}
