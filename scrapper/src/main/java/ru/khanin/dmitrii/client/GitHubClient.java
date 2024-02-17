package ru.khanin.dmitrii.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.khanin.dmitrii.DTO.GitHub.RepositoryResponse;

@Service
public class GitHubClient {
	private final WebClient webClient;
	
	public GitHubClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("http://api.github.com/").build();
	}
	
	public Mono<RepositoryResponse> getRepository(String owner, String repo) {
		return webClient.get().uri("repos/{owner}/{repo}", owner, repo).retrieve().bodyToMono(RepositoryResponse.class);
	}
}
