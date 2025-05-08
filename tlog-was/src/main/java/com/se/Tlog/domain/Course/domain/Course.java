package com.se.Tlog.domain.Course.domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "courses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Course {
    @Id
    private String id;

    private UUID ownerId;
    private OwnerType ownerType;
    
    private LocalDate startDate;
    private LocalDate endDate;
    @Transient
    private int duration;
    
    private List<CourseDate> dates;
    
    /**
     * 특정 날짜(day일차)에 여행지를 추가합니다.
     * <br/>day는 1부터 시작하는 날짜 값입니다.
     * @param day 1부터 시작하는 날짜 값
     * @param destinationId
     */
    public void addDestination(int day, String destinationId) {
        if (day < 1 || duration < day)
            throw new CustomException(ErrorType.INVALID_COURSE_DATE);
        
        while (dates.size() < day)
            dates.add(CourseDate.create());
        dates.get(day - 1).getDestinations().add(destinationId);
    }
    
    private Course(
            UUID ownerId, 
            OwnerType ownerType, 
            LocalDate startDate,
            LocalDate endDate, 
            List<CourseDate> dates) {
        this.ownerId = ownerId;
        this.ownerType = ownerType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.duration = startDate.until(endDate).getDays() + 1;
        if (dates == null)
            dates = new ArrayList<CourseDate>();
        this.dates = dates;
    }
    
    public static Course create(
            UUID ownerId,
            OwnerType ownerType,
            LocalDate startDate, 
            LocalDate endDate,
            List<CourseDate> dates) {
        if (ownerType == null || ownerId == null)
            throw new CustomException(ErrorType.INVALID_COURSE_OWNER);
        if (startDate == null || endDate == null
            || startDate.compareTo(endDate) > 0)
            throw new CustomException(ErrorType.INVALID_COURSE_DURATION);
        if (dates.size()
            > startDate.until(endDate).getDays() + 1)
            throw new CustomException(ErrorType.INVALID_COURSE_DATE);
        
        return new Course(ownerId, ownerType, startDate, endDate, dates);
    }
}
