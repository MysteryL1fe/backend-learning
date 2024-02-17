package ru.khanin.dmitrii.DTO.StackOverflow;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ShallowUserResponse(@JsonProperty("account_id") int accountId, @JsonProperty("display_name") String displayName, String link) {}
