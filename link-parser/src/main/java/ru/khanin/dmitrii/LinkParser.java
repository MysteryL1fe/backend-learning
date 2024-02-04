package ru.khanin.dmitrii;

import ru.khanin.dmitrii.parser.*;

public class LinkParser {
	public static void main(String[] args) {
		ParserHandler parser = new GitHubParser(new StackOverflowParser(null));
		String link = "";
		String[] splittedLink = link.split("/");
		if (splittedLink.length > 2)
		System.out.println(parser.parseLink(link, link.split("/")[2]));
	}
}