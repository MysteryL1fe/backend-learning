package ru.khanin.dmitrii;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class LinkUpdaterScheduler {
	public static final Logger log = LoggerFactory.getLogger(Logger.class);
	
	@Scheduled(fixedDelayString = "${app.scheduler.interval}")
	public void update() {
		log.info("update");
	}
}
