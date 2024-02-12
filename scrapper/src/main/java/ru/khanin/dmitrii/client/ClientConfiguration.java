package ru.khanin.dmitrii.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ClientConfiguration {
	@Bean
	public GitHubClient gitHubService() {
		WebClient client = WebClient.builder().baseUrl("http://api.github.com/").build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
		return factory.createClient(GitHubClient.class);
	}
	
	@Bean
	public StackOverflowClient stackOverflowService() {
		WebClient client = WebClient.builder().baseUrl("http://stackoverflow.com/").build();
		HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
		return factory.createClient(StackOverflowClient.class);
	}
}
