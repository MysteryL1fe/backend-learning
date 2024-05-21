package ru.khanin.dmitrii.scrapper.test.domain.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;
import ru.khanin.dmitrii.scrapper.DTO.entity.jpa.JpaLinkChat;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaLinkChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaLinkRepository;
import ru.khanin.dmitrii.scrapper.test.IntegrationEnvironment;

public class JpaLinkChatTest extends IntegrationEnvironment {
	@Autowired
	private JpaLinkChatRepository linkChatRepo;
	
	@Autowired
	private JpaLinkRepository linkRepo;
	
	@Autowired
	private JpaChatRepository chatRepo;
	
	@Transactional
	@Rollback
	@Test
	void addSingleLinkChatWithoutLinkAndChatTest() {
		// given
		LinkChat linkChat = new LinkChat();
		linkChat.setChatId(1);
		linkChat.setLinkId(1);
		
		assertAll(
				"Assert add single LinkChat without link and chat",
				() -> assertThrows(
						DataIntegrityViolationException.class,
						() -> linkChatRepo.add(linkChat)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addSingleLinkChatWithLinkAndChatTest() {
		// given
		Link link = new Link();
		link.setLink("https://github.com");
		Link savedLink = linkRepo.add(link);
		
		Chat chat = new Chat();
		chat.setChatId(1);
		chat.setUsername("username");
		Chat savedChat = chatRepo.add(chat);
		
		JpaLinkChat expected = new JpaLinkChat();
		expected.setLinkId(savedLink.getId());
		expected.setChatId(savedChat.getId());
		
		// when
		LinkChat result = linkChatRepo.add(expected);
		
		// then
		assertAll(
				"Assert add single LinkChat with link and chat",
				() -> assertThat(result).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result).isEqualTo(expected)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addLinkChatTwiceTest() {
		// given
		Link link = new Link();
		link.setLink("https://github.com");
		Link savedLink = linkRepo.add(link);
				
		Chat chat = new Chat();
		chat.setChatId(1);
		chat.setUsername("username");
		Chat savedChat = chatRepo.add(chat);
				
		JpaLinkChat expected = new JpaLinkChat();
		expected.setLinkId(savedLink.getId());
		expected.setChatId(savedChat.getId());
				
		// when
		LinkChat result = linkChatRepo.add(expected);
		
		// then
		assertAll(
				"Assert add single LinkChat with link and chat",
				() -> assertThat(result).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result).isEqualTo(expected),
				() -> assertThrows(
						DataIntegrityViolationException.class,
						() -> linkChatRepo.add(expected)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addMultipleLinkChatAndFindAllTest() {
		// given
		Link link1 = new Link();
		link1.setLink("https://github.com/1");
		Link savedLink1 = linkRepo.add(link1);
				
		Chat chat1 = new Chat();
		chat1.setChatId(1);
		chat1.setUsername("username");
		Chat savedChat1 = chatRepo.add(chat1);
				
		JpaLinkChat expected1 = new JpaLinkChat();
		expected1.setLinkId(savedLink1.getId());
		expected1.setChatId(savedChat1.getId());
		
		Link link2 = new Link();
		link2.setLink("https://github.com/2");
		Link savedLink2 = linkRepo.add(link2);
				
		Chat chat2 = new Chat();
		chat2.setChatId(2);
		chat2.setUsername("username");
		Chat savedChat2 = chatRepo.add(chat2);
				
		JpaLinkChat expected2 = new JpaLinkChat();
		expected2.setLinkId(savedLink2.getId());
		expected2.setChatId(savedChat2.getId());
		
		Iterable<JpaLinkChat> expectedIterable = List.of(expected1, expected2);
		
		// when
		LinkChat result1 = linkChatRepo.add(expected1);
		LinkChat result2 = linkChatRepo.add(expected2);
		Iterable<JpaLinkChat> resultIterable = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert add multiple LinkChat and findAll",
				() -> assertThat(result1).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result1).isEqualTo(expected1),
				() -> assertThat(result2).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result2).isEqualTo(expected2),
				() -> assertThat(resultIterable).containsExactlyElementsOf(expectedIterable)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void removeExistingLinkChatTest() {
		// given
		Link link = new Link();
		link.setLink("https://github.com");
		Link savedLink = linkRepo.add(link);
				
		Chat chat = new Chat();
		chat.setChatId(1);
		chat.setUsername("username");
		Chat savedChat = chatRepo.add(chat);
				
		LinkChat linkChatToDelete = new LinkChat();
		linkChatToDelete.setLinkId(savedLink.getId());
		linkChatToDelete.setChatId(savedChat.getId());
				
		// when
		linkChatRepo.add(linkChatToDelete);
		Iterable<JpaLinkChat> result1 = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove LinkChat db is not empty",
				() -> assertThat(result1).isNotEmpty()
		);
		
		// when
		Optional<LinkChat> removedLinkChat = linkChatRepo.remove(linkChatToDelete);
		Iterable<JpaLinkChat> result2 = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove LinkChat LinkChat removed",
				() -> assertThat(removedLinkChat).containsInstanceOf(JpaLinkChat.class),
				() -> assertThat(removedLinkChat.get()).isEqualTo(linkChatToDelete),
				() -> assertThat(result2).isEmpty()
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void removeNotExistingLinkChatTest() {
		// given
		long linkId = 1;
		long chatId = 1;
		LinkChat linkChatToDelete = new LinkChat();
		linkChatToDelete.setLinkId(linkId);
		linkChatToDelete.setChatId(chatId);
		
		// when
		Optional<LinkChat> removedLinkChat = linkChatRepo.remove(linkChatToDelete);
		
		// then
		assertAll(
				"Assert remove not existing LinkChat",
				() -> assertThat(removedLinkChat).isNotPresent()
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void removeAllLinkChatByChatIdTest() {
		// given
		Link link1 = new Link();
		link1.setLink("https://github.com/1");
		Link savedLink1 = linkRepo.add(link1);
				
		Chat chat1 = new Chat();
		chat1.setChatId(1);
		chat1.setUsername("username");
		Chat savedChat = chatRepo.add(chat1);
				
		JpaLinkChat expected1 = new JpaLinkChat();
		expected1.setLinkId(savedLink1.getId());
		expected1.setChatId(savedChat.getId());
		
		Link link2 = new Link();
		link2.setLink("https://github.com/2");
		Link savedLink2 = linkRepo.add(link2);
				
		JpaLinkChat expected2 = new JpaLinkChat();
		expected2.setLinkId(savedLink2.getId());
		expected2.setChatId(savedChat.getId());
		
		Iterable<JpaLinkChat> expectedIterable = List.of(expected1, expected2);
		
		// when
		LinkChat result1 = linkChatRepo.add(expected1);
		LinkChat result2 = linkChatRepo.add(expected2);
		Iterable<JpaLinkChat> resultIterable = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove all link_chat by chat_id db is not null",
				() -> assertThat(result1).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result1).isEqualTo(expected1),
				() -> assertThat(result2).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result2).isEqualTo(expected2),
				() -> assertThat(resultIterable).containsExactlyElementsOf(expectedIterable)
		);
		
		// when
		Iterable<? extends LinkChat> removedLinkChats = linkChatRepo.removeAllLinks(savedChat.getId());
		Iterable<JpaLinkChat> resultIterable2 = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove all link_chat by chat_id removed",
				() -> assertThat(removedLinkChats).hasOnlyElementsOfType(JpaLinkChat.class),
				() -> assertThat((Iterable<JpaLinkChat>) removedLinkChats).containsExactlyInAnyOrderElementsOf(expectedIterable),
				() -> assertThat(resultIterable2).isEmpty()
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void findAllByChatIdTest() {
		// given
		Link link1 = new Link();
		link1.setLink("https://github.com/1");
		Link savedLink1 = linkRepo.add(link1);

		Chat chat = new Chat();
		chat.setChatId(1);
		chat.setUsername("username");
		Chat savedChat = chatRepo.add(chat);
				
		JpaLinkChat expected1 = new JpaLinkChat();
		expected1.setLinkId(savedLink1.getId());
		expected1.setChatId(savedChat.getId());
		
		Link link2 = new Link();
		link2.setLink("https://github.com/2");
		Link savedLink2 = linkRepo.add(link2);
				
		JpaLinkChat expected2 = new JpaLinkChat();
		expected2.setLinkId(savedLink2.getId());
		expected2.setChatId(savedChat.getId());
		
		Iterable<JpaLinkChat> expectedIterable = List.of(expected1, expected2);
		
		// when
		LinkChat result1 = linkChatRepo.add(expected1);
		LinkChat result2 = linkChatRepo.add(expected2);
		Iterable<? extends LinkChat> resultIterable = linkChatRepo.findAllByChatId(savedChat.getId());
		
		// then
		assertAll(
				"Assert findAllByChatId",
				() -> assertThat(result1).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result1).isEqualTo(expected1),
				() -> assertThat(result2).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result2).isEqualTo(expected2),
				() -> assertThat((Iterable<LinkChat>) resultIterable).containsExactlyElementsOf(expectedIterable)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void findAllByLinkIdTest() {
		// given
		Link link = new Link();
		link.setLink("https://github.com/1");
		Link savedLink = linkRepo.add(link);
				
		Chat chat1 = new Chat();
		chat1.setChatId(1);
		chat1.setUsername("username");
		Chat savedChat1 = chatRepo.add(chat1);
				
		JpaLinkChat expected1 = new JpaLinkChat();
		expected1.setLinkId(savedLink.getId());
		expected1.setChatId(savedChat1.getId());
				
		Chat chat2 = new Chat();
		chat2.setChatId(2);
		chat2.setUsername("username");
		Chat savedChat2 = chatRepo.add(chat2);
				
		JpaLinkChat expected2 = new JpaLinkChat();
		expected2.setLinkId(savedLink.getId());
		expected2.setChatId(savedChat2.getId());
		
		Iterable<JpaLinkChat> expectedIterable = List.of(expected1, expected2);
		
		// when
		LinkChat result1 = linkChatRepo.add(expected1);
		LinkChat result2 = linkChatRepo.add(expected2);
		Iterable<? extends LinkChat> resultIterable = linkChatRepo.findAllByLinkId(savedLink.getId());
		
		// then
		assertAll(
				"Assert findAllByLinkId",
				() -> assertThat(result1).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result1).isEqualTo(expected1),
				() -> assertThat(result2).isInstanceOf(JpaLinkChat.class),
				() -> assertThat(result2).isEqualTo(expected2),
				() -> assertThat((Iterable<LinkChat>) resultIterable).containsExactlyElementsOf(expectedIterable)
		);
	}
}
