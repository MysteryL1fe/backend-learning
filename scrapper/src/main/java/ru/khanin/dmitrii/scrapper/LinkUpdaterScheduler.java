package ru.khanin.dmitrii.scrapper;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.link.parser.content.GitHubContent;
import ru.khanin.dmitrii.link.parser.content.StackOverflowContent;
import ru.khanin.dmitrii.link.parser.parser.GitHubParser;
import ru.khanin.dmitrii.link.parser.parser.ParserHandler;
import ru.khanin.dmitrii.link.parser.parser.StackOverflowParser;
import ru.khanin.dmitrii.scrapper.DTO.GitHub.GitHubRepositoryEvent;
import ru.khanin.dmitrii.scrapper.DTO.StackOverflow.StackOverflowQuestionItem;
import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;
import ru.khanin.dmitrii.scrapper.client.BotClient;
import ru.khanin.dmitrii.scrapper.client.GitHubClient;
import ru.khanin.dmitrii.scrapper.client.StackOverflowClient;
import ru.khanin.dmitrii.scrapper.service.ChatService;
import ru.khanin.dmitrii.scrapper.service.LinkChatService;
import ru.khanin.dmitrii.scrapper.service.LinkService;
import ru.khanin.dmitrii.scrapper.service.jpa.JpaChatService;
import ru.khanin.dmitrii.scrapper.service.jpa.JpaLinkChatService;
import ru.khanin.dmitrii.scrapper.service.jpa.JpaLinkService;

@Service
@Slf4j
public class LinkUpdaterScheduler {
	private final LinkService linkService;
	private final LinkChatService linkChatService;
	private final ChatService chatService;
	private final ParserHandler parser;
	private final GitHubClient gitHubClient = new GitHubClient(new URI("https://api.github.com"));
	private final StackOverflowClient stackOverflowClient = new StackOverflowClient(new URI("https://api.stackexchange.com/2.3"));
	private final BotClient botClient = new BotClient(new URI("http://localhost:8080"));
	
	public LinkUpdaterScheduler(JpaLinkService linkService, JpaLinkChatService linkChatService, JpaChatService chatService) throws URISyntaxException {
		this.linkService = linkService;
		this.linkChatService = linkChatService;
		this.chatService = chatService;
		
		this.parser = new GitHubParser();
		this.parser.setNextParser(new StackOverflowParser());
	}
	
	@Scheduled(fixedDelayString = "${app.scheduler.interval}")
	public void update() {
		OffsetDateTime tooLateDateTime = OffsetDateTime.now().minusDays(1);
		Collection<Link> needToUpdateLink = linkService.findAllWhereUpdateDateBeforeDate(tooLateDateTime);
		needToUpdateLink.forEach(this::updateLink);
		log.info("update");
	}
	
	private void updateLink(Link link) {
		Object parsedLink = this.parser.parse(link.getLink());
		
		if (parsedLink == null) {
			log.info("Unknown link parsed: " + link.getLink());
			return;
		}
		
		try {
			if (parsedLink instanceof GitHubContent) {
				GitHubContent content = (GitHubContent) parsedLink;
				
				List<GitHubRepositoryEvent> events = gitHubClient.getRepositoryEvents(content.owner(), content.repository(), link.getUpdateDate());
				
				for (GitHubRepositoryEvent event : events) { 
					Collection<LinkChat> linkChats = linkChatService.findAllByLinkId(link.getId());
					List<Long> tgChatIds = new ArrayList<>();
					linkChats.forEach((e) -> tgChatIds.add(chatService.findById(e.getChatId()).getChatId()));
					
					botClient.linkUpdate(link.getId(), new URI(link.getLink()), event.type(), tgChatIds);
					
					linkService.updateUpdateDate(link.getId(), event.created_at());
				}
			} else if (parsedLink instanceof StackOverflowContent) {
				StackOverflowContent content = (StackOverflowContent) parsedLink;
				
				List<StackOverflowQuestionItem> items = stackOverflowClient.getQuestionItems(content.questionId(), link.getUpdateDate());
				
				for (StackOverflowQuestionItem item : items) {
					Collection<LinkChat> linkChats = linkChatService.findAllByLinkId(link.getId());
					List<Long> tgChatIds = new ArrayList<>();
					linkChats.forEach((e) -> tgChatIds.add(chatService.findById(e.getChatId()).getChatId()));
					
					botClient.linkUpdate(link.getId(), new URI(link.getLink()), item.timeline_type(), tgChatIds);
					
					linkService.updateUpdateDate(link.getId(), item.creation_date().toInstant().atOffset(ZoneOffset.UTC));
				}
			} else {
				log.info("Can't update parsed link: " + link.getLink());
			}
		} catch (URISyntaxException e) {
			log.error("URI syntax error in update");
		}
	}
}
