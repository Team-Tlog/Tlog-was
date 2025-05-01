package com.se.Tlog.domain.Team.repository.jpa;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Team.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, UUID> {
	boolean existsByInviteCode(long InviteCode);

	Optional<Team> findByInviteCode(long inviteCode);
}
