package ru.khanin.dmitrii.scrapper.DTO;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {}