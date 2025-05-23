package com.se.Tlog.domain.Admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Admin.controller.dto.UnapprovedDestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationDto;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Travel.domain.UnapprovedDestination;
import com.se.Tlog.domain.Travel.domain.repository.DestinationRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.UnapprovedDestinationRepository;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Schema(description = "미검수 여행지를 추가하는 데이터타입입니다.")
record AddUnapprovedDestinationDto(
        @Schema(description = "생성한 사용자의 id")
        UUID submitter,
        
        @Schema(description = "여행지 데이터")
        DestinationDto destination,
        
        @Schema(description = "커스텀 태그")
        List<String> customTags) {
}

@Schema(description = "미검수 여행지 데이터를 가리키는 데이터타입입니다.")
record UnapprovedDestinationsDto(
        @Schema(description = "지칭할 데이터 고유 id 리스트")
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
    private final UnapprovedDestinationRepository unapprovedDestinationRepository;
    private final DestinationRepositoryService destinationRepositoryService;
    
    @PostMapping("/destinations")
    @Operation (
            summary = "[개발환경 전용] 검수되지 않은 여행지 추가",
            description = "[개발환경 전용] 여행지를 검수되지 않은 상태로 등록합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<UnapprovedDestinationDto>> addUnApprovedDestinations(@RequestBody AddUnapprovedDestinationDto request) {
        // DDD 구조와 관련해서, Destination의 생성 로직을 리팩토링할 필요가 있음.
        UnapprovedDestination addedDestination = unapprovedDestinationRepository.save(
                UnapprovedDestination.create(
                        request.submitter(),
                        Destination.create(
                                request.destination().getName(),
                                request.destination().getLocation(),
                                request.destination().getAddress(),
                                new ArrayList<>(),
                                request.destination().getCity(),
                                request.destination().getDistrict(),
                                request.destination().isHasParking(),
                                request.destination().isPetFriendly(),
                                request.destination().getDescription(),
                                request.destination().getImageUrl(),
                                destinationRepositoryService), // Admin에서 Destination 도메인의 repository를 접근해야만 하는 복잡성 개선 필요.
                        request.customTags()));
        
        return ResponseEntity.ok(SuccessRes.from(
                new UnapprovedDestinationDto(
                        addedDestination.getId(), 
                        addedDestination.getSubmitter(), 
                        DestinationSummaryRes.from(
                                addedDestination.getDestination(), 
                                addedDestination.getCustomTags().stream().map(TagCount::create).toList()))
        ));
    }
    
    @GetMapping("/destinations")
    @Operation (
            summary = "[개발환경 전용] 검수되지 않은 여행지 데이터 DB 직접 조회",
            description = "[개발환경 전용] 검수되지 않은 여행지에 대한 DB 데이터를 그대로 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<List<UnapprovedDestination>>> getUnApprovedDestinations() {
        return ResponseEntity.ok(SuccessRes.from(
                unapprovedDestinationRepository.findAll()));
    }

    @DeleteMapping("/destinations")
    @Operation (
            summary = "[개발환경 전용] 검수되지 않은 여행지 리스트에서 삭제",
            description = "[개발환경 전용] 검수되지 않은 여행지 리스트에서 특정 항목을 제거합니다.<br>DB에 존재하지 않는 항목은 처리되지 않으며, 처리되지 않은 ID 목록이 다시 반환됩니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. DB에 없어 처리되지 않은 ID가 반환됩니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<UnapprovedDestinationsDto>> deleteUnApprovedDestinations(@RequestBody UnapprovedDestinationsDto destinations) {
        UnapprovedDestinationsDto response = new UnapprovedDestinationsDto(new ArrayList<String>());
        for (String id : destinations.ids()) {
            if (!unapprovedDestinationRepository.existsById(id))
                response.ids().add(id);
            else 
                unapprovedDestinationRepository.deleteById(id);
        }
        return ResponseEntity.ok(SuccessRes.from(response));
    }
}
