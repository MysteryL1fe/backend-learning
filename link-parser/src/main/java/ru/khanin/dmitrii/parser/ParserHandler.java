package ru.khanin.dmitrii.parser;

public sealed interface ParserHandler permits GitHubParser, StackOverflowParser {
	Object parseLink(String link, String site);
};