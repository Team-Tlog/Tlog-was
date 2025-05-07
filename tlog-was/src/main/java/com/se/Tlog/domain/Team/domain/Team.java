package com.se.Tlog.domain.Team.domain;

import java.time.LocalDateTime;
import java.util.UUID;

import com.se.Tlog.domain.Team.domain.repository.TeamRepositoryService;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Slf4j

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Team {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private Long inviteCode;
	
	private String name;
	
	// private Tbti tbti;

	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	private Team(String name, Long inviteCode) {
		this.name = name;
		this.inviteCode = inviteCode;
	}
	
	public static Team create(
			String name, 
			Long inviteCode) {
		if (name == null || name.isBlank())
			throw new CustomException(ErrorType.TEAM_NAME_NOT_FOUND);
		if (!InviteCodeUtil.isValidCode(inviteCode))
			throw new CustomException(ErrorType.INTERNAL_ERROR_BY_INVITE_CODE);
		
		Team newTeam = new Team(name, inviteCode);
		return newTeam;
	}
	
	public void addUser(User user, TeamRepositoryService repoService) {
		if (repoService.isExistInTeam(this.getId(), user.getId()))
			throw new CustomException(ErrorType.ALREADY_EXIST_IN_TEAM);
		
		repoService.addUserToTeam(this.id, user.getId());
		// 기타 팀원 추가시 처리내용
		
		log.info("팀원을 팀 " + this.name + "에 추가합니다. : " + user.getName());
	}
	
	public void setLeader(User user, TeamRepositoryService repoService) {
	    if (!repoService.isExistInTeam(this.id, user.getId()))
            throw new CustomException(ErrorType.TEAM_USER_NOT_FOUND);
	    
	    repoService.setLeader(this.id, user.getId());
	}
	
	public void deleteUser(User user, TeamRepositoryService repoService) {
		if (!repoService.isExistInTeam(this.id, user.getId()))
			throw new CustomException(ErrorType.TEAM_USER_NOT_FOUND);
		if (repoService.countMemberInTeam(id) == 1)
			throw new CustomException(ErrorType.TEAM_CANNOT_BE_ORPHAN);
		if (repoService.isLeader(id, user.getId()))
		    throw new CustomException(ErrorType.CANNOT_REMOVE_TEAM_LEADER);
		
		repoService.deleteUserInTeam(this.id, user.getId());
		// 기타 팀원 삭제 후 처리내용
		
		log.info("팀원을 팀 " + this.name + "에서 제거합니다. : " + user.getName());
	}
}
