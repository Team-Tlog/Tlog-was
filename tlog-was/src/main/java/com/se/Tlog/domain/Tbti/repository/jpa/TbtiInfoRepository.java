package com.se.Tlog.domain.Tbti.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Tbti.domain.TbtiInfo;

@Repository
public interface TbtiInfoRepository extends JpaRepository<TbtiInfo, Long> {
    TbtiInfo findByTbtiString(String tbtiString);
    boolean existsByTbtiString(String tbtiString);
}
