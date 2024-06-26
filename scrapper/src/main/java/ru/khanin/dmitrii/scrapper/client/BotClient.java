package ru.khanin.dmitrii.scrapper.client;

import java.net.URI;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import ru.khanin.dmitrii.scrapper.DTO.bot.LinkUpdateRequest;

public class BotClient {
	private final WebClient webClient;
	
	public BotClient(URI baseUrl) {
		this.webClient = WebClient
				.builder()
				.baseUrl(baseUrl.toString())
				.build();
	}
	
	public void linkUpdate(long id, URI url, String description, List<Long> tgChatIds) {
		LinkUpdateRequest req = new LinkUpdateRequest(id, url, description, tgChatIds);
		webClient
			.post()
			.uri("/updates")
			.bodyValue(req)
			.retrieve()
			.bodyToMono(Void.class)
			.block();
	}
}
