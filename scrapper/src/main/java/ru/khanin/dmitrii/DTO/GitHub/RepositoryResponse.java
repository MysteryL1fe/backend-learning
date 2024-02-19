package ru.khanin.dmitrii.DTO.GitHub;

import java.net.URI;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RepositoryResponse(int id, @JsonProperty("node_id") String nodeId, String name, @JsonProperty("full_name") String fullName,
		@JsonProperty("html_url") URI htmlUrl, String description, int forks, int watchers, int size,
		@JsonProperty("created_at") OffsetDateTime createdAt, @JsonProperty("pushed_at") OffsetDateTime pushedAt,
		@JsonProperty("updated_at") OffsetDateTime updatedAt, OwnerResponse owner) {}
