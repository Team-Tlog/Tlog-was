package com.se.Tlog.domain.Tbti.Entity.repository;

import com.se.Tlog.domain.Tbti.Entity.TbtiQuestion;
import com.se.Tlog.domain.Tbti.Entity.TraitCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TbtiQuestionRepository extends JpaRepository<TbtiQuestion,Long> {
    Page<TbtiQuestion> findByTraitCategory(TraitCategory traitCategory, Pageable pageable);
}
