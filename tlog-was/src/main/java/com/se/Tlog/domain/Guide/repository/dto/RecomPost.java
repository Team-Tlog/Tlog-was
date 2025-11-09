package com.se.Tlog.domain.Guide.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class RecomPost {
    private List<String> imageUrls;
    private String content;
    private String id;
}
