package ru.khanin.dmitrii.client;

import java.net.URI;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.khanin.dmitrii.DTO.GitHub.RepositoryResponse;

public class GitHubClient {
	private final WebClient webClient;
	
	public GitHubClient(URI baseUrl) {
		this.webClient = WebClient
				.builder()
				.baseUrl(baseUrl.toString())
				.build();
	}
	
	public Mono<RepositoryResponse> getRepository(String owner, String repo) {
		return webClient
				.get()
				.uri("repos/{owner}/{repo}", owner, repo)
				.retrieve()
				.bodyToMono(RepositoryResponse.class);
	}
}
