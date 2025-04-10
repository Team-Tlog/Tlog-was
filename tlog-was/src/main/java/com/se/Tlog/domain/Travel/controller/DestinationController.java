package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/destinations")
@RequiredArgsConstructor
public class DestinationController {
    private final DestinationService destinationService;

    @PostMapping
    @Operation(
            summary = "새로운 여행지 생성하기",
            description = "새로운 여행지를 생성합니다.",
            tags = {"Travel 도메인"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            responses = {
                    @ApiResponse( responseCode = "201", description = "새로운 여행지 등록을 성공하였습니다."),
                    @ApiResponse( responseCode = "409", description = "이미 존재하는 여행지입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<?> createDestination(@RequestBody DestinationDto destinationDto) {
        destinationService.createDestination(destinationDto);
        return ResponseEntity
                .status(SuccessType.DESTINATION_CREATED.getStatus())
                .body(SuccessRes.from(SuccessType.DESTINATION_CREATED));
    }

    @GetMapping
    @Operation(
            summary = "전체 여행지 리스트 조회",
            description = "전체 여행지 정보를 페이지네이션하여 반환합니다.",
            tags = {"Travel 도메인"},
            security = @SecurityRequirement(name = "JwtAuthScheme"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<Page<DestinationRes>> getAllDestinations(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(destinationService.getAllDestinations(pageable));
    }

    @GetMapping("/{id}")
    @Operation (
            summary = "특정 여행지 GET",
            description = "특정 id 여행지를 얻습니다.",
            tags = {"Travel 도메인"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            parameters = {@Parameter(name = "id", description = "특정 destinaion의 UUID 입니다.")},
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse( responseCode = "400", description = "존재하지 않는 여행지입니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<DestinationRes> getDestinationById(
            @Parameter(description = "특정 destination의 UUID 입니다.") @PathVariable String id) {
        return ResponseEntity.ok(destinationService.getDestinationById(id));
    }
}
