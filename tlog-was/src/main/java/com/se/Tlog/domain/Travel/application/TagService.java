package com.se.Tlog.domain.Travel.application;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.controller.dto.TagDto;
import com.se.Tlog.domain.Travel.controller.dto.TagRes;
import com.se.Tlog.domain.Travel.domain.Tag;
import com.se.Tlog.domain.Travel.domain.repository.TagRepositoryService;
import com.se.Tlog.domain.Travel.repository.mongo.TagRepository;


import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ApplicationService
@RequiredArgsConstructor
@Transactional
public class TagService {
    private final TagRepository tagRepository;
    private final TagRepositoryService tagRepo;
    private final MongoTemplate mongoTemplate;

    @Transactional(readOnly = true)
    public List<TagRes> getAllActiveTags(Pageable pageable) {

        // 버그 위험성 : 순서가 보장되지 않을 경우 다음 페이지에 이전 페이지의 항목이 표시될 수 있습니다.
        // @Query(sort = "{'_id' : 1}") 가 추가될 것!
        return tagRepository.findAllByActiveTags(pageable).stream()
                .map(tag -> TagRes.from(tag.getId(), tag.getName()))
                .toList();
    }

    public void createTag(TagDto tagDto) {
    	Tag newTag = Tag.createTag(tagDto.name(), 0, tagRepo);
        tagRepository.save(newTag);
    }


    public void updateTagDeletedStatus(String tagId, boolean isDeleted) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_TAG));
        tag.updateTagDeleted(isDeleted);
        tagRepository.save(tag);

        MongoCollection<Document> collection = mongoTemplate.getCollection("destinations");

        Bson filter = Filters.eq("tags._id", new ObjectId(tagId));
        Bson update = Updates.set("tags.$[elem].isDeleted", isDeleted);

        UpdateOptions options = new UpdateOptions()
                .arrayFilters(List.of(
                        Filters.eq("elem._id", new ObjectId(tagId))
                ));

        collection.updateMany(filter, update, options);

    }
}
