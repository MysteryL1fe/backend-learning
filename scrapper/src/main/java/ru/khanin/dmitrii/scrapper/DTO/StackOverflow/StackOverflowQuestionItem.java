package ru.khanin.dmitrii.scrapper.DTO.StackOverflow;

import java.util.Date;

public record StackOverflowQuestionItem(Date creation_date, String timeline_type) {}
