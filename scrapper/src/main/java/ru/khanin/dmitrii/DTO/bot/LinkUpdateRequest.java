package ru.khanin.dmitrii.DTO.bot;

import java.net.URI;
import java.util.List;

public record LinkUpdateRequest(long id, URI url, String description, List<Long> tgChatIds) {}