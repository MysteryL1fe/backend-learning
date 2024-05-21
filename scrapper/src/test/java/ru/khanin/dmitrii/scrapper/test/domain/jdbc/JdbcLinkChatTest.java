package ru.khanin.dmitrii.scrapper.test.domain.jdbc;

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
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkRepository;
import ru.khanin.dmitrii.scrapper.test.IntegrationEnvironment;

public class JdbcLinkChatTest extends IntegrationEnvironment {
	@Autowired
	private JdbcLinkChatRepository linkChatRepo;
	
	@Autowired
	private JdbcLinkRepository linkRepo;
	
	@Autowired
	private JdbcChatRepository chatRepo;
	
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
		
		LinkChat expected = new LinkChat();
		expected.setLinkId(savedLink.getId());
		expected.setChatId(savedChat.getId());
		
		// when
		LinkChat result = linkChatRepo.add(expected);
		
		// then
		assertAll(
				"Assert add single LinkChat with link and chat",
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
				
		LinkChat expected = new LinkChat();
		expected.setLinkId(savedLink.getId());
		expected.setChatId(savedChat.getId());
				
		// when
		LinkChat result = linkChatRepo.add(expected);
		
		// then
		assertAll(
				"Assert add single LinkChat with link and chat",
				() -> assertThat(result).isEqualTo(expected),
				() -> assertThrows(
						DuplicateKeyException.class,
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
				
		LinkChat expected1 = new LinkChat();
		expected1.setLinkId(savedLink1.getId());
		expected1.setChatId(savedChat1.getId());
		
		Link link2 = new Link();
		link2.setLink("https://github.com/2");
		Link savedLink2 = linkRepo.add(link2);
				
		Chat chat2 = new Chat();
		chat2.setChatId(2);
		chat2.setUsername("username");
		Chat savedChat2 = chatRepo.add(chat2);
				
		LinkChat expected2 = new LinkChat();
		expected2.setLinkId(savedLink2.getId());
		expected2.setChatId(savedChat2.getId());
		
		Iterable<LinkChat> expectedIterable = List.of(expected1, expected2);
		
		// when
		LinkChat result1 = linkChatRepo.add(expected1);
		LinkChat result2 = linkChatRepo.add(expected2);
		Iterable<LinkChat> resultIterable = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert add multiple LinkChat and findAll",
				() -> assertThat(result1).isEqualTo(expected1),
				() -> assertThat(result2).isEqualTo(expected2),
				() -> assertThat(resultIterable).containsExactlyElementsOf(expectedIterable)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void removeLinkChatTest() {
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
		Iterable<LinkChat> result1 = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove LinkChat db is not empty",
				() -> assertThat(result1).isNotEmpty()
		);
		
		// when
		Optional<LinkChat> removedLinkChat = linkChatRepo.remove(linkChatToDelete);
		Iterable<LinkChat> result2 = linkChatRepo.findAll();
		
		// then
		assertAll(
				"Assert remove LinkChat LinkChat removed",
				() -> assertThat(removedLinkChat).containsInstanceOf(LinkChat.class),
				() -> assertThat(removedLinkChat.get()).isEqualTo(linkChatToDelete),
				() -> assertThat(result2).isEmpty()
		);
	}
}
