package ru.khanin.dmitrii.scrapper.domain;

import java.time.OffsetDateTime;
import java.util.Optional;

import ru.khanin.dmitrii.scrapper.DTO.entity.Link;

public interface LinkRepository {
	Link add(Link link);
	Optional<Link> updateUpdateDate(long linkId, OffsetDateTime updateDate);
	Optional<Link> findById(long id);
	Optional<Link> findByLink(String link);
	Iterable<? extends Link> findAll();
	Iterable<? extends Link> findAllByUpdateDateBefore(OffsetDateTime dateTime);
	Optional<Link> remove(String link);
}
