package com.se.Tlog.domain.Admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.se.Tlog.domain.Admin.application.AdminService;
import com.se.Tlog.domain.Admin.controller.dto.DestinationApproveReq;
import com.se.Tlog.domain.Admin.controller.dto.UnapprovedDestinationDto;
import com.se.Tlog.global.response.error.ErrorRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "관리자 작업")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class AdminController {
    private final AdminService adminService;
    
    @GetMapping("/destinations")
    @Operation (
            summary = "검수되지 않은 여행지 조회",
            description = "검수되지 않은 모든 여행지를 조회합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 검수되지 않은 여행지가 반환됩니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<Page<UnapprovedDestinationDto>>> getUnApprovedDestinations(
            @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(SuccessRes.from(
                adminService.getUnApprovedDestinations(pageable)));
    }
    
    @PostMapping("/destinations")
    @Operation (
            summary = "여행지 검수하기",
            description = "특정 여행지를 검수합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "처리 성공. 별도로 반환되는 정보는 없습니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류.",
                            content = @Content(schema = @Schema(implementation = ErrorRes.class)))}
    )
    public ResponseEntity<SuccessRes<?>> approvedDestination(
            @RequestBody DestinationApproveReq approveReqest) {
        adminService.approveDestination(approveReqest);
        return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
    }
}
