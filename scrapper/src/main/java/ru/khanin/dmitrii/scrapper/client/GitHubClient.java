package ru.khanin.dmitrii.scrapper.client;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import ru.khanin.dmitrii.scrapper.DTO.GitHub.GitHubRepositoryEvent;

public class GitHubClient {
	private final WebClient webClient;
	
	public GitHubClient(URI baseUrl) {
		this.webClient = WebClient
				.builder()
				.baseUrl(baseUrl.toString())
				.build();
	}
	
	public List<GitHubRepositoryEvent> getRepositoryEvents(String owner, String repo, OffsetDateTime updatedAt) {
		List<GitHubRepositoryEvent> events = webClient
				.get()
				.uri("repos/{owner}/{repo}/events", owner, repo)
				.retrieve()
				.bodyToFlux(GitHubRepositoryEvent.class)
				.collectList()
				.block();
		
		if (events == null) events = new ArrayList<>();
		
		return events
				.stream()
				.filter((e) -> e.created_at().isAfter(updatedAt))
				.toList();
	}
}
