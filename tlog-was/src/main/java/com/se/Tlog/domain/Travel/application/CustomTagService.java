package com.se.Tlog.domain.Travel.application;

import com.mongodb.DuplicateKeyException;
import com.se.Tlog.domain.Travel.domain.CustomTagDocument;
import com.se.Tlog.domain.Travel.domain.TagCount;
import com.se.Tlog.domain.Travel.repository.mongo.CustomTagDocumentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomTagService {
    private final CustomTagDocumentRepository customTagDocumentRepository;
    private final MongoTemplate mongoTemplate;

    public void addCustomTag(String destinationId, List<String> tagNameList) {

        try {
            mongoTemplate.upsert(
                    Query.query(Criteria.where("destinationId").is(destinationId)),
                    new Update().setOnInsert("destinationId", destinationId),
                    CustomTagDocument.class
            );
        } catch (DuplicateKeyException e) {
            log.info(" CustomTagDocument가 이미 존재합니다 destinationId : {}", destinationId);
        }

        for (String tagName : tagNameList) {
            String normalizedTagName = tagName.trim().toLowerCase();

            mongoTemplate.updateFirst(
                    Query.query(Criteria.where("destinationId").is(destinationId)),
                    new Update().inc("customTags." + normalizedTagName, 1),
                    CustomTagDocument.class
            );
        }

    }

    public List<TagCount> getTopTags(String destinationId, int limit) {
        return customTagDocumentRepository.findByDestinationId(destinationId)
                .map(doc -> doc.getCustomTags().entrySet().stream()
                        .map(entry -> new TagCount(entry.getKey(), entry.getValue()))
                        .sorted(Comparator.comparing(TagCount::getCount).reversed())
                        .limit(limit)
                        .toList()
                ).orElse(List.of());
    }
    
    public Map<String, List<TagCount>> getAllTopTags(List<String> destinationIds, int limit) {
        Map<String, List<TagCount>> tagCountMap = 
                customTagDocumentRepository.findAllByDestinationIdIn(destinationIds)
                .stream()
                .collect(Collectors.toMap(
                        CustomTagDocument::getDestinationId,
                        doc -> doc.getCustomTags().entrySet().stream()
                                .map(entry -> new TagCount(entry.getKey(),entry.getValue()))
                            .sorted(Comparator.comparing(TagCount::getCount).reversed())
                            .limit(limit)
                            .toList()));
        for (String desId : destinationIds)
            tagCountMap.putIfAbsent(desId, List.of());
        return tagCountMap;
    }
}
