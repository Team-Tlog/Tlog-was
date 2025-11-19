package com.se.Tlog.domain.Course.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDate {


    private int dayNumber;
    private List<String> destinationsIds;

    private CourseDate(int dayNumber, List<String> destinations) {
        this.dayNumber = dayNumber;
        this.destinationsIds = destinations;
    }

    private CourseDate(List<String> destinations) {
        this.destinationsIds = destinations;
    }
    
    public static CourseDate create() {
        return new CourseDate(0, new ArrayList<String>());
    }

    public static CourseDate create(int dayNumber, List<String> destinations) {
        if (destinations == null)
            destinations = new ArrayList<String>();
        return new CourseDate(dayNumber, destinations);
    }
}
