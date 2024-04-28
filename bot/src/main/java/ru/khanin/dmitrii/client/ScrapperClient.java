package ru.khanin.dmitrii.client;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.khanin.dmitrii.DTO.scrapper.*;

@Service
public class ScrapperClient {
	private final WebClient webClient;

	public ScrapperClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("").build();
	}

	public Mono<Void> addTgChatId(long tgChatId) {
		return webClient.post().uri("tg-chat/{id}", tgChatId).retrieve().bodyToMono(Void.class);
	}

	public Mono<Void> deleteTgChatId(long tgChatId) {
		return webClient.delete().uri("tg-chat/{id}", tgChatId).retrieve().bodyToMono(Void.class);
	}

	public Mono<ListLinksResponse> getLinks(long tgChatId) {
		return webClient.get().uri("links").header("Tg-Chat-Id", new String[] { String.valueOf(tgChatId) }).retrieve()
				.bodyToMono(ListLinksResponse.class);
	}

	public Mono<LinkResponse> addLink(long tgChatId, AddLinkRequest link) {
		return webClient.post().uri("links").header("Tg-Chat-Id", new String[] { String.valueOf(tgChatId) })
				.bodyValue(link).retrieve().bodyToMono(LinkResponse.class);
	}

	public Mono<LinkResponse> deleteLink(long tgChatId, RemoveLinkRequest link) {
		return webClient.method(HttpMethod.DELETE).uri("links")
				.header("Tg-Chat-Id", new String[] { String.valueOf(tgChatId) }).bodyValue(link).retrieve()
				.bodyToMono(LinkResponse.class);
	}
}
