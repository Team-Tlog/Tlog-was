package com.se.Tlog.domain.Travel.repository.mongo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Travel.domain.Tag;


@Repository
public interface TagRepository extends MongoRepository<Tag, String> {

    boolean existsByName(String name);

    // 버그 위험성 : 순서가 보장되지 않을 경우 다음 페이지에 이전 페이지의 항목이 표시될 수 있습니다.
    // @Query(sort = "{'_id' : 1}") 가 추가될 것!
    @Query("{'isDeleted': false}")
    Page<Tag> findAllByActiveTags(Pageable pageable);
}
