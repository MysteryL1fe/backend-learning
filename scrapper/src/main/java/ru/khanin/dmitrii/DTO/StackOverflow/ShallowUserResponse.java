package ru.khanin.dmitrii.DTO.StackOverflow;

import java.net.URI;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShallowUserResponse(@JsonProperty("account_id") int accountId, @JsonProperty("display_name") String displayName, URI link) {}
