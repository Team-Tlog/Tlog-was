package com.se.Tlog.domain.Admin.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import com.se.Tlog.global.util.redis.RedisUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Schema(description = "여행지들의 id를 전달하는 데이터타입입니다.")
record DestinationsDto(
        @Schema(description = "여행지 id 리스트")
        List<String> ids) {
}

@Controller
@Profile("dev")
@RequestMapping("/api/test/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 작업")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class DevAdminController {
    private final RedisUtil redisUtil;
    
    @PostMapping("/destinations")
    @Operation (
            summary = "[개발환경 전용] 검수되지 않은 여행지 추가",
            description = "[개발환경 전용] 여행지를 검수되지 않은 상태로 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<?>> addUnApprovedDestinations(@RequestBody DestinationsDto destinations) {
        for (String id : destinations.ids())
            redisUtil.pushDestinationIdToTaggingQueue(id);
        return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
    }
    
    @GetMapping("/destinations")
    @Operation (
            summary = "[개발환경 전용] 검수되지 않은 여행지 ID의 DB 직접 조회",
            description = "[개발환경 전용] 검수되지 않은 여행지에 대한 DB 데이터를 그대로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<String>>> getUnApprovedDestinations() {
        return ResponseEntity.ok(SuccessRes.from(
                redisUtil.getAllDestinationIdFromTaggingQueue()));
    }

    @DeleteMapping("/destinations")
    @Operation (
            summary = "[개발환경 전용] 검수되지 않은 여행지 리스트에서 삭제",
            description = "[개발환경 전용] 검수되지 않은 여행지 리스트에서 특정 id를 제거합니다.<br>DB에 없어 처리되지 않은 ID가 반환됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. DB에 없어 처리되지 않은 ID가 반환됩니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<DestinationsDto>> deleteUnApprovedDestinations(@RequestBody DestinationsDto destinations) {
        DestinationsDto response = new DestinationsDto(new ArrayList<String>());
        for (String id : destinations.ids())
            if (!redisUtil.removeDestinationIdFromTaggingQueue(id).isPresent())
                response.ids().add(id);
        return ResponseEntity.ok(SuccessRes.from(response));
    }
}
