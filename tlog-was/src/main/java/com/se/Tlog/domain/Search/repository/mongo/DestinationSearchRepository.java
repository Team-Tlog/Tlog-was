package com.se.Tlog.domain.Search.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Search.repository.DestinationRawData;

@Repository
public interface DestinationSearchRepository extends MongoRepository<DestinationRawData, String> {
	// Atlas Search를 위한 Json 쿼리
	@Aggregation(pipeline = {
    		"""
    		{ 
	    		$search: {
	    			'index' : 'destination_index', 
	    			'compound': {
    		 			'should': [
    		 		 		{
		 		 		 		'autocomplete': {
    				 		 		'query': ?0,
    				 		 		'path': 'name'
				 		 		}
			 		 		},
			 		 		{
			 		 			'autocomplete': {
			 		 				'query': ?0,
    				 				'path': 'address'
				 				}
			 				}
		 				]
					}
				}
			}
    		"""
    })
    List<DestinationRawData> autoCompleteBy(String searchText);
}
