package com.se.Tlog.domain.User.repository.jpa;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.se.Tlog.domain.User.domain.UserTagInfo;
import org.springframework.data.jpa.repository.Query;

public interface UserTagRepository extends JpaRepository<UserTagInfo, UUID> {
    @Query("SELECT u.weight FROM UserTagInfo u WHERE u.user.id = :userid ORDER BY u.tagId ASC")
    List<Double> findWeightsByUserId(UUID userid);
}
