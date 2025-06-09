package com.se.Tlog.domain.Search.repository.mongo;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Social.Post.domain.Post;

/**
 * 현재는 Mongo Atlas Search를 사용중입니다만,
 * <br>만약 추가적인 성능이 요구된다면 Elastic Search를 도입하는 것도
 * <br>고려중에 있습니다.
 */
@Repository
interface RawSnsPostSearchRepository extends MongoRepository<Post, String> {
    
    // 최근순 정렬 및 빠른 페이징 처리를 위한 Skip
    static final String QUERY_1_SORT_BY_RECENT = "{ $sort: { _id: -1 } }";
    static final String QUERY_1_SKIP = "{ $match: { $expr: { $lt: [ '$_id', ObjectId( ?1 ) ] } } }";
    
    // 여행지 검색결과의 join을 위한 전처리  
    static final String QUERY_1_CONVERT_ID = "{ $addFields: { _courseId: { $convert: { input: '$courseId', to: 'objectId' } } } }";
    
    // 여행지 이름이 하나라도 검색되는 코스의 id를 모두 조회
    // 파이프라인은 다음과 같습니다 :
    //      [코스의 여행지 id 추출] -> [여행지 중 검색에 매칭되는 항목만 조인] -> [조인 결과가 1개 이상인 항목 필터링] -> [id만 추출]
    static final String INNER_QUERY_1_SEARCH_COURSE = 
            """
            {
              $project: {
                _id: 1,
                destIds: {
                  $reduce: {
                    input: "$dates.destinations",
                    initialValue: [],
                    in: {
                      $concatArrays: [
                        "$$value",
                        {
                          $map: {
                            input: "$$this",
                            as: "item",
                            in: {
                              $convert: {
                                input: "$$item",
                                to: "objectId",
                              },
                            },
                          },
                        },
                      ],
                    },
                  },
                },
              },
            },
            {
              $lookup: {
                from: "destinations",
                localField: "destIds",
                foreignField: "_id",
                pipeline: [
                  {
                    $search: {
                      index: "destination_index",
                      text: {
                        query: ?2,
                        path: "name",
                      },
                    },
                  },
                  {
                    $project: {
                      _id: 1,
                    },
                  },
                ],
                as: "result",
              },
            },
            {
              $match: {
                $expr: {
                  $gt: [
                    {
                      $size: "$result",
                    },
                    0,
                  ],
                },
              },
            },
            {
              $project: {
                _id: 1,
              },
            },
            """;
    
    // 검색된 코스와 join
    static final String QUERY_1_JOIN_WITH_COURSE_RESULTS = 
            """
            {
                $lookup: {
                from: "courses",
                localField: "_courseId",
                foreignField: "_id",
                pipeline: [
                    """
                    + INNER_QUERY_1_SEARCH_COURSE +
                    """
                ],
                as: "courseLookup",
                },
            },
            """;
    
    // 게시글 내용에 검색어가 포함되거나 / 관련 여행지가 검색되어 코스와 조인이 된 게시글을 모두 필터링
    static final String QUERY_1_FILTER_RESULTS =
            """
            {
              $match: {
                $or: [
                  { content: { $regex: ?2 } },
                  { $expr: { $gt: [ { $size: "$courseLookup" }, 0 ] } }
                ],
              },
            },""";
    
    // 페이징 처리를 위한 limit
    static final String QUERY_1_LIMIT = "{ $limit: ?0 }";
    
    @Aggregation(pipeline = {
            QUERY_1_SORT_BY_RECENT,
            QUERY_1_SKIP,
            QUERY_1_CONVERT_ID,
            QUERY_1_JOIN_WITH_COURSE_RESULTS,
            QUERY_1_FILTER_RESULTS,
            QUERY_1_LIMIT
    })
    List<Post> searchOfDestinationsAndContent(int size, ObjectId lastPostId, String queryText);
}
