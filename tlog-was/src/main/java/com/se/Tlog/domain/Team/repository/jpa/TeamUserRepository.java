package com.se.Tlog.domain.Team.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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
	public List<TeamUserJpaEntity> findByTeam_Id(UUID teamId);
	public long countByTeam_Id(UUID teamId);
}
