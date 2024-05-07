package ru.khanin.dmitrii.domain.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.DTO.entity.LinkChat;
import ru.khanin.dmitrii.domain.LinkChatRepository;

@Repository
@RequiredArgsConstructor
public class JdbcLinkChatRepository implements LinkChatRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<LinkChat> rowMapper = new DataClassRowMapper<>(LinkChat.class);
	
	@Override
	public LinkChat add(LinkChat linkChat) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO link_chat(link_id, chat_id) VALUES(:linkId, :chatId) RETURNING *",
				new BeanPropertySqlParameterSource(linkChat),
				rowMapper
		);
	}
	
	@Override
	public Iterable<LinkChat> findAll() {
		return jdbcTemplate.query("SELECT * FROM link_chat", rowMapper);
	}
	
	@Override
	public Iterable<LinkChat> findAllByChatId(long chatId) {
		return jdbcTemplate.query("SELECT * FROM link_chat WHERE chat_id=:chatId", Map.of("chatId", chatId), rowMapper);
	}
	
	@Override
	public Optional<LinkChat> remove(LinkChat linkChat) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM link_chat WHERE link_id=:linkId AND chat_id=:chatId RETURNING *",
								Map.of("linkId", linkChat.getLinkId(), "chatId", linkChat.getChatId()),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<LinkChat> removeAllLinks(long chatId) {
		return jdbcTemplate.query(
				"DELETE FROM link_chat WHERE chat_id=:chatId RETURNING *",
				Map.of("chatId", chatId),
				rowMapper
		);
	}
	
}
