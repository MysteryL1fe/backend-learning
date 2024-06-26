package ru.khanin.dmitrii.scrapper.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import ru.khanin.dmitrii.scrapper.DTO.AddLinkRequest;
import ru.khanin.dmitrii.scrapper.DTO.LinkResponse;
import ru.khanin.dmitrii.scrapper.DTO.ListLinksResponse;
import ru.khanin.dmitrii.scrapper.DTO.RemoveLinkRequest;
import ru.khanin.dmitrii.scrapper.DTO.entity.Chat;
import ru.khanin.dmitrii.scrapper.DTO.entity.Link;
import ru.khanin.dmitrii.scrapper.service.ChatService;
import ru.khanin.dmitrii.scrapper.service.LinkChatService;
import ru.khanin.dmitrii.scrapper.service.LinkService;

@RestController
@RequiredArgsConstructor
public class ScrapperController {
	private final ChatService chatService;
	private final LinkService linkService;
	private final LinkChatService linkChatService;
	
	@PostMapping("/tg-chat/{id}")
	public ResponseEntity<?> registerChat(@PathVariable long id) {
		chatService.register(id);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/tg-chat/{id}")
	public ResponseEntity<?> deleteChat(@PathVariable long id) {
		chatService.unregister(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/links")
	public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") long chatId) {
		Chat foundChat = chatService.findByChatId(chatId);
		if (foundChat == null) return ResponseEntity.badRequest().build();
		Collection<Link> links = linkService.findAllByChatId(foundChat.getId());
		List<LinkResponse> linkResponses = new ArrayList<>();
		links.forEach((e) -> {
			try {
				linkResponses.add(new LinkResponse(e.getId(), new URI(e.getLink())));
			} catch (URISyntaxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
		ListLinksResponse result = new ListLinksResponse(linkResponses, linkResponses.size());
		return ResponseEntity.ok().body(result);
	}
	
	@PostMapping("/links")
	public ResponseEntity<LinkResponse> addLink(@RequestHeader("Tg-Chat-Id") long chatId,
			@RequestBody AddLinkRequest link) {
		Chat foundChat = chatService.findByChatId(chatId);
		if (foundChat == null) return ResponseEntity.badRequest().build();
		Link savedLink = linkService.add(link.link());
		long linkId = savedLink.getId();
		linkChatService.add(linkId, foundChat.getId());
		LinkResponse result = null;
		try {
			result = new LinkResponse(savedLink.getId(), new URI(savedLink.getLink()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(result);
	}
	
	@DeleteMapping("/links")
	public ResponseEntity<LinkResponse> deleteLink(@RequestHeader("Tg-Chat-Id") long chatId,
			@RequestBody RemoveLinkRequest link) {
		Chat foundChat = chatService.findByChatId(chatId);
		if (foundChat == null) return ResponseEntity.badRequest().build();
		Link foundLink = linkService.findByLink(link.link());
		if (foundLink == null) return ResponseEntity.badRequest().build();
		linkChatService.remove(foundLink.getId(), foundChat.getId());
		LinkResponse result = null;
		try {
			result = new LinkResponse(foundLink.getId(), new URI(foundLink.getLink()));
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ResponseEntity.ok().body(result);
	}
}