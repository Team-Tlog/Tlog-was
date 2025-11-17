package com.se.Tlog.domain.Travel.controller;

import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDetailsRes;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.domain.Travel.domain.DestinationSortType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;
import com.se.Tlog.global.security.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        destinationService.generateNewDestination(destinationDto);
        return ResponseEntity
                .status(SuccessType.DESTINATION_CREATED.getStatus())
                .body(SuccessRes.from(SuccessType.DESTINATION_CREATED));
    }

    @GetMapping
    @Operation(
            summary = "전체 여행지 리스트 조회 (인증 토큰 필요)",
            description = "전체 여행지 정보를 페이지네이션하여 반환합니다."
                        + "<br>RECOMMEND 정렬을 사용할 경우, city에 <b>지역코드</b>가 입력되어야 합니다!"
                        + "<br><br><b>인증 토큰이 필요합니다!</b>",
            tags = {"Travel 도메인"},
            security = @SecurityRequirement(name = "JwtAuthScheme"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<Slice<DestinationSummaryRes>>> getAllDestinations(
            @AuthenticationPrincipal CustomUserDetails user,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam String city,
            @RequestParam(defaultValue = "RECOMMAND") DestinationSortType sortType) {
        if (user == null)
            throw new CustomException(ErrorType.UN_AUTHENTICATION);

        return ResponseEntity
                .status(SuccessType.DESTINATION_GET_SUCCESS.getStatus())
                .body(SuccessRes.from(destinationService.getAllDestinations(pageable,city,sortType, UUID.fromString(user.getId()))));
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
    public ResponseEntity<SuccessRes<DestinationDetailsRes>> getDestinationById(
            @Parameter(description = "특정 destination의 UUID 입니다.") @PathVariable String id) {
        return ResponseEntity
                .status(SuccessType.DESTINATION_GET_SUCCESS.getStatus())
                .body(SuccessRes.from(destinationService.getDestinationById(id)));
    }

    /*
     * 25.9.23 이 API는 사용자 성향 파악용 정보의 표시 방식이 변경됨에 따라 삭제되었습니다.
    @GetMapping("/of-each-tag")
    @Operation(
            summary = "사용자 성향 파악용 여행지 조회",
            description = "사용자 성향을 파악하기 위한 여행지 미리보기를 불러옵니다."
                        + "<br> 최대 10개까지의 태그가 제시되며,"
                        + "<br> 각 태그에는 하나의 여행지가 제시됩니다.",
            tags = {"Travel 도메인"},
            security = @SecurityRequirement(
                    name = "JwtAuthScheme",
                    scopes = {"scope1", "scope2"}),
            parameters = {@Parameter(name = "tbtiCode", description = "사용자의 TBTI 코드입니다.")},
            responses = {
                    @ApiResponse( responseCode = "200", description = "성공"),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")
            }
    )
    public ResponseEntity<SuccessRes<List<DestinationByTagDto>>> getDestinationOfEachTag(@RequestParam int tbtiCode) {
        return ResponseEntity.ok(SuccessRes.from(
                destinationService.getDestinationOfEachTag(tbtiCode)));
    }
     */
}
