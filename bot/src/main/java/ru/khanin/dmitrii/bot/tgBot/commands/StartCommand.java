package ru.khanin.dmitrii.bot.tgBot.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.bot.client.ScrapperClient;

@Slf4j
public class StartCommand extends Command {
	@Autowired
	ScrapperClient scrapperClient;

	public StartCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		log.info("start command");
		sendAnswer(absSender, chat.getId(), "Успешно зарегестрированы");
		scrapperClient.addTgChatId(chat.getId());
	}

}
