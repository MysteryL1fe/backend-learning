package ru.khanin.dmitrii.service.jdbc;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.DTO.entity.Chat;
import ru.khanin.dmitrii.domain.jdbc.JdbcChatRepository;
import ru.khanin.dmitrii.domain.jdbc.JdbcLinkChatRepository;
import ru.khanin.dmitrii.service.ChatService;

@Service
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
	public Chat findByChatId(long chatId) {
		return chatRepo.findByChatId(chatId).orElse(null);
	}

}
