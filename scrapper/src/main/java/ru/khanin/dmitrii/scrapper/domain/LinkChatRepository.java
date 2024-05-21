package ru.khanin.dmitrii.scrapper.domain;

import java.util.Optional;

import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;

public interface LinkChatRepository {
	LinkChat add(LinkChat linkChat);
	Iterable<? extends LinkChat> findAll();
	Iterable<? extends LinkChat> findAllByChatId(long chatId);
	Iterable<? extends LinkChat> findAllByLinkId(long linkId);
	Optional<LinkChat> remove(LinkChat linkChat);
	Iterable<? extends LinkChat> removeAllLinks(long chatId);
}
