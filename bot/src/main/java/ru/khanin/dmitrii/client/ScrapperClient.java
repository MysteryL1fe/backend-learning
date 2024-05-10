package ru.khanin.dmitrii.client;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.khanin.dmitrii.DTO.scrapper.AddLinkRequest;
import ru.khanin.dmitrii.DTO.scrapper.LinkResponse;
import ru.khanin.dmitrii.DTO.scrapper.ListLinksResponse;
import ru.khanin.dmitrii.DTO.scrapper.RemoveLinkRequest;

@Service
public class ScrapperClient {
	private final WebClient webClient;

	public ScrapperClient(URI baseUrl) {
		this.webClient = WebClient
				.builder()
				.baseUrl(baseUrl.toString())
				.build();
	}

	public Mono<Void> addTgChatId(long tgChatId) {
		return webClient
				.post()
				.uri("tg-chat/{id}", tgChatId)
				.retrieve()
				.bodyToMono(Void.class);
	}

	public Mono<Void> deleteTgChatId(long tgChatId) {
		return webClient
				.delete()
				.uri("tg-chat/{id}", tgChatId)
				.retrieve()
				.bodyToMono(Void.class);
	}

	public List<LinkResponse> getLinks(long tgChatId) {
		ListLinksResponse result = webClient
				.get()
				.uri("links")
				.header("Tg-Chat-Id", String.valueOf(tgChatId))
				.retrieve()
				.bodyToMono(ListLinksResponse.class)
				.block();
		
		if (result == null) return new ArrayList<LinkResponse>();
		
		return result.links();
	}

	public LinkResponse addLink(long tgChatId, AddLinkRequest link) {
		return webClient
				.post()
				.uri("links")
				.header("Tg-Chat-Id", String.valueOf(tgChatId))
				.bodyValue(link)
				.retrieve()
				.bodyToMono(LinkResponse.class)
				.block();
	}

	public LinkResponse deleteLink(long tgChatId, RemoveLinkRequest link) {
		return webClient
				.method(HttpMethod.DELETE)
				.uri("links")
				.header("Tg-Chat-Id", String.valueOf(tgChatId))
				.bodyValue(link)
				.retrieve()
				.bodyToMono(LinkResponse.class)
				.block();
	}
}
