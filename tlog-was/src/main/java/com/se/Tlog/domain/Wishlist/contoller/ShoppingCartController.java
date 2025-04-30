package com.se.Tlog.domain.Wishlist.contoller;

import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.Travel.controller.dto.SimpleDestinationRes;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Wishlist.application.ShoppingCartService;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
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
@RequestMapping("/api/shopcart")
@RequiredArgsConstructor
@Tag(name = "여행지 장바구니(필수 포함 여행지)")
@SecurityRequirement(
		name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
		scopes = {"scope1", "scope2"} )
public class ShoppingCartController {
	private final ShoppingCartService shoppingCartService;
	
	@GetMapping("/user/{userId}")
	@Operation (
			summary = "유저 장바구니 조회",
    		description = "특정 유저의 장바구니를 조회합니다.",
			parameters = { @Parameter(name = "userId", description = "조회할 유저의 Id") },
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 유저의 장바구니 데이터가 반환됩니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<SimpleDestinationRes>>> getCartOfUser(@PathVariable(name = "userId") UUID userId) {
		return ResponseEntity.ok(SuccessRes.from(
				shoppingCartService.getCartData(userId, OwnerType.USER)));
	}
	
	@PutMapping("/user/{userId}")
	@Operation (
			summary = "유저 장바구니에 여행지 추가",
    		description = "특정 사용자의 장바구니에 여행지를 추가합니다.",
			parameters = { @Parameter(name = "userId", description = "여행지를 장바구니에 추가할 유저의 Id") },
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가할 여행지의 id<br/>쌍따옴표 불필요!",
				content = { @Content(examples = @ExampleObject(value = "여행지 ID, 쌍따옴표 필요 없어요")) }),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 여행지가 장바구니에 추가되었습니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> addToCartOfUser(@PathVariable(name = "userId") UUID userId, @RequestBody String destinationId) {
		shoppingCartService.addToCart(userId, OwnerType.USER, destinationId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}
	
	@DeleteMapping("/user/{userId}/destination/{destId}")
	@Operation (
			summary = "유저 장바구니에서 여행지 삭제",
    		description = "특정 사용자의 장바구니에서 여행지를 삭제합니다.",
			parameters = { 
					@Parameter(name = "userId", description = "장바구니에서 여행지를 삭제할 유저의 Id"),
					@Parameter(name = "destId", description = "장바구니에서 삭제할 여행지의 Id")},
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 여행지가 장바구니에서 제거되었습니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> deleteFromCartOfUser(@PathVariable(name = "userId") UUID userId, @PathVariable(name = "destId") String destinationId) {
		shoppingCartService.deleteFromCart(userId, OwnerType.USER, destinationId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}
	
	

	@GetMapping("/team/{teamId}")
	@Operation (
			summary = "팀 장바구니 조회",
    		description = "특정 팀의 장바구니를 조회합니다.",
			parameters = { @Parameter(name = "teamId", description = "조회할 팀의 Id") },
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 팀의 장바구니 데이터가 반환됩니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<List<SimpleDestinationRes>>> getCartOfTeam(@PathVariable(name = "teamId") UUID teamId) {
		return ResponseEntity.ok(SuccessRes.from(
				shoppingCartService.getCartData(teamId, OwnerType.TEAM)));
	}
	
	@PutMapping("/team/{teamId}")
	@Operation (
			summary = "팀 장바구니에 여행지 추가",
    		description = "특정 팀의 장바구니에 여행지를 추가합니다.",
			parameters = { @Parameter(name = "teamId", description = "여행지를 장바구니에 추가할 팀의 Id") },
			requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "추가할 여행지의 id<br/>쌍따옴표 불필요!",
				content = { @Content(examples = @ExampleObject(value = "여행지 ID, 쌍따옴표 필요 없어요")) }),
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 여행지가 장바구니에 추가되었습니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> addCartOfTeam(@PathVariable(name = "teamId") UUID teamId, @RequestBody String destinationId) {
		shoppingCartService.addToCart(teamId, OwnerType.TEAM, destinationId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}
	
	@DeleteMapping("/team/{teamId}/destination/{destId}")
	@Operation (
			summary = "팀 장바구니에서 여행지 삭제",
    		description = "특정 팀의 장바구니에서 여행지를 삭제합니다.",
			parameters = { 
					@Parameter(name = "teamId", description = "장바구니에서 여행지를 삭제할 팀의 Id"),
					@Parameter(name = "destId", description = "장바구니에서 삭제할 여행지의 Id")},
			responses = {
					@ApiResponse(responseCode = "200", description = "처리 성공. 해당 여행지가 장바구니에서 제거되었습니다."),
					@ApiResponse(responseCode = "500", description = "서버 내부 오류.",
							content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
	)
	public ResponseEntity<SuccessRes<?>> deleteFromCartOfTeam(@PathVariable(name = "teamId") UUID teamId, @PathVariable(name = "destId") String destinationId) {
		shoppingCartService.deleteFromCart(teamId, OwnerType.TEAM, destinationId);
		return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
	}
}
