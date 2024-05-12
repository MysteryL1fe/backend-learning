package ru.khanin.dmitrii.test.scrapper;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {TestcontainersContextInitializer.class})
public class IntegrationEnvironment {
	
}
