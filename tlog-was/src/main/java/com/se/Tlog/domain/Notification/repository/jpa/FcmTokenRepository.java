package com.se.Tlog.domain.Notification.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.se.Tlog.domain.Notification.repository.jpa.entity.UserFcmTokenJpaEntity;

public interface FcmTokenRepository extends JpaRepository<UserFcmTokenJpaEntity, UUID> {
	public UserFcmTokenJpaEntity findByUserId(UUID userId);
	public List<UserFcmTokenJpaEntity> findAllByUserIdIn(List<UUID> userIds);
	
	@Query("SELECT t.fcmToken FROM UserFcmTokenJpaEntity t WHERE t.user.id IN :userIds")
	public List<String> findFcmTokenByUserIdIn(@Param("userIds") List<UUID> userIds);
}
