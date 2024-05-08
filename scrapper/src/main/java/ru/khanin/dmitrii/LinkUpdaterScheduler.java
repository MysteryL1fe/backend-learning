package ru.khanin.dmitrii;

import java.time.OffsetDateTime;
import java.util.Collection;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.DTO.entity.Link;
import ru.khanin.dmitrii.service.LinkService;

@Service
@Slf4j
@RequiredArgsConstructor
public class LinkUpdaterScheduler {
	private final LinkService linkService;
	
	@Scheduled(fixedDelayString = "${app.scheduler.interval}")
	public void update() {
		OffsetDateTime tooLateDateTime = OffsetDateTime.now().minusDays(1);
		Collection<Link> needToUpdateLink = linkService.findAllWhereUpdateDateBeforeDate(tooLateDateTime);
		needToUpdateLink.forEach(this::updateLink);
		log.info("update");
	}
	
	private void updateLink(Link link) {
		
	}
}
