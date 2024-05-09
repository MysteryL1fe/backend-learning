package ru.khanin.dmitrii.domain;

import java.time.OffsetDateTime;
import java.util.Optional;

import ru.khanin.dmitrii.DTO.entity.Link;

public interface LinkRepository {
	Link add(Link link);
	Optional<Link> updateUpdateDate(long linkId, OffsetDateTime updateDate);
	Optional<Link> findById(long id);
	Optional<Link> findByLink(String link);
	Iterable<Link> findAll();
	Iterable<Link> findAllWhereUpdateDateBeforeDate(OffsetDateTime dateTime);
	Optional<Link> remove(String link);
}
