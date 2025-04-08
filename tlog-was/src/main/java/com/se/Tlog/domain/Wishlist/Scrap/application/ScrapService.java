package com.se.Tlog.domain.Wishlist.Scrap.application;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.se.Tlog.domain.ApplicationService;
import com.se.Tlog.domain.Travel.domain.Destination;
import com.se.Tlog.domain.Travel.repository.mongo.DestinationRepository;
import com.se.Tlog.domain.User.repository.jpa.UserRepository;
import com.se.Tlog.domain.Wishlist.Scrap.domain.Scrap;
import com.se.Tlog.domain.Wishlist.Scrap.repository.mongo.ScrapRepository;
import com.se.Tlog.global.exception.CustomException;
import com.se.Tlog.global.response.error.ErrorType;

@ApplicationService
public class ScrapService {
	@Autowired
	private ScrapRepository scrapRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private DestinationRepository destinationRepository;
	
	public List<Destination> getScrapList(UUID userId) {
		if (!userRepository.existsById(userId))
			throw new CustomException(ErrorType.USER_NOT_FOUND);
		
		Scrap scrap = scrapRepository.findByUserId(userId);
		if (scrap != null)
			return destinationRepository.findAllById(scrap.getDestinationIds());
		return new ArrayList<Destination>();
	}
	
	public void scrapDescription(UUID userId, String destinationId) {
		if (!userRepository.existsById(userId))
			throw new CustomException(ErrorType.USER_NOT_FOUND);
		if (!destinationRepository.existsById(destinationId))
			throw new CustomException(ErrorType.DESTINATION_NOT_FOUND);
		
		Scrap scrap = scrapRepository.findByUserId(userId);
		if (scrap == null)
			scrap = Scrap.create(userId);
		
		scrap.addDestination(destinationId);
		scrapRepository.save(scrap);
	}
	
	public void unscrapDescription(UUID userId, String destinationId) {
		if (!userRepository.existsById(userId))
			throw new CustomException(ErrorType.USER_NOT_FOUND);
		
		Scrap scrap = scrapRepository.findByUserId(userId);
		if (scrap == null)
			scrap = Scrap.create(userId);
		
		scrap.deleteDestination(destinationId);
		scrapRepository.save(scrap);
	}
}
