package com.se.Tlog.domain.User.infrastructure.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.se.Tlog.domain.User.domain.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByProviderUserInfo(String providerUserInfo);

}
