package ru.khanin.dmitrii.link.parser;

import ru.khanin.dmitrii.link.parser.parser.GitHubParser;
import ru.khanin.dmitrii.link.parser.parser.ParserHandler;
import ru.khanin.dmitrii.link.parser.parser.StackOverflowParser;

public class LinkParser {
	public static void main(String[] args) {
		ParserHandler parser = new GitHubParser();
		parser.setNextParser(new StackOverflowParser());
		String link = "";
		String[] splittedLink = link.split("/");
		if (splittedLink.length > 2)
		System.out.println(parser.parse(link));
	}
}