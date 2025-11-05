package com.se.Tlog.domain.Search.repository.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Data
public class NaverSearchRes<T> {
    private LocalDateTime lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<T> items;

    public void setLastBuildDate(String lastBuildDate) {
        this.lastBuildDate = LocalDateTime.parse(lastBuildDate, DateTimeFormatter.RFC_1123_DATE_TIME);
    }
}
