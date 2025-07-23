package com.se.Tlog.domain.Team.domain.repository;

import java.util.UUID;

public interface TeamRepositoryService {
    public void setLeader(UUID teamId, UUID userId);
	public long makeInviteCode();
}
