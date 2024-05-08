package ru.khanin.dmitrii.DTO.bot;

import java.net.URI;

public record LinkUpdateRequest(long id, URI url, String description, long[] tgChatIds) {}