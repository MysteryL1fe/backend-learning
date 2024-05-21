package ru.khanin.dmitrii.scrapper.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotNull;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jdbc.JdbcLinkRepository;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaLinkChatRepository;
import ru.khanin.dmitrii.scrapper.domain.jpa.JpaLinkRepository;
import ru.khanin.dmitrii.scrapper.service.ChatService;
import ru.khanin.dmitrii.scrapper.service.LinkChatService;
import ru.khanin.dmitrii.scrapper.service.LinkService;
import ru.khanin.dmitrii.scrapper.service.jdbc.JdbcChatService;
import ru.khanin.dmitrii.scrapper.service.jdbc.JdbcLinkChatService;
import ru.khanin.dmitrii.scrapper.service.jdbc.JdbcLinkService;
import ru.khanin.dmitrii.scrapper.service.jpa.JpaChatService;
import ru.khanin.dmitrii.scrapper.service.jpa.JpaLinkChatService;
import ru.khanin.dmitrii.scrapper.service.jpa.JpaLinkService;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, @NotNull Scheduler scheduler, @NotNull AccessType databaseAccessType) {
	public enum AccessType {
		JDBC,
		JPA
	}
	
	@Configuration
	@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
	public static class JdbcAccessConfiguration {
		@Bean
		public ChatService chatService(JdbcChatRepository chatRepository, JdbcLinkChatRepository linkChatRepository) {
			return new JdbcChatService(chatRepository, linkChatRepository);
		}
		
		@Bean
		public LinkService linkService(JdbcLinkRepository linkRepository, JdbcLinkChatRepository linkChatRepository) {
			return new JdbcLinkService(linkRepository, linkChatRepository);
		}
		
		@Bean LinkChatService linkChatService(JdbcLinkChatRepository linkChatRepository) {
			return new JdbcLinkChatService(linkChatRepository);
		}
	}
	
	@Configuration
	@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jpa")
	public static class JpaAccessConfiguration {
		@Bean
		public ChatService chatService(JpaChatRepository chatRepository, JpaLinkChatRepository linkChatRepository) {
			return new JpaChatService(chatRepository, linkChatRepository);
		}
		
		@Bean
		public LinkService linkService(JpaLinkRepository linkRepository, JpaLinkChatRepository linkChatRepository) {
			return new JpaLinkService(linkRepository, linkChatRepository);
		}
		
		@Bean
		public LinkChatService linkChatService(JpaLinkChatRepository linkChatRepository) {
			return new JpaLinkChatService(linkChatRepository);
		}
	}
}