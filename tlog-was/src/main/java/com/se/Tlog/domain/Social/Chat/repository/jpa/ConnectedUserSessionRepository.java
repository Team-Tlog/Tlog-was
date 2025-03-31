package com.se.Tlog.domain.Social.Chat.repository.jpa;

import com.se.Tlog.domain.Social.Chat.domain.ConnectedUserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectedUserSessionRepository extends JpaRepository<ConnectedUserSession, String> {
}
