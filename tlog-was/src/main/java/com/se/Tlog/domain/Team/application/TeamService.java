package com.se.Tlog.domain.Team.application;

import java.util.List;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Team.controller.dto.CreateTeamRequestDto;
import com.se.Tlog.domain.Team.controller.dto.TeamResponseDto;
import com.se.Tlog.domain.Team.controller.dto.TeamUserRequestDto;
import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.Team.domain.TeamDomainService;
import com.se.Tlog.domain.Team.domain.repository.TeamRepositoryService;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Team.repository.jpa.TeamUserRepository;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
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
	
	public UUID createTeam(CreateTeamRequestDto request) {
		if (request.name() == null)
			throw new CustomException(ErrorType.TEAM_NAME_NOT_FOUND);
		if (!userRepository.existsById(request.creator()))
			throw new CustomException(ErrorType.USER_NOT_FOUND);

		Team newTeam = teamRepository.save(
				Team.create(
						request.name(),
						repoService.makeInviteCode()));
		newTeam.addUser(userRepository.findById(request.creator()).get(), repoService);
		return newTeam.getId();
	}
	
	public List<TeamResponseDto> getTeamOfUser(UUID userId) {
		if (!userRepository.existsById(userId))
			throw new CustomException(ErrorType.USER_NOT_FOUND);

		return teamUserRepository.findByUser_Id(userId)
				.stream().map(teamUser -> {
					List<UUID> members = teamUserRepository.findByTeam_Id(teamUser.getTeam().getId())
							.stream().map(teamUserByTeam -> teamUserByTeam.getUser().getId()).toList();
					return new TeamResponseDto(
							teamUser.getTeam().getId(),
							teamUser.getTeam().getName(),
							members);
					})
				.toList();
	}
	
	public void deleteTeam(UUID teamId) {
		if (!teamRepository.existsById(teamId))
			throw new CustomException(ErrorType.TEAM_NOT_FOUND);
		
		teamDomainService.deleteTeamData(teamRepository.findById(teamId).get());
		teamRepository.deleteById(teamId);
	}
	
	public void inviteUser(TeamUserRequestDto request) {
		if (!teamRepository.existsById(request.teamId()))
			throw new CustomException(ErrorType.TEAM_NOT_FOUND);
		if (!userRepository.existsById(request.userId()))
			throw new CustomException(ErrorType.USER_NOT_FOUND);

		Team team = teamRepository.findById(request.teamId()).get();
		User user = userRepository.findById(request.userId()).get();
		teamDomainService.inviteUser(team, user);
	}
	
	public void joinTeam(TeamUserRequestDto request) {
		if (!teamRepository.existsById(request.teamId()))
			throw new CustomException(ErrorType.TEAM_NOT_FOUND);
		if (!userRepository.existsById(request.userId()))
			throw new CustomException(ErrorType.USER_NOT_FOUND);
		
		Team team = teamRepository.findById(request.teamId()).get();
		User user = userRepository.findById(request.userId()).get();
		team.addUser(user, repoService);
	}
	
	public void leaveTeam(UUID teamId, UUID userId) {
		if (!teamRepository.existsById(teamId))
			throw new CustomException(ErrorType.TEAM_NOT_FOUND);
		if (!userRepository.existsById(userId))
			throw new CustomException(ErrorType.USER_NOT_FOUND);
		
		Team team = teamRepository.findById(teamId).get();
		User user = userRepository.findById(userId).get();
		team.deleteUser(user, repoService);
	}
}
