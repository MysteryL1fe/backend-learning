package ru.khanin.dmitrii.scrapper.service.jdbc;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkRepository;
import ru.khanin.dmitrii.scrapper.service.LinkService;

@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
	private final JdbcLinkRepository linkRepo;
	private final JdbcLinkChatRepository linkChatRepo;
	
	@Override
	public Link add(URI link) {
		Link addLink = new Link();
		addLink.setLink(link.toString());
		return linkRepo.add(addLink);
	}
	
	@Override
	public Link updateUpdateDate(long linkId, OffsetDateTime updateDate) {
		Link link = linkRepo.findById(linkId).orElse(null);
		
		if (link == null || link.getUpdateDate().isAfter(updateDate)) return null;
		
		return linkRepo.updateUpdateDate(linkId, updateDate).orElse(null);
	}
	
	@Override
	public Link findById(long linkId) {
		return linkRepo.findById(linkId).orElse(null);
	}

	@Override
	public Link findByLink(URI link) {
		return linkRepo.findByLink(link.toString()).orElse(null);
	}
	
	@Override
	public Collection<Link> findAllByChatId(long chatId) {
		Iterable<LinkChat> found = linkChatRepo.findAllByChatId(chatId);
		Collection<Link> result = new ArrayList<>();
		found.forEach((e) -> result.add(linkRepo.findById(e.getLinkId()).get()));
		return result;
	}
	
	@Override
	public Collection<Link> findAllWhereUpdateDateBeforeDate(OffsetDateTime dateTime) {
		Iterable<Link> found = linkRepo.findAllByUpdateDateBefore(dateTime);
		Collection<Link> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public Link remove(URI link) {
		return linkRepo.remove(link.toString()).orElse(null);
	}

}
