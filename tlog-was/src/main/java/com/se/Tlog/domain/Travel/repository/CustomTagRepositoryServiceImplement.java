package com.se.Tlog.domain.Travel.repository;

import com.mongodb.DuplicateKeyException;
import com.se.Tlog.domain.Travel.domain.CustomTagDocument;
import com.se.Tlog.domain.Travel.domain.repository.CustomTagRepositoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomTagRepositoryServiceImplement implements CustomTagRepositoryService {
    private final MongoTemplate mongoTemplate;

    @Override
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
}
