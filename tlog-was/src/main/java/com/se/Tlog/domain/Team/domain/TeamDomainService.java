package com.se.Tlog.domain.Team.domain;

import org.springframework.stereotype.Service;

import com.se.Tlog.domain.Team.domain.repository.TeamRepositoryService;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.domain.Wishlist.domain.WishlistService;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistOwnerDto;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j

@Service
@RequiredArgsConstructor
public class TeamDomainService {
	private final TeamRepositoryService repoService;
	private final WishlistService wishlistService;
	
	public void initializeTeam(Team rawTeam, User teamLeader) {
	    rawTeam.addUser(teamLeader, repoService);
	    rawTeam.setLeader(teamLeader, repoService);
	}
	
	public void deleteTeamData(Team team) {
		// 팀 삭제시 각종 처리...
		wishlistService.deleteWishlist(new WishlistOwnerDto(OwnerType.TEAM, team.getId()));
		
		repoService.deleteTeamUsers(team.getId());
	}
	
	public void inviteUser(Team team, User invitedUser) {
		if (repoService.isExistInTeam(team.getId(), invitedUser.getId()))
			throw new CustomException(ErrorType.ALREADY_EXIST_IN_TEAM);
		
		// 알람 처리
		log.info("팀원을 팀 " + team.getName() + "에 초대합니다. : " + invitedUser.getName());
	}
}
