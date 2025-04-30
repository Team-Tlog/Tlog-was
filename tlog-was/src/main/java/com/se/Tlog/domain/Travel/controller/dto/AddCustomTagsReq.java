package com.se.Tlog.domain.Travel.controller.dto;

import java.util.List;

public record AddCustomTagsReq(
        List<String> tagNameList
) {
}
