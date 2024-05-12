package ru.khanin.dmitrii.scrapper.DTO.GitHub;

import java.time.OffsetDateTime;

public record GitHubRepositoryEvent(String type, OffsetDateTime created_at) {}
