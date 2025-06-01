package com.se.Tlog.domain.Travel.domain.repository;

import java.util.List;

public interface CustomTagRepositoryService {
    void addCustomTag(String destinationId, List<String> tagNameList);
}
