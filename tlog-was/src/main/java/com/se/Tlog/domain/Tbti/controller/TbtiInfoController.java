package com.se.Tlog.domain.Tbti.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.se.Tlog.domain.Tbti.application.TbtiInfoService;
import com.se.Tlog.domain.Tbti.controller.dto.CreateTbtiInfoReq;
import com.se.Tlog.domain.Tbti.controller.dto.TbtiInfoRes;
import com.se.Tlog.global.response.success.SuccessRes;
import com.se.Tlog.global.response.success.SuccessType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tbti-info")
@RequiredArgsConstructor
@Tag(name = "TBTI별 설명 도메인")
@SecurityRequirement(
        name = "JwtAuthScheme", // OpenApiConfig에 설정된 Security Scheme 이름일 것
        scopes = {"scope1", "scope2"})
public class TbtiInfoController {
    private final TbtiInfoService tbtiInfoService;
    
    @PostMapping
    @Operation (
            summary = "TBTI 설명 생성하기",
            description = "새 TBTI 설명을 생성합니다.",
            responses = {
                    @ApiResponse( responseCode = "200", description = "처리 성공"), 
                    @ApiResponse(responseCode = "409", description = "이미 해당 TBTI의 설명이 존재합니다."),
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")}
    )
    public ResponseEntity<SuccessRes<?>> createTbtiDescription(@RequestBody CreateTbtiInfoReq request){
        tbtiInfoService.createTbtiInfo(request);
        return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
    }
    
    @GetMapping
    @Operation (
    		summary = "TBTI 설명 조회하기",
    		description = "TBTI 설명을 반환합니다."
    		            + "<br><b>설명 정보가 없을 경우, 기본 표시 정보가 반환됩니다.</b>",
    		parameters = {
    				@Parameter(name = "tbti", description = "설명을 조회할 TBTI 값입니다. (SELA ~ RONI)")
    		},
    		responses = {
    				@ApiResponse( responseCode = "200", description = "처리 성공시 반환되는 TBTI 설명 데이터입니다."), 
    				@ApiResponse(responseCode = "500", description = "서버 내부 오류")}
    )
    public ResponseEntity<SuccessRes<TbtiInfoRes>> getTbtiDescription(@RequestParam(name = "tbti", required = false) String tbti){
        return ResponseEntity.ok().body(SuccessRes.from(
                tbtiInfoService.getTbtiInfo(tbti)));
    }

    @DeleteMapping("/{tbti}")
    @Operation (
            summary = "TBTI 설명 삭제하기",
            description = "특정 TBTI의 설명을 삭제합니다.",
            responses = {
                    @ApiResponse( responseCode = "200", description = "처리 성공"), 
                    @ApiResponse(responseCode = "500", description = "서버 내부 오류")}
    )
    public ResponseEntity<SuccessRes<?>> createTbtiDescription(@PathVariable(name = "tbti") String tbti){
        tbtiInfoService.deleteTbtiInfo(tbti);
        return ResponseEntity.ok(SuccessRes.from(SuccessType.OK));
    }
}
