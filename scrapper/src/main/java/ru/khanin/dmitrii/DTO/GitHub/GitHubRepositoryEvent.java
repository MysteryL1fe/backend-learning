package ru.khanin.dmitrii.DTO.GitHub;

import java.time.OffsetDateTime;

public record GitHubRepositoryEvent(String type, OffsetDateTime created_at) {}
