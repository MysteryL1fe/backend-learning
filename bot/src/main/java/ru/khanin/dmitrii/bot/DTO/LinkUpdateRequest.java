package ru.khanin.dmitrii.bot.DTO;

import java.net.URI;

public record LinkUpdateRequest(long id, URI url, String description, long[] tgChatIds) {}