package ru.khanin.dmitrii.client;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.reactive.function.client.WebClient;

import ru.khanin.dmitrii.DTO.StackOverflow.StackOverflowQuestionItem;

public class StackOverflowClient {
	private final WebClient webClient;
	
	public StackOverflowClient(URI baseUrl) {
		this.webClient = WebClient
				.builder()
				.baseUrl(baseUrl.toString())
				.build();
	}
	
	public List<StackOverflowQuestionItem> getQuestionItems(String questionId, OffsetDateTime updatedAt) {
		record StackOverflowQuestionItems(StackOverflowQuestionItem[] items) {}
		
		StackOverflowQuestionItems items = webClient
				.get()
				.uri("/2.3/questions/{id}/timeline?fromdate={date}&site=stackoverflow", questionId, updatedAt.toEpochSecond() * 1000)
				.retrieve()
				.bodyToMono(StackOverflowQuestionItems.class)
				.block();
		
		if (items == null) items = new StackOverflowQuestionItems(new StackOverflowQuestionItem[0]);
		
		return Arrays.asList(items.items);
	}
}
