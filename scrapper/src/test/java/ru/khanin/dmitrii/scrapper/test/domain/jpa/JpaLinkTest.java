package ru.khanin.dmitrii.scrapper.test.domain.jpa;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.DTO.entity.jpa.JpaLink;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaLinkRepository;
import ru.khanin.dmitrii.scrapper.test.IntegrationEnvironment;

public class JpaLinkTest extends IntegrationEnvironment {
	@Autowired
	private JpaLinkRepository linkRepo;

	@Transactional
	@Rollback
	@Test
	public void addSingleLinkTest() {
		// given
		Link initial = new Link();
		initial.setLink("https://github.com/repos/123/123");
		JpaLink expected = new JpaLink(initial);

		// when
		Link result = linkRepo.add(initial);

		// then
		assertAll("Assert add single link", () -> assertThat(result).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) result, expected));
	}

	@Transactional
	@Rollback
	@Test
	public void addMultipleLinkAndFindAllTest() {
		// given
		Link initialLink1 = new Link();
		initialLink1.setLink("https://github.com/1.html");
		JpaLink expectedLink1 = new JpaLink(initialLink1);

		Link initialLink2 = new Link();
		initialLink2.setLink("https://github.com/2.html");
		JpaLink expectedLink2 = new JpaLink(initialLink2);

		Iterable<JpaLink> expectedIterable = List.of(expectedLink1, expectedLink2);

		// when
		Link resultLink1 = linkRepo.add(expectedLink1);
		Link resultLink2 = linkRepo.add(expectedLink2);
		Iterable<JpaLink> resultIterable = linkRepo.findAll();

		// then
		assertAll("Assert add multipe links and find all", () -> assertThat(resultLink1).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) resultLink1, expectedLink1),
				() -> assertThat(resultLink2).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) resultLink2, expectedLink2),
				() -> assertIterableJpaLinkEqualsWithoutId(resultIterable, expectedIterable));
	}

	@Transactional
	@Rollback
	@Test
	public void addLinkTwiceTest() {
		// given
		Link initialLink = new Link();
		initialLink.setLink("https://github.com");
		initialLink.setUpdateDate(OffsetDateTime.of(2024, 5, 6, 12, 0, 0, 0, ZoneOffset.UTC));
		JpaLink expectedLink = new JpaLink(initialLink);

		// when
		Link result = linkRepo.add(expectedLink);

		// then
		assertAll(
				"Assert add link twice",
				() -> assertThat(result).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) result, expectedLink),
				() -> assertThrows(
						DataIntegrityViolationException.class,
						() -> linkRepo.add(expectedLink)
				)
		);
	}

	@Transactional
	@Rollback
	@Test
	public void addMultipleLinkWithSameLinkTest() {
		// given
		String link = "https://github.com";

		Link link1 = new Link();
		link1.setLink(link);
		link1.setUpdateDate(OffsetDateTime.of(2024, 5, 6, 15, 0, 0, 0, ZoneOffset.UTC));
		JpaLink expectedLink1 = new JpaLink(link1);

		Link link2 = new Link();
		link2.setLink(link);
		link2.setUpdateDate(OffsetDateTime.of(2024, 5, 6, 12, 0, 0, 0, ZoneOffset.UTC));

		// when
		Link result = linkRepo.add(link1);

		// then
		assertAll(
				"Assert add multiple link with same link",
				() -> assertThat(result).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) result, expectedLink1),
				() -> assertThrows(
						DataIntegrityViolationException.class,
						() -> linkRepo.add(link2)
				)
		);
	}

	@Transactional
	@Rollback
	@Test
	public void removeLinkTest() {
		// given
		String link = "https://github.com";
		Link linkToDelete = new Link();
		linkToDelete.setLink(link);
		JpaLink expectedDelete = new JpaLink(linkToDelete);

		// when
		linkRepo.add(linkToDelete);
		Iterable<JpaLink> result1 = linkRepo.findAll();

		// then
		assertAll("Assert remove link db is not empty", () -> assertThat(result1).isNotEmpty());

		// when
		Optional<Link> removedLink = linkRepo.remove(link);
		Iterable<JpaLink> result2 = linkRepo.findAll();

		// then
		assertAll("Assert remove link link removed", () -> assertThat(removedLink).isPresent(),
				() -> assertThat(removedLink.get()).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) removedLink.get(), expectedDelete),
				() -> assertThat(result2).isEmpty());
	}

	@Transactional
	@Rollback
	@Test
	public void removeNotExistingLinkTest() {
		// given
		String link = "abc";

		// when
		Optional<Link> removedLink = linkRepo.remove(link);

		// then
		assertAll("Assert remove not existing link", () -> assertThat(removedLink).isNotPresent());
	}

	@Transactional
	@Rollback
	@Test
	public void findByIdExistingLinkTest() {
		// given
		Link link1 = new Link();
		link1.setLink("https://github.com/1.html");

		Link link2 = new Link();
		link2.setLink("https://github.com/2.html");

		// when
		Link savedLink1 = linkRepo.add(link1);
		Link savedLink2 = linkRepo.add(link2);

		// then
		assertAll(
				"Assert save links in find by id existing link",
				() -> assertThat(savedLink1).isNotNull().isInstanceOf(JpaLink.class),
				() -> assertThat(savedLink2).isNotNull().isInstanceOf(JpaLink.class)
		);

		// given
		JpaLink expectedLink1 = (JpaLink) savedLink1;
		JpaLink expectedLink2 = (JpaLink) savedLink2;

		// when
		Optional<Link> resultChat1 = linkRepo.findById(expectedLink1.getId());
		Optional<Link> resultChat2 = linkRepo.findById(expectedLink2.getId());

		assertAll(
				"Assert find by id existing link",
				() -> assertThat(resultChat1).isPresent(),
				() -> assertThat(resultChat1.get()).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) resultChat1.get(), expectedLink1),
				() -> assertThat(resultChat2).isPresent(),
				() -> assertThat(resultChat2.get()).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) resultChat2.get(), expectedLink2)
		);
	}

	@Transactional
	@Rollback
	@Test
	public void findByIdNotExistingLinkTest() {
		// given

		// when
		Optional<Link> resultLink1 = linkRepo.findById(1);

		assertAll("Assert find by id not existing link", () -> assertThat(resultLink1).isNotPresent());
	}

	@Transactional
	@Rollback
	@Test
	public void findByLinkExistingLinkTest() {
		// given
		String link1 = "abc";
		Link initialLink1 = new Link();
		initialLink1.setLink(link1);

		String link2 = "xyz";
		Link initialLink2 = new Link();
		initialLink2.setLink(link2);

		// when
		Link savedLink1 = linkRepo.add(initialLink1);
		Link savedLink2 = linkRepo.add(initialLink2);

		// then
		assertAll(
				"Assert save links in find by link existing link",
				() -> assertThat(savedLink1).isNotNull().isInstanceOf(JpaLink.class),
				() -> assertThat(savedLink2).isNotNull().isInstanceOf(JpaLink.class)
		);

		// given
		JpaLink expectedLink1 = (JpaLink) savedLink1;
		JpaLink expectedLink2 = (JpaLink) savedLink2;

		// when
		Optional<Link> resultLink1 = linkRepo.findByLink(link1);
		Optional<Link> resultLink2 = linkRepo.findByLink(link2);

		assertAll("Assert find by link existing link", () -> assertThat(resultLink1).isPresent(),
				() -> assertThat(resultLink1.get()).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) resultLink1.get(), expectedLink1),
				() -> assertThat(resultLink2).isPresent(),
				() -> assertThat(resultLink2.get()).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) resultLink2.get(), expectedLink2));
	}

	@Transactional
	@Rollback
	@Test
	public void findByLinkNotExistingLinkTest() {
		// given

		// when
		Optional<Link> resultLink1 = linkRepo.findByLink("abc");

		assertAll("Assert find by link not existing link", () -> assertThat(resultLink1).isNotPresent());
	}

	@Transactional
	@Rollback
	@Test
	public void updateUpdateDateInExistingLinkTest() {
		// given
		String link = "https://github.com";
		OffsetDateTime initialUpdateDate = OffsetDateTime.of(2024, 5, 6, 15, 0, 0, 0, ZoneOffset.UTC);
		OffsetDateTime expectedUpdateDate = OffsetDateTime.of(2024, 5, 20, 15, 0, 0, 0, ZoneOffset.UTC);
		
		Link linkToUpdate = new Link();
		linkToUpdate.setLink(link);
		linkToUpdate.setUpdateDate(initialUpdateDate);
		JpaLink jpaLinkToUpdate = new JpaLink(linkToUpdate);

		// when
		Link savedLink = linkRepo.add(linkToUpdate);

		// then
		assertAll(
				"Assert update update_date db is not empty",
				() -> assertThat(savedLink).isInstanceOf(JpaLink.class),
				() -> assertJpaLinkEqualsWithoutId((JpaLink) savedLink, jpaLinkToUpdate)
		);

		// when
		Optional<Link> updatedLink = linkRepo.updateUpdateDate(savedLink.getId(), expectedUpdateDate);

		// then
		assertAll(
				"Assert remove link link removed",
				() -> assertThat(updatedLink).isPresent(),
				() -> assertThat(updatedLink.get()).isInstanceOf(JpaLink.class),
				() -> assertThat(updatedLink.get().getUpdateDate()).isEqualTo(expectedUpdateDate),
				() -> assertThat(linkRepo.findById(savedLink.getId()).get().getUpdateDate()).isEqualTo(expectedUpdateDate)
		);
	}

	@Transactional
	@Rollback
	@Test
	public void updateUpdateDateInNotExistingLinkTest() {
		// given
		long linkId = 1;
		OffsetDateTime newUpdateDate = OffsetDateTime.of(2024, 5, 20, 15, 0, 0, 0, ZoneOffset.UTC);

		// when
		Optional<Link> updatedLink = linkRepo.updateUpdateDate(linkId, newUpdateDate);

		// then
		assertAll(
				"Assert remove link link removed",
				() -> assertThat(updatedLink).isNotPresent()
		);
	}

	private void assertJpaLinkEqualsWithoutId(JpaLink actual, JpaLink expected) {
		assertThat(actual).usingRecursiveComparison().ignoringFields("id").isEqualTo(expected);
	}

	private void assertIterableJpaLinkEqualsWithoutId(Iterable<JpaLink> actual, Iterable<JpaLink> expected) {
		assertThat(actual).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
				.containsExactlyInAnyOrderElementsOf(expected);
	}
}
