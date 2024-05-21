package ru.khanin.dmitrii.scrapper.domain.jpa;

import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.DTO.entity.jpa.JpaChat;
import ru.khanin.dmitrii.scrapper.domain.ChatRepository;

public interface JpaChatRepository extends JpaRepository<JpaChat, Long>, ChatRepository {
	@Override
	default Chat add(Chat chat) {
		if (findByChatId(chat.getChatId()).isPresent()) throw new DataIntegrityViolationException("Chat already exists");
		return saveAndFlush(chat instanceof JpaChat ? (JpaChat) chat : new JpaChat(chat));
	}
	
	@Override
	default Optional<Chat> findByChatId(long chatId) {
		try {
			return Optional.of(getReferenceByChatId(chatId));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	@Override
	default Optional<Chat> remove(long chatId) {
		try {
			JpaChat chat = getReferenceByChatId(chatId);
			deleteById(chat.getId());
			flush();
			return Optional.of(chat);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	JpaChat getReferenceByChatId(long chatID);
}
