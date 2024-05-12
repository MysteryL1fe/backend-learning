package ru.khanin.dmitrii.scrapper.domain.jdbc;

import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.domain.ChatRepository;

@Repository
@RequiredArgsConstructor
public class JdbcChatRepository implements ChatRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Chat> rowMapper = new DataClassRowMapper<>(Chat.class);
	
	@Override
	public Chat add(Chat chat) {
		return jdbcTemplate.queryForObject(
				"INSERT INTO chat(chat_id, username) VALUES (:chatId, :username) RETURNING *",
				new BeanPropertySqlParameterSource(chat),
				rowMapper
		);
	}
	
	@Override
	public Optional<Chat> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM chat WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Optional<Chat> findByChatId(long chatId) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM chat WHERE chat_id=:chatId",
								Map.of("chatId", chatId),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<Chat> findAll() {
		return jdbcTemplate.query("SELECT * FROM chat", rowMapper);
	}
	
	@Override
	public Optional<Chat> remove(long chatId) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM chat WHERE chat_id=:chatId RETURNING *",
								Map.of("chatId", chatId),
								rowMapper
						)
				)
		);
	}
}
