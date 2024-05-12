package ru.khanin.dmitrii.test.link.parser.parser;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.khanin.dmitrii.link.parser.content.StackOverflowContent;
import ru.khanin.dmitrii.link.parser.parser.ParserHandler;
import ru.khanin.dmitrii.link.parser.parser.StackOverflowParser;

@ExtendWith(MockitoExtension.class)
public class StackOverflowParserTest {

	@InjectMocks
	private StackOverflowParser stackOverflowParser;
	
	@Mock
	private ParserHandler nextParser;
	
	@BeforeEach
	void setUp() {
		stackOverflowParser = new StackOverflowParser();
		stackOverflowParser.setNextParser(nextParser);
	}
	
	@Test
	void parseLinkNotLinkShouldReturnNull() {
		String link = "123";
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert not link",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotValidLinkWithoutProtocolShouldReturnNull() {
		String link = "//123/456";
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert not valid link without protocol",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotValidLinkWithSymbolsAfterSlashShouldReturnNull() {
		String link = "http:/123/456";
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert not valid link with symbols after slash",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotStackOverflowLinkShouldCallNextParser() {
		String link = "http://abc";
		String expectedResult = "12345";
		Mockito.when(nextParser.parse(Mockito.any())).thenReturn(expectedResult);
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert not stack overflow link with next parser",
				() -> assertThat(result).isNotNull(),
				() -> assertThat(result).isInstanceOf(String.class),
				() -> assertThat((String) result).isEqualTo(expectedResult)
		);
	}
	
	@Test
	void parseLinkNotStackOverflowLinkShouldReturnNull() {
		stackOverflowParser = new StackOverflowParser();
		String link = "http://abc";
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert not stack overflow link without next parser",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotQuestionLinkShouldReturnNull() {
		String link = "http://stackoverflow.com/abc/123";
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert stack overflow not question link",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkTooShortQuestionLinkShouldReturnNull() {
		String link = "http://stackoverflow.com/question";
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert stack overflow too short question link",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkValidHttpLinkShouldReturnLong() {
		String link = "http://stackoverflow.com/questions/123";
		StackOverflowContent expectedResult = new StackOverflowContent("123");
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert valid http stack overflow ling",
				() -> assertThat(result).isNotNull(),
				() -> assertThat(result).isInstanceOf(StackOverflowContent.class),
				() -> assertThat((StackOverflowContent) result).isEqualTo(expectedResult)
		);
	}
	
	@Test
	void parseLinkValidHttpsLinkShouldReturnLong() {
		String link = "https://stackoverflow.com/questions/123";
		StackOverflowContent expectedResult = new StackOverflowContent("123");
		
		Object result = stackOverflowParser.parse(link);
		
		assertAll(
				"Assert valid https stack overflow ling",
				() -> assertThat(result).isNotNull(),
				() -> assertThat(result).isInstanceOf(StackOverflowContent.class),
				() -> assertThat((StackOverflowContent) result).isEqualTo(expectedResult)
		);
	}
}
