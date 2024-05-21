package ru.khanin.dmitrii.scrapper.test.domain.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcChatRepository;
import ru.khanin.dmitrii.scrapper.test.IntegrationEnvironment;

public class JdbcChatTest extends IntegrationEnvironment {
	@Autowired
	private JdbcChatRepository chatRepo;
	
	@Transactional
	@Rollback
	@Test
	void addSingleChatTest() {
		// given
		Chat expected = new Chat();
		expected.setChatId(10);
		expected.setUsername("abc");
		
		// when
		Chat result = chatRepo.add(expected);
		
		// then
		assertAll(
				"Assert add single chat",
				() -> assertChatEqualsWithoutId(result, expected)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addMultipleChatAndFindAllTest() {
		// given
		Chat expectedChat1 = new Chat();
		expectedChat1.setChatId(1);
		expectedChat1.setUsername("abc");
		
		Chat expectedChat2 = new Chat();
		expectedChat2.setChatId(2);
		expectedChat2.setUsername("xyz");
		
		Iterable<Chat> expectedIterable = List.of(expectedChat1, expectedChat2);
		
		// when
		Chat resultChat1 = chatRepo.add(expectedChat1);
		Chat resultChat2 = chatRepo.add(expectedChat2);
		Iterable<Chat> resultIterable = chatRepo.findAll();
		
		// then
		assertAll(
				"Assert add multiple chat and find all",
				() -> assertChatEqualsWithoutId(resultChat1, expectedChat1),
				() -> assertChatEqualsWithoutId(resultChat2, expectedChat2),
				() -> assertIterableChatEqualsWithoutId(resultIterable, expectedIterable)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addChatTwiceTest() {
		// given
		Chat expectedChat = new Chat();
		expectedChat.setChatId(9);
		expectedChat.setUsername("abc");
		
		// when
		Chat result = chatRepo.add(expectedChat);
		
		// then
		assertAll(
				"Assert add chat twice",
				() -> assertChatEqualsWithoutId(result, expectedChat),
				() -> assertThrows(
						DuplicateKeyException.class,
						() -> chatRepo.add(expectedChat)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addMultipleChatWithSameChatId() {
		// given
		long chatId = 999;
		
		Chat chat1 = new Chat();
		chat1.setChatId(chatId);
		chat1.setUsername("abc");
		
		Chat chat2 = new Chat();
		chat2.setChatId(chatId);
		chat2.setUsername("xyz");
		
		// when
		Chat result = chatRepo.add(chat1);
		
		// then
		assertAll(
				"Assert add multiple chat with same chat id",
				() -> assertChatEqualsWithoutId(result, chat1),
				() -> assertThrows(
						DuplicateKeyException.class,
						() -> chatRepo.add(chat2)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void removeChatTest() {
		// given
		long chatId = 5;
		Chat chatToDelete = new Chat();
		chatToDelete.setChatId(chatId);
		chatToDelete.setUsername("xyz");
		
		// when
		chatRepo.add(chatToDelete);
		Iterable<Chat> result1 = chatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove chat db is not empty",
				() -> assertThat(result1).isNotEmpty()
		);
		
		// when
		Optional<Chat> removedChat = chatRepo.remove(chatId);
		Iterable<Chat> result2 = chatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove chat chat removed",
				() -> assertThat(removedChat).containsInstanceOf(Chat.class),
				() -> assertChatEqualsWithoutId(removedChat.get(), chatToDelete),
				() -> assertThat(result2).isEmpty()
		);
	}
	
	private void assertChatEqualsWithoutId(Chat actual, Chat expected) {
		assertThat(actual)
			.usingRecursiveComparison()
			.ignoringFields("id")
			.isEqualTo(expected);
	}
	
	private void assertIterableChatEqualsWithoutId(Iterable<Chat> actual, Iterable<Chat> expected) {
		assertThat(actual)
			.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
			.containsExactlyInAnyOrderElementsOf(expected);
	}
}
