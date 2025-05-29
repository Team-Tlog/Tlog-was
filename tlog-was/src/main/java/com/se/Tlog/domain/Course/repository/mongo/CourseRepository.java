package com.se.Tlog.domain.Course.repository.mongo;

import java.util.List;
import java.util.UUID;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.se.Tlog.domain.Course.domain.Course;
import com.se.Tlog.domain.Course.domain.OwnerType;
import com.se.Tlog.domain.Course.repository.dto.CourseDistrictsDto;

@Repository
public interface CourseRepository extends MongoRepository<Course, String> {
    List<Course> findAllByOwnerIdAndOwnerType(UUID ownerId, OwnerType ownerType);
    
    @Aggregation(pipeline = {
            // Course 조회
            """
            { $match: { _id: ObjectId(?0) } }
            """,
            // Course 내 모든 여행지의 id 추출
            """
            { 
                $project: { 
                    _id: 1,
                    destIds: { 
                        $reduce: {
                            input: '$dates.destinations',
                            initialValue: [],
                            in: { 
                                $concatArrays : [ 
                                    "$$value",
                                    { 
                                        $map : {
                                            input : "$$this",
                                            as : "item",
                                            in : { $convert: { input : "$$item", to : "objectId" } }
                                        }
                                    }
                                ]
                            }
                        }
                    }
                }
            }
            """,
            // 여행지 컬렉션과 join
            """
            { 
                $lookup: {
                    from: 'destinations',
                    localField: 'destIds',
                    foreignField: '_id',
                    pipeline: [ { $project: { _id:0, district:1 }} ],
                    as: 'districts'
                }
            }
            """,
            // DTO에 매핑
            """
            {
                $project: {
                    courseId: "$_id", 
                    districts: {
                        $reduce: {
                            input: "$districts.district",
                            initialValue: [],
                            in: { $concatArrays: ["$$value", ["$$this"]] }
                        }
                    }
                } 
            }
            """
    })
    CourseDistrictsDto findAllDestinationNameInCourse(String courseId);
}
