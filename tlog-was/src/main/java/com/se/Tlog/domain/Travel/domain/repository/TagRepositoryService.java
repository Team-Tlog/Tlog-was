package com.se.Tlog.domain.Travel.domain.repository;

public interface TagRepositoryService {
	public boolean existById(String tagId);
	public boolean existByName(String tagName);
}
