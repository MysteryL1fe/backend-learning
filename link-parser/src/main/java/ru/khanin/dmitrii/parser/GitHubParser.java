package ru.khanin.dmitrii.parser;

public record GitHubParser(ParserHandler next) implements ParserHandler {
	public Object parseLink(String link, String site) {
		if (site.equals("github.com")) {
			String[] splittedLink = link.split("/");
			if (splittedLink.length > 4) return new String[] {splittedLink[3], splittedLink[4]};
		} else if (next != null) {
			return next.parseLink(link, site);
		}
		return null;
	}
};