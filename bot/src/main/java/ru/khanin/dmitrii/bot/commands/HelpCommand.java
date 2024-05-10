package ru.khanin.dmitrii.bot.commands;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HelpCommand extends Command {

	public HelpCommand(String commandIdentifier, String description) {
		super(commandIdentifier, description);
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		log.info("help command");
		sendAnswer(absSender, chat.getId(), "/help - \n /track - \n /untrack - \n /list - ");
	}

}
