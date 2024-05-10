package ru.khanin.dmitrii.bot.commands;

import java.net.URI;
import java.net.URISyntaxException;

import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.bots.AbsSender;

import lombok.extern.slf4j.Slf4j;
import ru.khanin.dmitrii.DTO.scrapper.AddLinkRequest;
import ru.khanin.dmitrii.DTO.scrapper.LinkResponse;
import ru.khanin.dmitrii.client.ScrapperClient;

@Slf4j
public class TrackCommand extends Command {
	private final ScrapperClient scrapperClient;

	public TrackCommand(String commandIdentifier, String description, ScrapperClient scrapperClient) {
		super(commandIdentifier, description);
		this.scrapperClient = scrapperClient;
	}

	@Override
	public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {
		log.info("track command");
		
		if (arguments.length == 0) {
			sendAnswer(absSender, chat.getId(), "Ссылка не указана");
			return;
		}
		
		try {
			URI url = new URI(arguments[0]);
			LinkResponse link = scrapperClient.addLink(chat.getId(), new AddLinkRequest(url));
			if (link == null) sendAnswer(absSender, chat.getId(), "Ссылка не поддерживается");
			else sendAnswer(absSender, chat.getId(), "Ссылка " + link.url().toString() + " успешно добавлена");
		} catch (URISyntaxException e) {
			sendAnswer(absSender, chat.getId(), "Ссылка не распознана");
		}
	}

}
