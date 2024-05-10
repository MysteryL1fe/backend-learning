package ru.khanin.dmitrii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import ru.khanin.dmitrii.bot.TelegramBot;
import ru.khanin.dmitrii.configuration.ApplicationConfig;

@SpringBootApplication
@EnableConfigurationProperties(ApplicationConfig.class)
public class BotApplication {
	public static void main(String[] args) throws TelegramApiException {
		var ctx = SpringApplication.run(BotApplication.class);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		System.out.println(config);
		
		TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
		botsApi.registerBot(ctx.getBean(TelegramBot.class));
	}
}