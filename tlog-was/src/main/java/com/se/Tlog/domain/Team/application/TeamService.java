package com.se.Tlog.domain.Team.application;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Social.Chat.room.ChatRoomService;
import com.se.Tlog.domain.Team.controller.dto.*;
import com.se.Tlog.domain.Team.domain.InviteCodeUtil;
import com.se.Tlog.domain.Team.domain.Team;
import com.se.Tlog.domain.Team.domain.TeamDomainService;
import com.se.Tlog.domain.Team.repository.jpa.TeamRepository;
import com.se.Tlog.domain.Team.repository.jpa.TeamUserRepository;
import com.se.Tlog.domain.Team.repository.jpa.entity.TeamUserJpaEntity;
import com.se.Tlog.domain.Team.travelplan.TravelPlan;
import com.se.Tlog.domain.Team.travelplan.repository.mongo.TravelPlanRepository;
import com.se.Tlog.domain.Team.travelplan.service.TravelPlanService;
import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.domain.service.UserDomainService;
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
	private final TravelPlanRepository travelPlanRepository;
	
	private final TeamDomainService teamDomainService;
	private final UserDomainService userDomainService;

	private final ShoppingCartService shoppingCartService;
	private final ChatRoomService chatRoomService;
	private final TravelPlanService travelPlanService;

	@Transactional
	public TeamCreateRes createTeam(CreateTeamRequestDto request) {
		if (request.name() == null)
			throw new CustomException(ErrorType.TEAM_NAME_NOT_FOUND);

		User user = userDomainService.findByIdOrThrow(request.creator());

		Team newTeam = teamDomainService.createTeam(request.name(), user);
		travelPlanService.saveTravelPlan(newTeam.getId(), request.travelPlan());

		Long chatRoomId = chatRoomService.create(user, newTeam.getId());
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
		Set<UUID> teamIds = membersOfTeam.keySet();
		List<TravelPlan> travelPlans = travelPlanRepository.findAllByTeamIdIn(teamIds.stream().map(UUID::toString).toList());

		Map<String, TravelPlan> travelPlanMap = travelPlans.stream()
				.collect(Collectors.toMap(TravelPlan::getTeamId, Function.identity()));

		// DTO 변환
		return membersOfTeam.entrySet().stream()
				.map(entry -> toTeamResponseDto(entry, travelPlanMap))
				.toList();
	}
	private TeamResponseDto toTeamResponseDto(
			Map.Entry<UUID, List<TeamUserJpaEntity>> entry,
			Map<String, TravelPlan> travelPlanMap
	) {
		List<TeamUserJpaEntity> teamMembers = entry.getValue();
		Team team = teamMembers.get(0).getTeam();
		List<TeamMemberSimpleDto> memberSimpleDtoList = new ArrayList<>();

		TravelPlan travelPlan = travelPlanMap.get(entry.getKey().toString());
		TravelPlanDto travelPlanDto = travelPlan != null ? TravelPlanDto.from(travelPlan) : null;

		User teamLeader = null;
		for (TeamUserJpaEntity teamUserInTeam : teamMembers) {
			User user = teamUserInTeam.getUser();
			if (teamUserInTeam.isLeader())
				teamLeader = user;

			memberSimpleDtoList.add(
					TeamMemberSimpleDto.from(user)
			);
		}

		return TeamResponseDto.from(team, teamLeader, memberSimpleDtoList, travelPlanDto);
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

		teamDomainService.addUser(team, user);
		chatRoomService.joinChatRoom(user, team.getId());
	}
	
	public void leaveTeam(UUID teamId, UUID userId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new CustomException(ErrorType.TEAM_NOT_FOUND));
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorType.USER_NOT_FOUND));

		teamDomainService.deleteUser(team, user);
	}

	public TeamDetailDto getTeamDetails(UUID teamId) {
		Team team = teamRepository.findById(teamId)
				.orElseThrow(() -> new CustomException(ErrorType.TEAM_NOT_FOUND));
		List<TeamMemberDto> memberDtoList = teamUserRepository.findWithUserByTeamId(team.getId())
				.stream().map(teamUser -> 
				        TeamMemberDto.from(teamUser.getUser(), teamUser.isLeader())
		        ).toList();

		List<WishlistDestinationRes> wishList = shoppingCartService.getCartData(team.getId(), OwnerType.TEAM);

		TravelPlan travelPlan = travelPlanRepository.findByTeamId(teamId.toString())
				.orElseThrow(() -> new CustomException(ErrorType.TRAVEL_PLAN_NOT_FOUND));

		TravelPlanDto travelPlanDto = TravelPlanDto.from(travelPlan);
		return TeamDetailDto.from(
				team,
				chatRoomService.getRoomIdByTeam(team.getId()),
				memberDtoList,
				wishList,
				travelPlanDto);
	}
}
