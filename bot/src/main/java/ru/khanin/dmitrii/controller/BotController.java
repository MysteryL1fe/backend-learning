package ru.khanin.dmitrii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.DTO.LinkUpdateRequest;
import ru.khanin.dmitrii.bot.TelegramBot;

@RestController
@RequiredArgsConstructor
public class BotController {
	TelegramBot tgBot;
	
	@PostMapping("/updates")
	public ResponseEntity<?> postUpdates(@RequestBody LinkUpdateRequest update) {
		tgBot.linkUpdate(update.id(), update.url(), update.description(), update.tgChatIds());
		return ResponseEntity.ok().build();
	}
}