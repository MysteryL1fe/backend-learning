package ru.khanin.dmitrii.parser;

import ru.khanin.dmitrii.content.GitHubContent;

public class GitHubParser extends ParserHandler {
	@Override
	protected boolean isSupported(String link) {
		String[] splittedLink = link.split("/");
		
		if (splittedLink.length < 5
				&& !splittedLink[0].equals("http:") && !splittedLink[0].equals("https:")
				&& !splittedLink[1].isEmpty()
				&& !splittedLink[2].equals("github.com")
		) return false;
		
		return true;
	}

	@Override
	protected Object parseLink(String link) {
		String[] splittedLink = link.split("/");

		return new GitHubContent(splittedLink[3], splittedLink[4]);
	}
};
