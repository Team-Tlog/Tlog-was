package com.se.Tlog.domain.Admin.repository.jpa;

import com.se.Tlog.domain.Admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
