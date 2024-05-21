package ru.khanin.dmitrii.scrapper.service.jpa;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaLinkChatRepository;
import ru.khanin.dmitrii.scrapper.service.ChatService;

@Service
@RequiredArgsConstructor
public class JpaChatService implements ChatService {
	private final JpaChatRepository chatRepo;
	private final JpaLinkChatRepository linkChatRepo;
	
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
