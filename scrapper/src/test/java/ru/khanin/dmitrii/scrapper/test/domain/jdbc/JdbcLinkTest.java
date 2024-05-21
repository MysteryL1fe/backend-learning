package ru.khanin.dmitrii.scrapper.test.domain.jdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkRepository;
import ru.khanin.dmitrii.scrapper.test.IntegrationEnvironment;

public class JdbcLinkTest extends IntegrationEnvironment {
	@Autowired
	private JdbcLinkRepository linkRepo;
	
	@Transactional
	@Rollback
	@Test
	void addSingleLinkTest() {
		// given
		Link expected = new Link();
		expected.setLink("https://github.com/repos/123/123");
		
		// when
		Link result = linkRepo.add(expected);
		
		// then
		assertAll(
				"Assert add single link",
				() -> assertLinkEqualsWithoutId(result, expected)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addMultipleLinkAndFindAllTest() {
		// given
		Link expectedLink1 = new Link();
		expectedLink1.setLink("https://github.com/1.html");
		Link expectedLink2 = new Link();
		expectedLink2.setLink("https://github.com/2.html");
		Iterable<Link> expectedIterable = List.of(expectedLink1, expectedLink2);
		
		// when
		Link resultLink1 = linkRepo.add(expectedLink1);
		Link resultLink2 = linkRepo.add(expectedLink2);
		Iterable<Link> resultIterable = linkRepo.findAll();
		
		// then
		assertAll(
				"Assert add multipe links and find all",
				() -> assertLinkEqualsWithoutId(resultLink1, expectedLink1),
				() -> assertLinkEqualsWithoutId(resultLink2, expectedLink2),
				() -> assertIterableLinkEqualsByOnlyLink(resultIterable, expectedIterable)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addLinkTwiceTest() {
		// given
		Link expectedLink = new Link();
		expectedLink.setLink("https://github.com");
		expectedLink.setUpdateDate(OffsetDateTime.of(2024, 5, 6, 12, 0, 0, 0, ZoneOffset.UTC));
		
		// when
		Link result = linkRepo.add(expectedLink);
		
		// then
		assertAll(
				"Assert add link twice",
				() -> assertLinkEqualsWithoutId(result, expectedLink),
				() -> assertThrows(
						DuplicateKeyException.class,
						() -> linkRepo.add(expectedLink)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void addMultipleLinkWithSameLinkTest() {
		// given
		String link = "https://github.com";
		
		Link link1 = new Link();
		link1.setLink(link);
		link1.setUpdateDate(OffsetDateTime.of(2024, 5, 6, 15, 0, 0, 0, ZoneOffset.UTC));
		
		Link link2 = new Link();
		link2.setLink(link);
		link2.setUpdateDate(OffsetDateTime.of(2024, 5, 6, 12, 0, 0, 0, ZoneOffset.UTC));
		
		// when
		Link result = linkRepo.add(link1);
		
		// then
		assertAll(
				"Assert add multiple link with same link",
				() -> assertLinkEqualsWithoutId(result, link1),
				() -> assertThrows(
						DuplicateKeyException.class,
						() -> linkRepo.add(link2)
				)
		);
	}
	
	@Transactional
	@Rollback
	@Test
	void removeLinkTest() {
		// given
		String link = "https://github.com";
		Link linkToDelete = new Link();
		linkToDelete.setLink(link);
		
		// when
		linkRepo.add(linkToDelete);
		Iterable<Link> result1 = linkRepo.findAll();
		
		// then
		assertAll(
				"Assert remove link db is not empty",
				() -> assertThat(result1).isNotEmpty()
		);
		
		// when
		Optional<Link> removedLink = linkRepo.remove(link);
		Iterable<Link> result2 = linkRepo.findAll();
		
		// then
		assertAll(
				"Assert remove link link removed",
				() -> assertThat(removedLink).containsInstanceOf(Link.class),
				() -> assertLinkEqualsWithoutId(removedLink.get(), linkToDelete),
				() -> assertThat(result2).isEmpty());
	}
	
	private void assertLinkEqualsWithoutId(Link actual, Link expected) {
		assertThat(actual)
			.usingRecursiveComparison()
			.ignoringFields("id")
			.isEqualTo(expected);
	}
	
	private void assertIterableLinkEqualsByOnlyLink(Iterable<Link> actual, Iterable<Link> expected) {
		assertThat(actual)
			.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
			.containsExactlyInAnyOrderElementsOf(expected);
	}
}
