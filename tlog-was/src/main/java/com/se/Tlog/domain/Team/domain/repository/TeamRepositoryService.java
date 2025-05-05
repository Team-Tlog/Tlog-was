package com.se.Tlog.domain.Team.domain.repository;

import java.util.UUID;

public interface TeamRepositoryService {
	public boolean isExistInTeam(UUID teamId, UUID userId);
	public long countMemberInTeam(UUID teamId);
	public void addUserToTeam(UUID teamId, UUID userId);
    public void setLeader(UUID teamId, UUID userId);
	public void deleteUserInTeam(UUID teamId, UUID userId);
	public void deleteTeamUsers(UUID teamId);
	public long makeInviteCode();
}
