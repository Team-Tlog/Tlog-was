package com.se.Tlog.domain.Tbti.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Tbti.domain.TbtiQuestion;
import com.se.Tlog.domain.Tbti.domain.TraitCategory;

import java.util.List;
import java.util.UUID;

@Repository
public interface TbtiQuestionRepository extends JpaRepository<TbtiQuestion, UUID> {
    
    @Query(value = "SELECT q FROM TbtiQuestion q JOIN FETCH q.tbtiAnswers")
    List<TbtiQuestion> findAllFetch();
    
    @Query(value = "SELECT q FROM TbtiQuestion q JOIN FETCH q.tbtiAnswers WHERE q.traitCategory = :traitCategory")
    List<TbtiQuestion> findByTraitCategoryFetch(TraitCategory traitCategory);

    @Query(value = "SELECT q FROM TbtiQuestion q JOIN FETCH q.tbtiAnswers a WHERE a.id = :id")
    List<TbtiQuestion> findByAnswerId(long id);
}
