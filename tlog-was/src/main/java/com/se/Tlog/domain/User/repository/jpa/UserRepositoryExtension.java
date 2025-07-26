package com.se.Tlog.domain.User.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Tbti.domain.Tbti;
import com.se.Tlog.domain.User.repository.dto.UserTbtiProjection;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRepositoryExtension {
    private final UserRepository userRepository;
    
    public List<UUID> findAllIdsByRelativeTbti(UUID userId) {
        /* 2025.7.26
         * 성능 이슈 : 
         *     모든 사용자를 전체 조회한 후에 TBTI가 유사한지를 비교해 추출합니다.
         *     메소드가 사용될때마다 전체 사용자를 조회하는 문제가 있습니다.
         * 참고사항 :
         *     TBTI가 유사한지를 판단하는 기준은 다음과 같습니다.
         *     - TBTI의 특징이 2개 이상 동일할 것.
         */
        UserTbtiProjection user = userRepository.findOfTbtiById(userId);
        List<UserTbtiProjection> users = userRepository.findAllOfIdAndTbti();
        
        return users.stream()
                .filter(u -> (u.getUserId() != user.getUserId()
                        && 2 <= Tbti.getSameFeatureCount(u.getTbtiCode(), user.getTbtiCode())))
                .map(u -> u.getUserId())
                .toList();
    }
}
