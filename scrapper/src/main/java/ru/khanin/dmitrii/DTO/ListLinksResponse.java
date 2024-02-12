package ru.khanin.dmitrii.DTO;

import java.util.List;

public record ListLinksResponse(List<LinkResponse> links, int size) {}