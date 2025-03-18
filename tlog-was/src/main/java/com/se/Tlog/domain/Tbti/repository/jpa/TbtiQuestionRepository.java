package com.se.Tlog.domain.Tbti.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Tbti.domain.TbtiQuestion;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;

import java.util.UUID;

@Repository
public interface TbtiQuestionRepository extends JpaRepository<TbtiQuestion, UUID> {
    Page<TbtiQuestion> findByTraitCategory(TraitCategory traitCategory, Pageable pageable);
}
