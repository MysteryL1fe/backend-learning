package ru.khanin.dmitrii.scrapper.domain;

import java.util.Optional;

import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;

public interface ChatRepository {
	Chat add(Chat chat);
	Optional<Chat> findById(long id);
	Optional<Chat> findByChatId(long chatId);
	Iterable<? extends Chat> findAll();
	Optional<Chat> remove(long chatId);
}