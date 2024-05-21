package ru.khanin.dmitrii.scrapper.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {TestcontainersContextInitializer.class})
public abstract class IntegrationEnvironment {
	
}
