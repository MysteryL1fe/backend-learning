package ru.khanin.dmitrii.scrapper.service;

import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;

public interface ChatService {
	Chat register(long chatId);
	Chat register(long chatId, String username);
	Chat unregister(long chatId);
	Chat findById(long id);
	Chat findByChatId(long chatId);
}
