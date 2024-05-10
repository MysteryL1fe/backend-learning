package ru.khanin.dmitrii.bot;

import java.net.URI;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.bot.commands.HelpCommand;
import ru.khanin.dmitrii.bot.commands.ListCommand;
import ru.khanin.dmitrii.bot.commands.StartCommand;
import ru.khanin.dmitrii.bot.commands.TrackCommand;
import ru.khanin.dmitrii.bot.commands.UntrackCommand;
import ru.khanin.dmitrii.client.ScrapperClient;

@Component
@Slf4j
public class TelegramBot extends TelegramLongPollingCommandBot {
	private final ScrapperClient scrapperClient;
	private final String BOT_NAME;
	
	public TelegramBot(String botName, String botToken, ScrapperClient scrapperClient) {
		super(botToken);
		
		this.scrapperClient = scrapperClient;
		this.BOT_NAME = botName;
		
		register(new StartCommand("start", "Зарегистрировать пользователя"));
		register(new HelpCommand("help", "Вывести окно с командами"));
		register(new TrackCommand("track", "Начать отслеживание ссылки", this.scrapperClient));
		register(new UntrackCommand("untrack", "Прекратить отслеживание ссылки", this.scrapperClient));
		register(new ListCommand("list", "Показать список отслеживаемых ссылок", this.scrapperClient));
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		log.info("processNonCommandUpdate");
		SendMessage answer = new SendMessage();
		answer.setText("Неизвестная команда");
		answer.setChatId(update.getMessage().getChatId());
		try {
			execute(answer);
		} catch (TelegramApiException e) {
			log.error(e.getMessage());
		}
	}

	@Override
	public String getBotUsername() {
		return BOT_NAME;
	}
	
	public void linkUpdate(long id, URI url, String description, long[] tgChatIds) {
		SendMessage message = new SendMessage();
		message.setText("Updated " + url.toString() + ": " + description);
		for (long tgChatId : tgChatIds) {
			message.setChatId(tgChatId);
			try {
				execute(message);
			} catch (TelegramApiException e) {
				log.error(e.getMessage());
			}
		}
	}
}
