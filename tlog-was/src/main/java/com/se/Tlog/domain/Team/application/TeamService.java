package com.se.Tlog.domain.Team.application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Social.Chat.room.ChatRoomService;
import com.se.Tlog.domain.Team.controller.dto.*;
import com.se.Tlog.domain.Team.domain.InviteCodeUtil;
import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.Team.domain.TeamDomainService;
import com.se.Tlog.domain.Team.domain.repository.TeamRepositoryService;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Team.repository.jpa.TeamUserRepository;
import com.se.Tlog.domain.Team.repository.jpa.entity.TeamUserJpaEntity;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.Wishlist.application.ShoppingCartService;
import com.se.Tlog.domain.Wishlist.domain.OwnerType;
import com.se.Tlog.domain.Wishlist.domain.dto.WishlistDestinationRes;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@ApplicationService
@RequiredArgsConstructor
public class TeamService {
	private final UserRepository userRepository;
	private final TeamRepository teamRepository;
	private final TeamUserRepository teamUserRepository;
	
	private final TeamDomainService teamDomainService;
	private final TeamRepositoryService repoService;

	private final ShoppingCartService shoppingCartService;
	private final ChatRoomService chatRoomService;

	@Transactional
	public TeamCreateRes createTeam(CreateTeamRequestDto request) {
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
		Long chatRoomId = chatRoomService.create(request.creator(), newTeam.getId());
		return TeamCreateRes.of(newTeam.getId(),chatRoomId);
	}
	
	public List<TeamResponseDto> getTeamOfUser(UUID userId) {
		if (!userRepository.existsById(userId))
			throw new CustomException(ErrorType.USER_NOT_FOUND);

		// DB 일괄 조회
		Map<UUID, List<TeamUserJpaEntity>> membersOfTeam = new HashMap<UUID, List<TeamUserJpaEntity>>();
		for (TeamUserJpaEntity teamUser : teamUserRepository.findAllMembersByUserIds(userId)) {
		    List<TeamUserJpaEntity> members = membersOfTeam.getOrDefault(
		            teamUser.getTeam().getId(),
		            new ArrayList<TeamUserJpaEntity>());
            members.add(teamUser);
	        membersOfTeam.putIfAbsent(
	                teamUser.getTeam().getId(), 
	                members);
		}
		
		// DTO 변환
		return membersOfTeam.values().stream().map(teamMembers -> {
		    String teamLeaderName = null;
		    List<UUID> members = new ArrayList<UUID>();
		    for (TeamUserJpaEntity teamUserInTeam : teamMembers) {
		        if (teamUserInTeam.isLeader())
		            teamLeaderName = teamUserInTeam.getUser().getName();
                members.add(teamUserInTeam.getUser().getId());
		    }
			return TeamResponseDto.from(teamMembers.get(0).getTeam(), teamLeaderName, members);
		})
		.toList();
	}
	
	public void deleteTeam(UUID requesterId, UUID teamId) {
		if (!teamRepository.existsById(teamId))
			throw new CustomException(ErrorType.TEAM_NOT_FOUND);
		TeamUserJpaEntity teamUser = teamUserRepository.findByTeam_IdAndUser_Id(teamId, requesterId).orElse(null);
		if (teamUser == null || !teamUser.isLeader())
		    throw new CustomException(ErrorType.UN_AUTHORIZATION);
		
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
	@Transactional
	public void joinTeamByInviteCode(TeamUserRequestDto request) {
		Team team = teamRepository.findByInviteCode(InviteCodeUtil.strToLong(request.inviteCode()))
				.orElseThrow(() -> new CustomException(ErrorType.TEAM_NOT_FOUND));
		User user = userRepository.findById(request.userId())
				.orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

		team.addUser(user, repoService);
		chatRoomService.joinChatRoom(user, team.getId());
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

		List<WishlistDestinationRes> wishList = shoppingCartService.getCartData(team.getId(), OwnerType.TEAM);

		return TeamDetailDto.from(team, memberDtoList, wishList);
	}
}
