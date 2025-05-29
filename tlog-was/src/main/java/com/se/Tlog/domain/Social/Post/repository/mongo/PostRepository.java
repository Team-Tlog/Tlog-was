package com.se.Tlog.domain.Social.Post.repository.mongo;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Social.Post.domain.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String> {
    /**
     * 대표 이미지 조회를 위해 이미지 필드만 조회합니다.
     * @param author
     * @param pageable
     * @return
     */
    @Query(value = "{ 'author' : ?0 }", fields = "{ '_id' : 1, 'imageUrls' : 1 }")
    Page<Post> findAllPreviewByAuthor(UUID author, Pageable pageable);
}
