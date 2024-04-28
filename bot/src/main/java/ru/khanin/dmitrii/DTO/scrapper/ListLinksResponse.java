package ru.khanin.dmitrii.DTO.scrapper;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {}