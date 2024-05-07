package ru.khanin.dmitrii;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LinkUpdaterScheduler {
	@Scheduled(fixedDelayString = "${app.scheduler.interval}")
	public void update() {
		log.info("update");
	}
}
