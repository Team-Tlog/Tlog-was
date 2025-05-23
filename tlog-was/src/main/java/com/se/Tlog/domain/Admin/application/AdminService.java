package com.se.Tlog.domain.Admin.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Admin.controller.dto.DestinationApproveReq;
import com.se.Tlog.domain.Admin.controller.dto.UnapprovedDestinationDto;
import com.se.Tlog.domain.Travel.application.DestinationService;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSummaryRes;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Travel.repository.mongo.UnapprovedDestinationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@ApplicationService
@RequiredArgsConstructor
public class AdminService {
    private final UnapprovedDestinationRepository unapprovedDestinationRepository;
    private final DestinationService destinationService;
    
    public Page<UnapprovedDestinationDto> getUnApprovedDestinations(Pageable pageable) {
        return unapprovedDestinationRepository.findAll(pageable)
                .map(dest -> 
                        new UnapprovedDestinationDto(
                                dest.getId(),
                                dest.getSubmitter(),
                                DestinationSummaryRes.from(
                                        dest.getDestination(), 
                                        dest.getCustomTags().stream().map(TagCount::create).toList())
                        )
                );
    }
    
    public void approveDestination(DestinationApproveReq approveReqest) {
        // DDD 구조 개선점 : 
        //   도메인 간의 결합을 낮추기 위해, Spring Event를 도입하여 해결할 수 있습니다.
        //   오류 발생시 중단/피드백이 필요하다면 동기 방식의 이벤트로 설정할 수 있습니다.
        destinationService.assignDestination(
                approveReqest.id(), 
                approveReqest.fixedTags());
    }
}
