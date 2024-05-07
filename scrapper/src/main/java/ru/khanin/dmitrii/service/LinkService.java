package ru.khanin.dmitrii.service;

import java.net.URI;
import java.util.Collection;

import ru.khanin.dmitrii.DTO.entity.Link;

public interface LinkService {
	Link add(URI link);
	Link findById(long linkId);
	Link findByLink(URI link);
	Collection<Link> findAllByChatId(long chatId);
	Link remove(URI link);
}
