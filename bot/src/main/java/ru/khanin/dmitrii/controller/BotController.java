package ru.khanin.dmitrii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.khanin.dmitrii.DTO.*;

@RestController
public class BotController {
	@PostMapping("/updates")
	public ResponseEntity<?> postUpdates(@RequestBody LinkUpdateRequest update) {
		return ResponseEntity.ok().build();
	}
}