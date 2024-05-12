package ru.khanin.dmitrii.link.parser.parser;

import ru.khanin.dmitrii.link.parser.content.StackOverflowContent;

public class StackOverflowParser extends ParserHandler {
	
	@Override
	protected boolean isSupported(String link) {
		String[] splittedLink = link.split("/");
		
		if (splittedLink.length < 5
				&& !splittedLink[0].equals("http:") && !splittedLink[0].equals("https:")
				&& !splittedLink[1].isEmpty()
				&& !splittedLink[2].equals("stackoverflow.com")
				&& !splittedLink[3].equals("questions")
		) return false;
		
		return true;
	}

	@Override
	protected Object parseLink(String link) {
		String[] splittedLink = link.split("/");
		
		return new StackOverflowContent(splittedLink[4]);
	}
};
