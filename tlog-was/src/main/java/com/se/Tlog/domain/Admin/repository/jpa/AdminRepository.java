package com.se.Tlog.domain.Admin.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.se.Tlog.domain.Admin.domain.Admin;

import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
