package com.se.Tlog.domain.User.repository.jpa;

import com.se.Tlog.domain.User.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByProviderUserInfo(String providerUserInfo);

}
