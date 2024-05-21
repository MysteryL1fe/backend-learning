package ru.khanin.dmitrii.scrapper.domain.jpa;

import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.DTO.entity.jpa.JpaLink;
import ru.khanin.dmitrii.scrapper.domain.LinkRepository;

public interface JpaLinkRepository extends JpaRepository<JpaLink, Long>, LinkRepository {
	@Override
	default Link add(Link link) {
		if (findByLink(link.getLink()).isPresent()) throw new DataIntegrityViolationException("Link already exists");
		return saveAndFlush(link instanceof JpaLink ? (JpaLink) link : new JpaLink(link));
	}
	
	@Override
	default Optional<Link> updateUpdateDate(long linkId, OffsetDateTime updateDate) {
		Optional<Link> foundLink = findById(linkId);
		if (foundLink.isEmpty() || !(foundLink.get() instanceof JpaLink)) return Optional.empty();
		JpaLink link = (JpaLink) foundLink.get();
		link.setUpdateDate(updateDate);
		flush();
		return Optional.of(link);
	}
	
	@Override
	default Optional<Link> findByLink(String link) {
		try {
			return Optional.of(getReferenceByLink(link));
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	@Override
	default Optional<Link> remove(String link) {
		try {
			JpaLink jpaLink = getReferenceByLink(link);
			deleteById(jpaLink.getId());
			flush();
			return Optional.of(jpaLink);
		} catch (Exception e) {
			return Optional.empty();
		}
	}
	
	JpaLink getReferenceByLink(String link);
}
