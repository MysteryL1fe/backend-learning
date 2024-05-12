package ru.khanin.dmitrii.scrapper.service;

import java.util.Collection;

import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;

public interface LinkChatService {
	LinkChat add(long linkId, long chatId);
	Collection<LinkChat> findAllByChatId(long chatId);
	Collection<LinkChat> findAllByLinkId(long linkId);
	LinkChat remove(long linkId, long chatID);
	Collection<LinkChat> removeAllLinks(long chatId);
}
