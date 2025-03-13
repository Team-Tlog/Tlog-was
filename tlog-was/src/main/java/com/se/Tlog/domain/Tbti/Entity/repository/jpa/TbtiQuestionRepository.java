package com.se.Tlog.domain.Tbti.Entity.repository.jpa;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TbtiQuestionRepository extends JpaRepository<TbtiQuestion, UUID> {
    Page<TbtiQuestion> findByTraitCategory(TraitCategory traitCategory, Pageable pageable);
}
