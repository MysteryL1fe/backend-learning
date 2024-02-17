package ru.khanin.dmitrii.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import ru.khanin.dmitrii.DTO.*;

@RestController
public class ScrapperController {
	@PostMapping("/tg-chat/{id}")
	public ResponseEntity<?> registerChat(@PathVariable int id) {
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/tg-chat/{id}")
	public ResponseEntity<?> deleteChat(@PathVariable int id) {
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/links")
	public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") int chatId) {
		return ResponseEntity.ok().build();
	}
	
	@PostMapping("/links")
	public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") int chatId,
			@RequestBody AddLinkRequest link) {
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/links")
	public ResponseEntity<LinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") int chatId,
			@RequestBody RemoveLinkRequest link) {
		return ResponseEntity.ok().build();
	}
}