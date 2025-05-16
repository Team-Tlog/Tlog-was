package com.se.Tlog.domain.Admin.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Admin.controller.dto.DestinationApproveReq;
import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import com.se.Tlog.global.util.redis.RedisUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@ApplicationService
@RequiredArgsConstructor
public class AdminService {
    private final RedisUtil redisUtil;
    private final DestinationService destinationService;
    
    public Page<DestinationSummaryRes> getUnApprovedDestinations(Pageable pageable) {
        return destinationService.getDestinationByIds(
                // 현재 여행지의 TaggingQueue는 아직 관리자가 검수하지 않은 여행지 리스트로도 활용중에 있습니다.
                redisUtil.getAllDestinationIdFromTaggingQueue(), 
                pageable);
    }
    
    public void approveDestination(DestinationApproveReq approveReqest) {
        redisUtil.removeDestinationIdFromTaggingQueue(approveReqest.destinationId())
            .orElseThrow(() -> new CustomException(ErrorType.ALREADY_APPROVED_DESTINATION));
        
        // 기타 destination 관련 검수 처리
        destinationService.addFixedTagsToDestination(
                approveReqest.destinationId(), 
                approveReqest.fixedTags());
    }
}
