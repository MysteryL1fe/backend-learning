package ru.khanin.dmitrii.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import ru.khanin.dmitrii.DTO.StackOverflow.ListAnswersResponse;

@Service
public class StackOverflowClient {
	private final WebClient webClient;
	
	public StackOverflowClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl("https://api.stackexchange.com/").build();
	}
	
	public Mono<ListAnswersResponse> getAnswers(int id) {
		return webClient.get().uri("2.3/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow", id).retrieve().bodyToMono(ListAnswersResponse.class);
	}
}
