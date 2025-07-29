package com.se.Tlog.domain.User.repository.jpa;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.se.Tlog.domain.User.domain.UserTagInfo;

public interface UserTagRepository extends JpaRepository<UserTagInfo, UUID> {

}
