package ru.khanin.dmitrii.bot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.khanin.dmitrii.bot.commands.*;

public class TelegramBot extends TelegramLongPollingCommandBot {
	public static final Logger log = LoggerFactory.getLogger(Logger.class);
	private final String BOT_NAME;
	
	public TelegramBot(String botName, String botToken) {
		super(botToken);
		BOT_NAME = botName;
		register(new StartCommand("start", "Зарегистрировать пользователя"));
		register(new HelpCommand("help", "Вывести окно с командами"));
		register(new TrackCommand("track", "Начать отслеживание ссылки"));
		register(new UntrackCommand("untrack", "Прекратить отслеживание ссылки"));
		register(new ListCommand("list", "Показать список отслеживаемых ссылок"));
	}

	@Override
	public void processNonCommandUpdate(Update update) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		return BOT_NAME;
	}
}
