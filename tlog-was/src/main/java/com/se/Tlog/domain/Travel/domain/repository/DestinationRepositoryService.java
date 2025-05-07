package com.se.Tlog.domain.Travel.domain.repository;

import com.se.Tlog.domain.Travel.domain.TagInfo;

import java.util.List;

public interface DestinationRepositoryService {
	boolean exist(String name);
	void addFixedTags(String id, List<TagInfo> fixedTags);
}
