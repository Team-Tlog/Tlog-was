package com.se.Tlog.domain.Reward.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Reward.domain.Reward;

@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    @Modifying
    @Query("""
            UPDATE Reward r 
            SET r.isDefault =
                CASE
                    WHEN (r.rewardInfo.id = :rewardInfoId) THEN true
                    ELSE false
                END
            WHERE r.user.id = :userId
            """)
    void updateDefaultReward(Long rewardInfoId, UUID userId);
    
    @Query("SELECT r FROM Reward r JOIN FETCH r.user JOIN FETCH r.rewardInfo WHERE r.user.id = :userId")
	List<Reward> findAllByUser_Id(UUID userId);
    
	boolean existsByRewardInfo_IdAndUser_Id(Long rewardInfoId, UUID userId);
}
