package ru.khanin.dmitrii.DTO;

import java.util.List;

public record LinkUpdate(int id, String url, String description, List<Integer> tgChatIds) {}