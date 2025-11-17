package com.se.Tlog.domain.Search.repository.dto;

import java.util.List;

public record AiDestinationReq(
        List<Double> user_tags,
        List<Integer> region_codes,
        int n
) {

}
