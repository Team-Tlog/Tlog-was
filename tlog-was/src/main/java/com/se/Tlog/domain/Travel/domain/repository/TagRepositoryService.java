package com.se.Tlog.domain.Travel.domain.repository;

import java.util.List;
import java.util.Set;

public interface TagRepositoryService {
	/**
	 * 주어진 id 중 실제 존재하는 id만을 Set으로 반환합니다.
	 * @param tagIds
	 * @return
	 */
	Set<String> getExistSet(List<String> tagIds);
}
