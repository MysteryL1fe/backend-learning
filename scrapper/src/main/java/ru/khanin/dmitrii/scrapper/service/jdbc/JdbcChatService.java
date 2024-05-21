package ru.khanin.dmitrii.scrapper.service.jdbc;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkChatRepository;
import ru.khanin.dmitrii.scrapper.service.ChatService;

@RequiredArgsConstructor
public class JdbcChatService implements ChatService {
	private final JdbcChatRepository chatRepo;
	private final JdbcLinkChatRepository linkChatRepo;

	@Override
	public Chat register(long chatId) {
		Chat chat = new Chat();
		chat.setChatId(chatId);
		chat.setUsername("user");
		return chatRepo.add(chat);
	}

	@Override
	public Chat register(long chatId, String username) {
		Chat chat = new Chat();
		chat.setChatId(chatId);
		chat.setUsername(username);
		return chatRepo.add(chat);
	}

	@Override
	public Chat unregister(long chatId) {
		linkChatRepo.removeAllLinks(chatId);
		return chatRepo.remove(chatId).orElse(null);
	}
	
	@Override
	public Chat findById(long id) {
		return chatRepo.findById(id).orElse(null);
	}
	
	@Override
	public Chat findByChatId(long chatId) {
		return chatRepo.findByChatId(chatId).orElse(null);
	}

}
