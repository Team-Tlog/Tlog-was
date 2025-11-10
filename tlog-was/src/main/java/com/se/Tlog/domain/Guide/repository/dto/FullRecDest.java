package com.se.Tlog.domain.Guide.repository.dto;

import lombok.Data;

import java.util.List;

@Data
public class FullRecDest {
    private String id;

    private String imageUrl;

    private String title;

    private String description;

    private List<Dest> destinations;
    @Data
    public static class Dest {
        private String imageUrl;
        private String name;
        private String id;
    }
}
