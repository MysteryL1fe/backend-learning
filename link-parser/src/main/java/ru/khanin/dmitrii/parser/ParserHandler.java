package ru.khanin.dmitrii.parser;

public abstract class ParserHandler {
	private ParserHandler nextParser;
	
	public final void setNextParser(ParserHandler next) {
		nextParser = next;
	}
	
	public final Object parse(String link) {
		if (isSupported(link)) return parse(link);
		else if (nextParser != null) return nextParser.parse(link);
		return null;
	}
	
	protected abstract boolean isSupported(String link);
	protected abstract Object parseLink(String link);
};
