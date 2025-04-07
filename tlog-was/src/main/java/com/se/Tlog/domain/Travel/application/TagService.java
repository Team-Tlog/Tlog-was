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

        return tagRepository.findAllByActiveTags(pageable).stream()
                .map(tag -> TagRes.from(tag.getId(), tag.getName()))
                .toList();
    }

    public void createTag(TagDto tagDto) {
    	Tag newTag = Tag.createTag(tagDto.getName(), 0, tagRepo);
        tagRepository.save(newTag);
    }


    public void deleteTag(String tagId) {

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND));
        tag.markAsDeleted();
        tagRepository.save(tag);

        MongoCollection<Document> collection = mongoTemplate.getCollection("destinations");

        Bson filter = Filters.eq("tags._id", new ObjectId(tagId));
        Bson update = Updates.set("tags.$[elem].isDeleted", true);

        UpdateOptions options = new UpdateOptions()
                .arrayFilters(List.of(
                        Filters.eq("elem._id", new ObjectId(tagId))
                ));

        collection.updateMany(filter, update, options);

    }
}
