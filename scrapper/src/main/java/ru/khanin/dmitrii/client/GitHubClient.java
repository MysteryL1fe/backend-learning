package ru.khanin.dmitrii.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import ru.khanin.dmitrii.DTO.GitHub.RepositoryResponse;

public interface GitHubClient {
	@GetExchange("repos/{owner}/{repo}")
	RepositoryResponse getRepository(@PathVariable String owner, @PathVariable String repo);
}
