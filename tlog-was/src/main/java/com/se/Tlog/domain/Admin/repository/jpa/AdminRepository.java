package com.se.Tlog.domain.Admin.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Admin.domain.Admin;

import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
