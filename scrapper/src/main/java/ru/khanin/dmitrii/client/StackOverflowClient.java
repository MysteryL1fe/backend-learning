package ru.khanin.dmitrii.client;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import ru.khanin.dmitrii.DTO.StackOverflow.ListAnswersResponse;

public interface StackOverflowClient {
	@GetExchange("2.3/questions/{id}/answers?order=desc&sort=activity&site=stackoverflow")
	ListAnswersResponse getAnswers(@PathVariable int id);
}
