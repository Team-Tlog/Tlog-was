package com.se.Tlog.domain.Social.Follow.repository.jpa;

import com.se.Tlog.domain.Social.Follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FollowRepository extends JpaRepository<Follow, UUID> {
    Optional<Follow> findByFromUserIdAndToUserId(UUID fromUserId, UUID toUserId);

    @Query("select f.toUserId from Follow  f where f.fromUserId = :fromUserId")
    List<UUID> findToUserIdsByFromUserId(@Param("fromUserId") UUID fromUserId);

    @Query("select f.fromUserId from Follow f where f.toUserId = :toUserId")
    List<UUID> findFromUserIdsByToUserId(@Param("toUserId") UUID toUserId);
    
    int countByFromUserId(UUID fromUserId);
    int countByToUserId(UUID toUserId);
}
