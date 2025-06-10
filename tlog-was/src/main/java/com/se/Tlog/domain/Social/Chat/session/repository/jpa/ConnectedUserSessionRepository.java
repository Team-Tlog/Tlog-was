package com.se.Tlog.domain.Social.Chat.session.repository.jpa;

import com.se.Tlog.domain.Social.Chat.session.ConnectedUserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface ConnectedUserSessionRepository extends JpaRepository<ConnectedUserSession, String> {
    @Query("SELECT cus.userId from ConnectedUserSession cus where cus.roomId = :roomId")
    Set<UUID> findConnectedUserIdsByRoomId(@Param("roomId") Long roomId);
}
