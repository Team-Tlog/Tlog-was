package com.se.Tlog.domain.Search.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Travel.domain.Destination;

/**
 * 검색에 있어 실질적인 MongoDB 쿼리를 담당합니다.
 * <br/> 그러나 검색 기능과 Page 기능이 함께 지원되지 않기에, 페이징을 사용하기 위해서는
 * <br/> 이 리포지토리를 래핑하는 DestinationSearchRepository를 사용하는 것이 좋습니다.
 */
@Repository
interface RawDestinationSearchRepository extends MongoRepository<Destination, String> {
    @Aggregation(pipeline = {
            """
            { 
                $search: {
                    'index': 'destination_index', 
                    'autocomplete': {
                        'query': ?0,
                        'path': 'address'
                    }
                }
            }
            """})
    List<Destination> autoCompleteByAddress(String address);
    
    @Aggregation(pipeline = {
            """
            { 
                $search: {
                    'index': 'destination_index', 
                    'autocomplete': {
                        'query': ?0,
                        'path': 'name'
                    }
                }
            }
            """})
    List<Destination> autoCompleteByName(String name);
    
    @Aggregation(pipeline = {
            """
            { 
                $search: {
                    'index': 'destination_index', 
                    'text': {
                        'query': ?0,
                        'path': 'city'
                    }
                }
            }
            """,
            """
            {
                $facet: {
                    'totalCountArr': [ { $count: 'totalCount' } ],
                    'results': [
                        { $skip: ?1 },
                        { $limit: ?2 }                    
                    ]
                }
            }
            """,
            // 최종 결과 다듬기 (null 처리, 배열로 감싸진 형태에서 꺼내기 등)
            """ 
            { 
                $project: {
                    'totalSize': { $ifNull: [ { $arrayElemAt: [ '$totalCountArr.totalCount', 0 ] }, 0 ] },
                    'pagedDestinations': { $ifNull: [ '$results', [] ] }
                }
            }
            """})
    RawPagedResultDto searchByCity(String city, long skip, int limit);
}
