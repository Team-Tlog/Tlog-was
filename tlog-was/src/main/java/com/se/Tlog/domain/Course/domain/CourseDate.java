package com.se.Tlog.domain.Course.domain;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDate {
    // private int day;
    
    private List<String> destinations;
    
    private CourseDate(List<String> destinations) {
        this.destinations = destinations;
    }
    
    public static CourseDate create() {
        return create(null);
    }
    
    public static CourseDate create(List<String> destinations) {
        if (destinations == null)
            destinations = new ArrayList<String>();
        return new CourseDate(destinations);
    }
}
