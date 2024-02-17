package ru.khanin.dmitrii.DTO.StackOverflow;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AnswerResponse(@JsonProperty("answer_id") int answerId, @JsonProperty("creation_date") OffsetDateTime creationDate,
		@JsonProperty("last_edit_date") OffsetDateTime lastEditDate, ShallowUserResponse owner) {}
