package ru.khanin.dmitrii.scrapper.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "rabbit", ignoreUnknownFields = false)
@Data
public class RabbitMQConfiguration {
	private String queueName;
	private String exchangeName;
	
	@Bean
	public DirectExchange messageExchange() {
		return new DirectExchange(exchangeName);
	}
	
	@Bean
	public Queue messageQueue() {
		return new Queue(queueName);
	}
	
	@Bean
	public Binding messageBinding() {
		return BindingBuilder
				.bind(messageQueue())
				.to(messageExchange())
				.with("routing key");
	}
	
	@Bean
	public MessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
