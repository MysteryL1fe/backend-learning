package ru.khanin.dmitrii.scrapper.domain.jdbc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Optional;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.domain.LinkRepository;
import ru.khanin.dmitrii.scrapper.domain.jdbc.rowMapper.LinkRowMapper;

@Repository
@RequiredArgsConstructor
public class JdbcLinkRepository implements LinkRepository {
	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final RowMapper<Link> rowMapper = new LinkRowMapper();
	
	@Override
	public Link add(Link link) {
		SqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("link", link.getLink())
				.addValue("updateDate", link.getUpdateDate() == null ? null
						: link.getUpdateDate().withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime());
		return jdbcTemplate.queryForObject(
				"INSERT INTO link(link, update_date) VALUES(:link, :updateDate) RETURNING *",
				parameters,
				rowMapper
		);
	}
	
	@Override
	public Optional<Link> updateUpdateDate(long linkId, OffsetDateTime updateDate) {
		return Optional.ofNullable(DataAccessUtils.singleResult(
				jdbcTemplate.query(
						"UPDATE link SET update_date=:updateDate WHERE id=:id RETURNING *",
						Map.of(
								"id", linkId,
								"updateDate", updateDate.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime()
						),
						rowMapper
				)
			)
		);
	}
	
	@Override
	public Optional<Link> findById(long id) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM link WHERE id=:id",
								Map.of("id", id),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Optional<Link> findByLink(String link) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"SELECT * FROM link WHERE link=:link",
								Map.of("link", link),
								rowMapper
						)
				)
		);
	}
	
	@Override
	public Iterable<Link> findAll() {
		return jdbcTemplate.query("SELECT * FROM link", rowMapper);
	}
	
	@Override
	public Iterable<Link> findAllByUpdateDateBefore(OffsetDateTime dateTime) {
		return jdbcTemplate.query(
				"SELECT * FROM link WHERE update_date=NULL or update_date<:updateDate",
				Map.of("updateDate", dateTime.withOffsetSameInstant(ZoneOffset.UTC).toLocalDateTime()),
				rowMapper
		);
	}
	
	@Override
	public Optional<Link> remove(String link) {
		return Optional.ofNullable(
				DataAccessUtils.singleResult(
						jdbcTemplate.query(
								"DELETE FROM link WHERE link=:link RETURNING *",
								Map.of("link", link),
								rowMapper
						)
				)
		);
	}
}
