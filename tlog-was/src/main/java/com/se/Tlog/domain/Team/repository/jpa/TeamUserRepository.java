package com.se.Tlog.domain.Team.repository.jpa;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Team.repository.jpa.entity.TeamUserJpaEntity;

import jakarta.transaction.Transactional;

@Repository
public interface TeamUserRepository extends JpaRepository<TeamUserJpaEntity, Long> {
	public boolean existsByTeam_IdAndUser_Id(UUID teamId, UUID userId);
	@Transactional
	public void deleteByTeam_IdAndUser_Id(UUID teamId, UUID userId);
	@Transactional
	public void deleteByTeam_Id(UUID teamId);
	public List<TeamUserJpaEntity> findByUser_Id(UUID userId);
	public long countByTeam_Id(UUID teamId);
	Optional<TeamUserJpaEntity> findByTeam_IdAndUser_Id(UUID teamId, UUID userId);
	Optional<TeamUserJpaEntity> findByTeam_IdAndIsLeaderTrue(UUID teamId);

	@Query("select tu from TeamUserJpaEntity tu join fetch tu.user where tu.team.id = :teamId")
	List<TeamUserJpaEntity> findWithUserByTeamId(@Param("teamId") UUID teamId);
}
