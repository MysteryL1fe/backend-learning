package ru.khanin.dmitrii.domain;

import java.util.Optional;

import ru.khanin.dmitrii.DTO.entity.Link;

public interface LinkRepository {
	Link add(Link link);
	Optional<Link> findById(long id);
	Optional<Link> findByLink(String link);
	Iterable<Link> findAll();
	Optional<Link> remove(String link);
}
