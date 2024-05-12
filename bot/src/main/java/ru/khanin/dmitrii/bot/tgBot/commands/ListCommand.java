package ru.khanin.dmitrii.bot.tgBot.commands;

import java.util.List;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.bot.DTO.scrapper.LinkResponse;
import ru.khanin.dmitrii.bot.client.ScrapperClient;

@Component
@Slf4j
public class ListCommand extends Command {
	private final ScrapperClient scrapperClient;

	public ListCommand(String commandIdentifier, String description, ScrapperClient scrapperClient) {
		super(commandIdentifier, description);
		
		this.scrapperClient = scrapperClient;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		log.info("list command");
		
		List<LinkResponse> links = scrapperClient.getLinks(chat.getId());
		StringBuilder sb = new StringBuilder("Ваши ссылки:");
		for (LinkResponse link : links) {
			sb.append("\n" + link.url().toString());
		}
		
		sendAnswer(absSender, chat.getId(), sb.toString());
	}

}
