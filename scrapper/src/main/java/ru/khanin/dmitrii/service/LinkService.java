package ru.khanin.dmitrii.service;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;

import ru.khanin.dmitrii.DTO.entity.Link;

public interface LinkService {
	Link add(URI link);
	Link findById(long linkId);
	Link findByLink(URI link);
	Collection<Link> findAllByChatId(long chatId);
	Collection<Link> findAllWhereUpdateDateBeforeDate(OffsetDateTime dateTime);
	Link remove(URI link);
}
