package com.se.Tlog.domain.Reward.aspect;

import com.se.Tlog.domain.Reward.application.RewardService;
import com.se.Tlog.domain.Team.controller.dto.CreateTeamRequestDto;
import com.se.Tlog.domain.Team.controller.dto.TeamUserRequestDto;
import com.se.Tlog.domain.User.controller.dto.TokenDto;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.domain.Wishlist.domain.UpdateType;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.util.jwt.AccessTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class RewardCatchAspect {
    private final RewardService rewardService;
    private final AccessTokenProvider accessTokenProvider;

    private void addRewardToUser(UUID userId, Long rewardId) {
        try {
            rewardService.addRewardToUser(userId, rewardId);
        } catch (CustomException e) {
            // 이미 보상을 가지고 있거나, 사용자 id가 잘못되었습니다.
            log.error("보상 지급이 무시되었습니다. {}", e);
        }
    }

    @AfterReturning(value = "execution(* *..SsoAuthService.register(..))", returning = "tokenDto")
    public void registerdUserReward(TokenDto tokenDto) {
        UUID userId = UUID.fromString(accessTokenProvider.parseToken(tokenDto.accessToken()).getSubject());
        addRewardToUser(userId, 1L);
    }

    @Around(value = "execution(* *..TeamService.createTeam(..))")
    public Object createTeam(ProceedingJoinPoint joinPoint) throws Throwable {
        CreateTeamRequestDto createRequest = (CreateTeamRequestDto)joinPoint.getArgs()[0];
        Object returnValue = joinPoint.proceed();
        addRewardToUser(createRequest.creator(), 2L);
        return returnValue;
    }

    @Around(value = "execution(* *..TeamService.joinTeamByInviteCode(..))")
    public Object joinToTeam(ProceedingJoinPoint joinPoint) throws Throwable {
        TeamUserRequestDto joinRequest = (TeamUserRequestDto)joinPoint.getArgs()[0];
        Object returnValue = joinPoint.proceed();
        addRewardToUser(joinRequest.userId(), 2L);
        return returnValue;
    }

    @AfterReturning("execution(* *..WishlistService.updateWishlist(..)) && args(updateType, owner, *, *)")
    public void scrappedDestination(UpdateType updateType, WishlistOwnerDto owner) {
        if (updateType == UpdateType.ADD && owner.ownerType() == OwnerType.USER)
            addRewardToUser(owner.ownerId(), 3L);
    }
}
