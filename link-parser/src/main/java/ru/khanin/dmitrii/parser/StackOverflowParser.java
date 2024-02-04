package ru.khanin.dmitrii.parser;

public record StackOverflowParser(ParserHandler next) implements ParserHandler {
	public Object parseLink(String link, String site) {
		if (site.equals("stackoverflow.com")) {
			String[] splittedLink = link.split("/");
			if (splittedLink.length > 4 && splittedLink[3].equals("questions")) return Integer.parseInt(splittedLink[4]);
		} else if (next != null) {
			return next.parseLink(link, site);
		}
		return null;
	}
};