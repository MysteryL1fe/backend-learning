package ru.khanin.dmitrii.service;

import ru.khanin.dmitrii.DTO.entity.Chat;

public interface ChatService {
	Chat register(long chatId);
	Chat register(long chatId, String username);
	Chat unregister(long chatId);
}
