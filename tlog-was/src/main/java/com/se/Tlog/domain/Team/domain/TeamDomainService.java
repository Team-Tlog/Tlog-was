package com.se.Tlog.domain.Team.domain;

import org.springframework.stereotype.Service;

import com.se.Tlog.domain.Team.domain.repository.TeamRepositoryService;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
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
    private final TeamRepository teamRepository;
	private final TeamRepositoryService repoService;
	private final WishlistService wishlistService;
	
	public Team createTeam(String name, User leader) {
	    Team newTeam = Team.create(name, repoService.makeInviteCode());
	    teamRepository.save(newTeam);
	    
	    addUser(newTeam, leader);
	    setLeader(newTeam, leader);
	    return newTeam;
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
    
    public void addUser(Team team, User user) {
        if (repoService.isExistInTeam(team.getId(), user.getId()))
            throw new CustomException(ErrorType.ALREADY_EXIST_IN_TEAM);
        
        repoService.addUserToTeam(team.getId(), user.getId());
        // 기타 팀원 추가시 처리내용
        
        log.info("팀원을 팀 " + team.getName() + "에 추가합니다. : " + user.getName());
    }
    
    public void setLeader(Team team, User user) {
        if (!repoService.isExistInTeam(team.getId(), user.getId()))
            throw new CustomException(ErrorType.TEAM_USER_NOT_FOUND);
        
        repoService.setLeader(team.getId(), user.getId());
    }
    
    public void deleteUser(Team team, User user) {
        if (!repoService.isExistInTeam(team.getId(), user.getId()))
            throw new CustomException(ErrorType.TEAM_USER_NOT_FOUND);
        if (repoService.countMemberInTeam(team.getId()) == 1)
            throw new CustomException(ErrorType.TEAM_CANNOT_BE_ORPHAN);
        if (repoService.isLeader(team.getId(), user.getId()))
            throw new CustomException(ErrorType.CANNOT_REMOVE_TEAM_LEADER);
        
        repoService.deleteUserInTeam(team.getId(), user.getId());
        // 기타 팀원 삭제 후 처리내용
        
        log.info("팀원을 팀 " + team.getName() + "에서 제거합니다. : " + user.getName());
    }
}
