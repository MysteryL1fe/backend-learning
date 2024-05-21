package ru.khanin.dmitrii.scrapper.domain.jpa;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.khanin.dmitrii.scrapper.DTO.entity.LinkChat;
import ru.khanin.dmitrii.scrapper.DTO.entity.jpa.JpaLinkChat;
import ru.khanin.dmitrii.scrapper.DTO.entity.jpa.JpaLinkChat.LinkChatId;
import ru.khanin.dmitrii.scrapper.domain.LinkChatRepository;

public interface JpaLinkChatRepository extends JpaRepository<JpaLinkChat, LinkChatId>, LinkChatRepository {
	@Override
	default LinkChat add(LinkChat linkChat) {
		LinkChatId linkChatId = new LinkChatId();
		linkChatId.setLinkId(linkChat.getLinkId());
		linkChatId.setChatId(linkChat.getChatId());
		if (findById(linkChatId).isPresent()) throw new DataIntegrityViolationException("LinkChat already exists");
		return saveAndFlush(linkChat instanceof JpaLinkChat ? (JpaLinkChat) linkChat : new JpaLinkChat(linkChat));
	}
	
	@Override
	default Optional<LinkChat> remove(LinkChat linkChat) {
		try {
			LinkChatId linkChatId = new LinkChatId();
			linkChatId.setLinkId(linkChat.getLinkId());
			linkChatId.setChatId(linkChat.getChatId());
			JpaLinkChat jpaLinkChat = getReferenceById(linkChatId);
			deleteById(linkChatId);
			flush();
			return Optional.of(jpaLinkChat);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	@Override
	default Iterable<? extends LinkChat> removeAllLinks(long chatId) {
		return deleteByLinkChatIdChatId(chatId);
	}
	
	@Override
	@Query("SELECT lc FROM JpaLinkChat lc WHERE lc.linkChatId.linkId = ?1")
	Iterable<? extends LinkChat> findAllByLinkId(long linkId);
	
	@Override
	@Query("SELECT lc FROM JpaLinkChat lc WHERE lc.linkChatId.chatId = ?1")
	Iterable<? extends LinkChat> findAllByChatId(long chatId);

	List<JpaLinkChat> deleteByLinkChatIdChatId(long chatId);
}
