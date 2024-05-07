package ru.khanin.dmitrii.service;

import java.util.Collection;

import ru.khanin.dmitrii.DTO.entity.LinkChat;

public interface LinkChatService {
	LinkChat add(long linkId, long chatId);
	Collection<LinkChat> findAllByChatId(long chatId);
	LinkChat remove(long linkId, long chatID);
	Collection<LinkChat> removeAllLinks(long chatId);
}
