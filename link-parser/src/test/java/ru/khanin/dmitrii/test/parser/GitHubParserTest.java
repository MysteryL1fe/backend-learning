package ru.khanin.dmitrii.test.parser;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.khanin.dmitrii.parser.GitHubParser;
import ru.khanin.dmitrii.parser.ParserHandler;

@ExtendWith(MockitoExtension.class)
public class GitHubParserTest {
	
	@InjectMocks
	private GitHubParser gitHubParser;
	
	@Mock
	private ParserHandler nextParser;
	
	@BeforeEach
	void setUp() {
		gitHubParser = new GitHubParser(nextParser);
	}
	
	@Test
	void parseLinkNotLinkShouldReturnNull() {
		String link = "123";
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert not link",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotValidLinkWithSymbolsAfterSlashShouldReturnNull() {
		String link = "http:/asd/asd";
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert not valid link with symbols after slash",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotValidLinkWithoutProtocolShouldReturnNull() {
		String link = "//123/456";
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert not valid link without protocol",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotGitHubLinkShouldCallNextParser() {
		String link = "http://abc";
		String expectedResult = "456";
		Mockito.when(nextParser.parseLink(Mockito.any())).thenReturn(expectedResult);
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert not github link with next parser",
				() -> assertThat(result).isNotNull(),
				() -> assertThat(result).isInstanceOf(String.class),
				() -> assertThat((String) result).isEqualTo(expectedResult)
		);
	}
	
	@Test
	void parseLinkValidHttpLinkShouldReturnStrings() {
		String link = "http://github.com/123/456";
		String[] expectedResult = new String[] {"123", "456"};
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert valid github http link",
				() -> assertThat(result).isNotNull(),
				() -> assertThat(result).isInstanceOf(String[].class),
				() -> assertThat((String[]) result).isEqualTo(expectedResult)
		);
	}
	
	@Test
	void parseLinkValidHttpsLinkShouldReturnStrings() {
		String link = "https://github.com/123/456";
		String[] expectedResult = new String[] {"123", "456"};
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert valid github https link",
				() -> assertThat(result).isNotNull(),
				() -> assertThat(result).isInstanceOf(String[].class),
				() -> assertThat((String[]) result).isEqualTo(expectedResult)
		);
	}
	
	@Test
	void parseLinkTooShortGitHubLinkShouldReturnNull() {
		String link = "http://github.com";
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert too short github link",
				() -> assertThat(result).isNull()
		);
	}
	
	@Test
	void parseLinkNotGitHubLinkShouldReturnNull() {
		gitHubParser = new GitHubParser(null);
		String link = "http://abc";
		
		Object result = gitHubParser.parseLink(link);
		
		assertAll(
				"Assert not github link without next parser",
				() -> assertThat(result).isNull()
		);
	}
}
