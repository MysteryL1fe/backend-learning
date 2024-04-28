package ru.khanin.dmitrii.parser;

public record GitHubParser(ParserHandler next) implements ParserHandler {

	public Object parseLink(String link) {
		String[] splittedLink = link.split("/");
		if (splittedLink.length < 3 || !(splittedLink[0].equals("http:") || splittedLink[0].equals("https:"))
				|| !splittedLink[1].isEmpty())
			return null;

		String site = splittedLink[2];

		if (site.equals("github.com")) {
			if (splittedLink.length > 4)
				return new String[] { splittedLink[3], splittedLink[4] };
		} else if (next != null) {
			return next.parseLink(link);
		}
		return null;
	}
};
