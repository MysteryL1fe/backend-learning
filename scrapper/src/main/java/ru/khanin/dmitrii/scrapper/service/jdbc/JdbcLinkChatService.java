package ru.khanin.dmitrii.scrapper.service.jdbc;

import java.util.ArrayList;
import java.util.Collection;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkChatRepository;
import ru.khanin.dmitrii.scrapper.service.LinkChatService;

@RequiredArgsConstructor
public class JdbcLinkChatService implements LinkChatService {
	private final JdbcLinkChatRepository linkChatRepo;

	@Override
	public LinkChat add(long linkId, long chatId) {
		LinkChat linkChat = new LinkChat();
		linkChat.setLinkId(linkId);
		linkChat.setChatId(chatId);
		return linkChatRepo.add(linkChat);
	}
	
	@Override
	public Collection<LinkChat> findAllByChatId(long chatId) {
		Iterable<LinkChat> found = linkChatRepo.findAllByChatId(chatId);
		Collection<LinkChat> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}
	
	@Override
	public Collection<LinkChat> findAllByLinkId(long linkId) {
		Iterable<LinkChat> found = linkChatRepo.findAllByLinkId(linkId);
		Collection<LinkChat> result = new ArrayList<>();
		found.forEach(result::add);
		return result;
	}

	@Override
	public LinkChat remove(long linkId, long chatID) {
		LinkChat linkChat = new LinkChat();
		linkChat.setLinkId(linkId);
		linkChat.setChatId(chatID);
		return linkChatRepo.remove(linkChat).orElse(null);
	}
	
	@Override
	public Collection<LinkChat> removeAllLinks(long chatId) {
		Iterable<LinkChat> removed = linkChatRepo.removeAllLinks(chatId);
		Collection<LinkChat> result = new ArrayList<>();
		removed.forEach(result::add);
		return result;
	}

}
