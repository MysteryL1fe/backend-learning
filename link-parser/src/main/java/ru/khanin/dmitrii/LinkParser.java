package ru.khanin.dmitrii;

import ru.khanin.dmitrii.parser.*;

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