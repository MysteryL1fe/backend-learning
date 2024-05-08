package ru.khanin.dmitrii.domain;

import java.util.Optional;

import ru.khanin.dmitrii.DTO.entity.LinkChat;

public interface LinkChatRepository {
	LinkChat add(LinkChat linkChat);
	Iterable<LinkChat> findAll();
	Iterable<LinkChat> findAllByChatId(long chatId);
	Iterable<LinkChat> findAllByLinkId(long linkId);
	Optional<LinkChat> remove(LinkChat linkChat);
	Iterable<LinkChat> removeAllLinks(long chatId);
}
