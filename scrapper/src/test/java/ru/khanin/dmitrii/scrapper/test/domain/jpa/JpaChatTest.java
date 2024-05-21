package ru.khanin.dmitrii.scrapper.test.domain.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.DTO.entity.jpa.JpaChat;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaChatRepository;
import ru.khanin.dmitrii.scrapper.test.IntegrationEnvironment;

public class JpaChatTest extends IntegrationEnvironment {
	@Autowired
	private JpaChatRepository chatRepo;
	
	@Transactional
	@Rollback
	@Test
	public void addSingleChatTest() {
		// given
		Chat initial = new Chat();
		initial.setChatId(10);
		initial.setUsername("abc");
		JpaChat expected = new JpaChat(initial);
		
		// when
		Chat result = chatRepo.add(expected);
		
		// then
		assertAll(
				"Assert add single chat",
				() -> assertThat(result).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) result, expected)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void addMultipleChatAndFindAllTest() {
		// given
		Chat initialChat1 = new Chat();
		initialChat1.setChatId(1);
		initialChat1.setUsername("abc");
		JpaChat expectedChat1 = new JpaChat(initialChat1);
		
		Chat initialChat2 = new Chat();
		initialChat2.setChatId(2);
		initialChat2.setUsername("xyz");
		JpaChat expectedChat2 = new JpaChat(initialChat2);
		
		Iterable<JpaChat> expectedIterable = List.of(expectedChat1, expectedChat2);
		
		// when
		Chat resultChat1 = chatRepo.add(expectedChat1);
		Chat resultChat2 = chatRepo.add(expectedChat2);
		List<JpaChat> resultIterable = new ArrayList<>();
		chatRepo.findAll().forEach((e) -> resultIterable.add(e));
		
		
		// then
		assertAll(
				"Assert add multiple chat and find all",
				() -> assertThat(resultIterable).hasOnlyElementsOfType(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) resultChat1, expectedChat1),
				() -> assertJpaChatEqualsWithoutId((JpaChat) resultChat2, expectedChat2),
				() -> assertIterableJpaChatEqualsWithoutId(resultIterable, expectedIterable)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void addChatTwiceTest() {
		// given
		Chat initialChat = new Chat();
		initialChat.setChatId(9);
		initialChat.setUsername("abc");
		JpaChat expectedChat = new JpaChat(initialChat);
		
		// when
		Chat result1 = chatRepo.add(initialChat);
		
		// then
		assertAll(
				"Assert add chat twice",
				() -> assertThat(expectedChat).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) result1, expectedChat),
				() -> assertThrows(
						DataIntegrityViolationException.class,
						() -> chatRepo.add(initialChat)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void addMultipleChatWithSameChatIdTest() {
		// given
		long chatId = 999;
		
		Chat chat1 = new Chat();
		chat1.setChatId(chatId);
		chat1.setUsername("abc");
		JpaChat expectedChat1 = new JpaChat(chat1);
		
		Chat chat2 = new Chat();
		chat2.setChatId(chatId);
		chat2.setUsername("xyz");
		
		// when
		Chat result = chatRepo.add(chat1);
		
		// then
		assertAll(
				"Assert add multiple chat with same chat id",
				() -> assertThat(result).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) result, expectedChat1),
				() -> assertThrows(
						DataIntegrityViolationException.class,
						() -> chatRepo.add(chat2)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void removeChatTest() {
		// given
		long chatId = 5;
		Chat chatToDelete = new Chat();
		chatToDelete.setChatId(chatId);
		chatToDelete.setUsername("xyz");
		JpaChat expectedDelete = new JpaChat(chatToDelete);
		
		// when
		chatRepo.add(chatToDelete);
		Iterable<JpaChat> result1 = chatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove chat db is not empty",
				() -> assertThat(result1).isNotEmpty()
		);
		
		// when
		Optional<Chat> removedChat = chatRepo.remove(chatId);
		Iterable<JpaChat> result2 = chatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove chat chat removed",
				() -> assertThat(removedChat).isPresent(),
				() -> assertThat(removedChat.get()).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) removedChat.get(), expectedDelete),
				() -> assertThat(result2).isEmpty()
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void removeNotExistingChatTest() {
		// given
		long chatId = 5;
		
		// when
		Optional<Chat> removedChat = chatRepo.remove(chatId);
		
		// then
		assertAll(
				"Assert remove not existing chat",
				() -> assertThat(removedChat).isNotPresent()
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void findByIdExistingChatTest() {
		// given
		Chat chat1 = new Chat();
		chat1.setChatId(1);
		chat1.setUsername("user");
		
		Chat chat2 = new Chat();
		chat2.setChatId(2);
		chat2.setUsername("user");
		
		// when
		Chat savedChat1 = chatRepo.add(chat1);
		Chat savedChat2 = chatRepo.add(chat2);
		
		// then
		assertAll(
				"Assert save chats in find by id existing chat",
				() -> assertThat(savedChat1).isNotNull().isInstanceOf(JpaChat.class),
				() -> assertThat(savedChat2).isNotNull().isInstanceOf(JpaChat.class)
		);
		
		// given
		JpaChat expectedChat1 = (JpaChat) savedChat1;
		JpaChat expectedChat2 = (JpaChat) savedChat2;
		
		// when
		Optional<Chat> resultChat1 = chatRepo.findById(expectedChat1.getId());
		Optional<Chat> resultChat2 = chatRepo.findById(expectedChat2.getId());
		
		assertAll(
				"Assert find by id existing chat",
				() -> assertThat(resultChat1).isPresent(),
				() -> assertThat(resultChat1.get()).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) resultChat1.get(), expectedChat1),
				() -> assertThat(resultChat2).isPresent(),
				() -> assertThat(resultChat2.get()).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) resultChat2.get(), expectedChat2)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void findByIdNotExistingChatTest() {
		// given
		
		// when
		Optional<Chat> resultChat1 = chatRepo.findById(1);
		
		assertAll(
				"Assert find by id not existing chat",
				() -> assertThat(resultChat1).isNotPresent()
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void findByChatIdExistingChatTest() {
		// given
		long chatId1 = 1;
		Chat chat1 = new Chat();
		chat1.setChatId(chatId1);
		chat1.setUsername("user");
		
		long chatId2 = 2;
		Chat chat2 = new Chat();
		chat2.setChatId(chatId2);
		chat2.setUsername("user");
		
		// when
		Chat savedChat1 = chatRepo.add(chat1);
		Chat savedChat2 = chatRepo.add(chat2);
		
		// then
		assertAll(
				"Assert save chats in find by chat_id existing chat",
				() -> assertThat(savedChat1).isNotNull().isInstanceOf(JpaChat.class),
				() -> assertThat(savedChat2).isNotNull().isInstanceOf(JpaChat.class)
		);
		
		// given
		JpaChat expectedChat1 = (JpaChat) savedChat1;
		JpaChat expectedChat2 = (JpaChat) savedChat2;
		
		// when
		Optional<Chat> resultChat1 = chatRepo.findByChatId(chatId1);
		Optional<Chat> resultChat2 = chatRepo.findByChatId(chatId2);
		
		assertAll(
				"Assert find by chat_id existing chat",
				() -> assertThat(resultChat1).isPresent(),
				() -> assertThat(resultChat1.get()).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) resultChat1.get(), expectedChat1),
				() -> assertThat(resultChat2).isPresent(),
				() -> assertThat(resultChat2.get()).isInstanceOf(JpaChat.class),
				() -> assertJpaChatEqualsWithoutId((JpaChat) resultChat2.get(), expectedChat2)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	public void findByChatIdNotExistingChatTest() {
		// given
		
		// when
		Optional<Chat> resultChat1 = chatRepo.findByChatId(1);
		
		assertAll(
				"Assert find by chat_id not existing chat",
				() -> assertThat(resultChat1).isNotPresent()
		);
	}
	
	private void assertJpaChatEqualsWithoutId(JpaChat actual, JpaChat expected) {
		assertThat(actual)
			.usingRecursiveComparison()
			.ignoringFields("id")
			.isEqualTo(expected);
	}
	
	private void assertIterableJpaChatEqualsWithoutId(Iterable<JpaChat> actual, Iterable<JpaChat> expected) {
		assertThat(actual)
			.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
			.containsExactlyInAnyOrderElementsOf(expected);
	}
}
