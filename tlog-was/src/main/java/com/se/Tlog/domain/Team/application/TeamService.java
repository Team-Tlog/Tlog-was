package com.se.Tlog.domain.Team.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Team.controller.dto.*;
import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.Team.domain.TeamDomainService;
import com.se.Tlog.domain.Team.domain.repository.TeamRepositoryService;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Team.repository.jpa.TeamUserRepository;
import com.se.Tlog.domain.Team.repository.jpa.entity.TeamUserJpaEntity;
import com.se.Tlog.domain.Travel.controller.dto.SimpleDestinationRes;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.Wishlist.application.ShoppingCartService;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;

@ApplicationService
@RequiredArgsConstructor
public class TeamService {
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final TeamUserRepository teamUserRepository;
	
	private final TeamDomainService teamDomainService;
	private final TeamRepositoryService repoService;

	private final ShoppingCartService shoppingCartService;
	
	public UUID createTeam(CreateTeamRequestDto request) {
		if (request.name() == null)
			throw new CustomException(ErrorType.TEAM_NAME_NOT_FOUND);
		if (!userRepository.existsById(request.creator()))
			throw new CustomException(ErrorType.USER_NOT_FOUND);

		Team newTeam = Team.create(request.name(), repoService.makeInviteCode());
		teamRepository.save(newTeam);
		
		// Team의 id가 생성된 후에야 초기화 작업이 가능함.
		// 그러나 Team.create 후에 별도로 다시 initialize를 해야 하는 구조는 적절하지 않음!
		// (초기화를 까먹는 휴먼 에러에 취약한 점 등)
		teamDomainService.initializeTeam(
		        newTeam, 
		        userRepository.findById(request.creator()).get());
		return newTeam.getId();
	}
	
	public List<TeamResponseDto> getTeamOfUser(UUID userId) {
		if (!userRepository.existsById(userId))
			throw new CustomException(ErrorType.USER_NOT_FOUND);

		return teamUserRepository.findByUser_Id(userId)
				.stream().map(teamUser -> {
				    UUID teamLeader = null;
				    List<UUID> members = new ArrayList<UUID>();
				    for (TeamUserJpaEntity teamUserInTeam : teamUserRepository.findWithUserByTeamId(teamUser.getTeam().getId())) {
				        if (teamUserInTeam.isLeader())
                            teamLeader = teamUserInTeam.getUser().getId();
                        members.add(teamUserInTeam.getUser().getId());
				    }
					return TeamResponseDto.from(teamUser.getTeam(), teamLeader, members);
				})
				.toList();
	}
	
	public void deleteTeam(UUID teamId) {
		if (!teamRepository.existsById(teamId))
			throw new CustomException(ErrorType.TEAM_NOT_FOUND);
		
		teamDomainService.deleteTeamData(teamRepository.findById(teamId).get());
		teamRepository.deleteById(teamId);
	}
	
	/*public void inviteUser(TeamUserRequestDto request) {

		if (!teamRepository.existsById(request.teamId()))
			throw new CustomException(ErrorType.TEAM_NOT_FOUND);
		if (!userRepository.existsById(request.userId()))
			throw new CustomException(ErrorType.USER_NOT_FOUND);

		Team team = teamRepository.findById(request.teamId()).get();
		User user = userRepository.findById(request.userId()).get();
		teamDomainService.inviteUser(team, user);
	}*/
	
	public void joinTeamByInviteCode(TeamUserRequestDto request) {
		Team team = teamRepository.findByInviteCode(request.inviteCode())
				.orElseThrow(() -> new CustomException(ErrorType.TEAM_NOT_FOUND));
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

		team.addUser(user, repoService);
	}
	
	public void leaveTeam(UUID teamId, UUID userId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new CustomException(ErrorType.TEAM_NOT_FOUND));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

		team.deleteUser(user, repoService);
	}

	public TeamDetailDto getTeamDetails(UUID teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new CustomException(ErrorType.TEAM_NOT_FOUND));
		List<TeamMemberDto> memberDtoList = teamUserRepository.findWithUserByTeamId(team.getId())
				.stream().map(teamUser -> 
				        TeamMemberDto.from(teamUser.getUser(), teamUser.isLeader())
		        ).toList();

		List<SimpleDestinationRes> wishList = shoppingCartService.getCartData(team.getId(), OwnerType.TEAM);

		return TeamDetailDto.from(team, memberDtoList, wishList);
	}
}
