package ru.khanin.dmitrii.bot.commands;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.extensions.bots.commandbot.commands.BotCommand;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public abstract class Command extends BotCommand {
	public static final Logger log = LoggerFactory.getLogger(Logger.class);

	public Command(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	protected void sendAnswer(AbsSender absSender, Long chatId, String text) {
		SendMessage msg = new SendMessage();
		msg.setChatId(chatId);
		msg.setText(text);
		try {
			absSender.execute(msg);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
