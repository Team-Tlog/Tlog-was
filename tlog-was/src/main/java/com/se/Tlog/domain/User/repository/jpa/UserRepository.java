package com.se.Tlog.domain.User.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.User.domain.User;
import com.se.Tlog.domain.User.repository.dto.UserTbtiProjection;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByProviderUserInfo(String providerUserInfo);
    boolean existsBySnsId(String snsId); // 또는 snsId
    List<User> findByIdIn(Set<UUID> userIds);
    
    @Query("select u.id, u.tbti from User u where u.id = :id")
    UserTbtiProjection findOfTbtiById(@Param("id") UUID id);
    @Query("select u.id, u.tbti from User u")
    List<UserTbtiProjection> findAllOfIdAndTbti();
}
