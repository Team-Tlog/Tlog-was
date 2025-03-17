package com.se.Tlog.domain.Social.infrastructure.jpa;

import com.se.Tlog.domain.Social.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface FollowRepository extends JpaRepository<Follow, UUID> {
    Optional<Follow> findByFromUserIdAndToUserId(UUID fromUserId, UUID toUserId);

    @Query("select f.toUserId from Follow  f where f.fromUserId = :fromUserId")
    Page<UUID> findToUserIdsByFromUserId(@Param("fromUserId") UUID fromUserId, Pageable pageable);

    @Query("select f.fromUserId from Follow f where f.toUserId = :toUserId")
    Page<UUID> findFromUserIdsByToUserId(@Param("toUserId") UUID toUserId, Pageable pageable);
}
