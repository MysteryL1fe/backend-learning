package ru.khanin.dmitrii.scrapper.test;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class TestcontainersContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
	private static final DockerImageName POSTGRES_DOCKER_IMAGE = DockerImageName.parse("postgres:latest");
	protected static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_DOCKER_IMAGE);
	
	@Override
	public void initialize(@NotNull ConfigurableApplicationContext applicationContext){
		log.info("init");
		Startables.deepStart(postgreSQLContainer).join();
		TestPropertyValues.of(
				"spring.datasource.url=%s".formatted(postgreSQLContainer.getJdbcUrl()),
				"spring.datasource.username=%s".formatted(postgreSQLContainer.getUsername()),
				"spring.datasource.password=%s".formatted(postgreSQLContainer.getPassword())
		).applyTo(applicationContext);
	}
}
