package ru.khanin.dmitrii.DTO.GitHub;

import java.time.OffsetDateTime;

public record RepositoryResponse(int id, String nodeId, String name, String fullName, String htmlUrl,
		String description, boolean fork, int forks, int stargazersCount, int watchers,
		int size, OffsetDateTime createdAt, OffsetDateTime pushedAt, OffsetDateTime updatedAt,
		OwnerResponse owner) {}
