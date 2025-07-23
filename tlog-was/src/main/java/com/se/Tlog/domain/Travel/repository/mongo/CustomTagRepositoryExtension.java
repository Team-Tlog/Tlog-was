package com.se.Tlog.domain.Travel.repository.mongo;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationOperation;
import org.springframework.data.mongodb.core.aggregation.LimitOperation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.UnwindOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.mongodb.DuplicateKeyException;
import com.se.Tlog.domain.Travel.controller.dto.DestinationSimilarDto;
import com.se.Tlog.domain.Travel.domain.CustomTagDocument;
import com.se.Tlog.domain.Travel.domain.TagCount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomTagRepositoryExtension {
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
    
    public List<DestinationSimilarDto> findRelatedDestinations(String destinationId, List<TagCount> topTags) {
        // ne : 자기자신 제외 not equal
        MatchOperation match = Aggregation.match(Criteria.where("destinationId").ne(destinationId));

        // 태그 있으면 tag value 반환 , 없으면 0 -> List 형태로
        List<Object> addOperands = topTags.stream()
                .map(tagCount -> {
                    String tag = "$customTags." + tagCount.getTagName();
                    return new Document("$ifNull", List.of(tag, 0));
                })
                .collect(Collectors.toList());

        // 위 List 결과 전체 합산 및 score 필드 생성  (db 반영 x)
        AddFieldsOperation addScore = Aggregation.addFields()
                .addField("score").withValue(new Document("$add", addOperands))
                .build();
        // customtags 의 destinationId 타입 변환
        AggregationOperation convertDestinationId = context -> new Document(
                "$addFields",
                new Document("destinationIdObjectId", new Document("$toObjectId", "$destinationId"))
        );

        // lookup 설정 destinations 대상
        LookupOperation lookup = LookupOperation.newLookup()
                .from("destinations")
                .localField("destinationIdObjectId")
                .foreignField("_id")
                .as("destination");

        UnwindOperation unwind = Aggregation.unwind("destination");

        SortOperation sort = Aggregation.sort(Sort.by(Sort.Direction.DESC, "score"));
        LimitOperation limit = Aggregation.limit(5);

        // id 타입 변환 및 tag 구조 변환
        AggregationExpression customTagsExpression = context ->
                new Document("$map", new Document()
                        .append("input", new Document(
                                "$slice",
                                List.of(new Document("$objectToArray", "$customTags"),3)
                        ))
                        .append("as","tag")
                        .append("in", new Document()
                                .append("tagName", "$$tag.k")
                                .append("count", "$$tag.v")
                        ));

        ProjectionOperation project = Aggregation.project()
                .andInclude("destinationId")
                .and("destination.name").as("name")
                .and("destination.imageUrl").as("imageUrl")
                .and("destination.description").as("description")
                .and(customTagsExpression).as("customTags");

        Aggregation aggregation = Aggregation.newAggregation(
                match,
                addScore,
                convertDestinationId,
                lookup,
                unwind,
                sort,
                limit,
                project
        );

        return mongoTemplate.aggregate(
                aggregation,
                "customtags",
                DestinationSimilarDto.class
        ).getMappedResults();
    }
}
