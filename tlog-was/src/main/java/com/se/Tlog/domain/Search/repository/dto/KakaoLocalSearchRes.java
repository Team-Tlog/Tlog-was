package com.se.Tlog.domain.Search.repository.dto;

import lombok.Data;

@Data
public class KakaoLocalSearchRes {
    private Meta meta;
    @Data
    public static class Meta {
        private int total_count;
        private int pageable_count;
        private boolean is_end;
        private SameName same_name;
        @Data
        public static class SameName {
            private String[] region;
            private String keyword;
            private String selected_region;
        }
    }

    private Document[] documents;
    @Data
    public static class Document {
        private String id;
        private String place_name;
        private String category_name;
        private String category_group_code;
        private String category_group_name;
        private String phone;
        private String address_name;
        private String road_address_name;
        private String x;
        private String y;
        private String place_url;
        private String distance;
    }
}
