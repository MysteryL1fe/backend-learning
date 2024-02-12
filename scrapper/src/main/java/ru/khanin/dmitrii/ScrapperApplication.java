package ru.khanin.dmitrii;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import ru.khanin.dmitrii.configuration.ApplicationConfig;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ApplicationConfig.class)
public class ScrapperApplication {
	public static void main(String[] args) {
		var ctx = SpringApplication.run(ScrapperApplication.class);
		ApplicationConfig config = ctx.getBean(ApplicationConfig.class);
		System.out.println(config);
	}
}