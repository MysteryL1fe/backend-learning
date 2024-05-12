package ru.khanin.dmitrii.scrapper.domain.jdbc.rowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.springframework.jdbc.core.RowMapper;

import ru.khanin.dmitrii.scrapper.DTO.entity.Link;

public class LinkRowMapper implements RowMapper<Link> {
	@Override
	public Link mapRow(ResultSet rs, int rowNum) throws SQLException {
		Link res = new Link();
		res.setId(rs.getLong("id"));
		res.setLink(rs.getString("link"));
		Timestamp timestamp = rs.getTimestamp("update_date");
		res.setUpdateDate(timestamp == null ? null
				: OffsetDateTime.of(rs.getTimestamp("update_date").toLocalDateTime(), ZoneOffset.UTC));
		return res;
	}
}
