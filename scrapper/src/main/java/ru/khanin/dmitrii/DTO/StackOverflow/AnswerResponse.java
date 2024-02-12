package ru.khanin.dmitrii.DTO.StackOverflow;

import java.time.OffsetDateTime;

public record AnswerResponse(int answerId, OffsetDateTime creationDate, OffsetDateTime lastEditDate,
		ShallowUserResponse owner) {}
