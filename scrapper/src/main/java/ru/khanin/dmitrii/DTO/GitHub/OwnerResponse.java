package ru.khanin.dmitrii.DTO.GitHub;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OwnerResponse(String login, int id, @JsonProperty("node_id") String nodeId, @JsonProperty("html_url") String htmlUrl, String type) {}
