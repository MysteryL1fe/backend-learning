package ru.khanin.dmitrii.bot.DTO.scrapper;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {}