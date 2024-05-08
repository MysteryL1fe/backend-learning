package ru.khanin.dmitrii.domain;

import java.util.Optional;

import ru.khanin.dmitrii.DTO.entity.Chat;

public interface ChatRepository {
	Chat add(Chat chat);
	Optional<Chat> findByChatId(long chatId);
	Iterable<Chat> findAll();
	Optional<Chat> remove(long chatId);
}