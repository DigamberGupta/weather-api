package com.digamber.weather.service;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheService.class);

	private final CacheManager cacheManager;

	@Autowired
	public CacheService(final CacheManager cacheManager) {
		this.cacheManager = cacheManager;
	}

	@Scheduled(fixedRateString = "${cache.refresh.fixedRate}")
	public void refreshCache() {
		LOGGER.info("cached manager refreshed");
		cacheManager.getCacheNames()
				.forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
	}

}