package com.se.Tlog.domain.Guide.repository.jpa;

import com.se.Tlog.domain.Guide.domain.Guide;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Integer> {
    @Query("""
        SELECT g 
        FROM Guide g  
        WHERE (:latitude BETWEEN g.minLatitude AND g.maxLatitude)
            AND (:longitude BETWEEN g.minLongitude AND g.maxLongitude)
        """)
    Page<Guide> findLocalGuide(double latitude, double longitude, Pageable pageable);
}
