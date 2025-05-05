package com.se.Tlog.domain.Team.repository;

import java.util.UUID;

import org.springframework.stereotype.Component;

import com.se.Tlog.domain.Team.domain.InviteCodeUtil;
import com.se.Tlog.domain.Team.domain.repository.TeamRepositoryService;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Team.repository.jpa.TeamUserRepository;
import com.se.Tlog.domain.Team.repository.jpa.entity.TeamUserJpaEntity;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class TeamRepositoryServiceImplement implements TeamRepositoryService {
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final TeamUserRepository teamUserRepository;
	
	@Override
	public boolean isExistInTeam(UUID teamId, UUID userId) {
		return teamUserRepository.existsByTeam_IdAndUser_Id(teamId, userId);
	}

	@Override
	public long countMemberInTeam(UUID teamId) {
		return teamUserRepository.countByTeam_Id(teamId);
	}

	@Override
	public void addUserToTeam(UUID teamId, UUID userId) {
		TeamUserJpaEntity teamUser = TeamUserJpaEntity.builder()
				.team(teamRepository.getReferenceById(teamId))
				.user(userRepository.getReferenceById(userId))
				.build();
		teamUserRepository.save(teamUser);
	}
	
	@Override
	public void setLeader(UUID teamId, UUID userId) {
	    TeamUserJpaEntity oldLeader = teamUserRepository.findByTeam_IdAndIsLeaderTrue(teamId).orElse(null);
        TeamUserJpaEntity newLeader = teamUserRepository.findByTeam_IdAndUser_Id(teamId, userId)
                .orElseThrow(() -> new CustomException(ErrorType.TEAM_USER_NOT_FOUND));
   
        if (oldLeader != null) {
            if (oldLeader.getUser().getId() == userId)
    	        return;
            oldLeader.setLeader(false);
            teamUserRepository.save(oldLeader);
	    }
        newLeader.setLeader(true);
        teamUserRepository.save(newLeader);
	}

	@Override
	public void deleteUserInTeam(UUID teamId, UUID userId) {
		teamUserRepository.deleteByTeam_IdAndUser_Id(teamId, userId);
	}

	@Override
	public void deleteTeamUsers(UUID teamId) {
		teamUserRepository.deleteByTeam_Id(teamId);
	}

	@Override
	public long makeInviteCode() {
		// 현재 로직은 팀 수가 code range의 max에 가까이 차게 되면 매우 비효율적.
		// (물론, 현재 max는 19,770,609,663으로, 적은건 아님)
		
		if ((teamRepository.count() * 0.75) > InviteCodeUtil.MAX_INVITE_CODE_COUNT)
			log.warn("팀 초대 코드값에 여분이 없습니다!");
		if (teamRepository.count() >= InviteCodeUtil.MAX_INVITE_CODE_COUNT)
			throw new CustomException(ErrorType.NO_MORE_SPACE_FOR_INVITE_CODE);
		
		// 랜덤 생성 5000회 시도
		final int MAX_TRY_COUNT = 5000;
		for (int tryRound = 0; tryRound < MAX_TRY_COUNT; tryRound++) {
			long code = InviteCodeUtil.makeInviteCode();
			if (!teamRepository.existsByInviteCode(code))
				return code;
		}
		
		// 랜덤 생성으로 너무 오래 걸릴 경우, 순차적으로 생성
		for (long val = 0; val < InviteCodeUtil.MAX_INVITE_CODE_COUNT; val++)
			if (!teamRepository.existsByInviteCode(val)) {
				return val;
			}
			
		// 코드를 생성할 여유 공간이 있는데 코드를 생성하지 못한 상황...
		throw new CustomException(ErrorType.INTERNAL_SERVER_ERROR);
	}
}
