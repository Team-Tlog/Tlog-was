package com.se.Tlog.domain.Course.domain;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
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
    
    private List<CourseDate> dates;

    public int getDuration() {
        if(startDate == null || endDate == null || startDate.isAfter(endDate)) return 0;

        return (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    public void addDestination(int day, String destinationId) {
        if (day < 1 || getDuration() < day)
            throw new CustomException(ErrorType.INVALID_COURSE_DATE);

        if (this.dates == null) {
            this.dates = new ArrayList<>();
        }

        while (dates.size() < day) {
            int newDayNumber = dates.size() + 1;
            dates.add(CourseDate.create(newDayNumber, new ArrayList<String>()));
        }

        dates.get(day - 1).getDestinationsIds().add(destinationId);
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
