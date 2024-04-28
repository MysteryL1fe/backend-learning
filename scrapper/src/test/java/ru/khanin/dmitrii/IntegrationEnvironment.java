package ru.khanin.dmitrii;

import java.io.File;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.JdbcDatabaseContainer.NoDriverFoundException;
import org.testcontainers.containers.PostgreSQLContainer;

import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.DatabaseException;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.statement.SqlStatement;

public class IntegrationEnvironment {
	private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;
	
	static {
		Path curPath = new File(".").toPath().toAbsolutePath();
		Path migrationsDirectory = curPath.getParent().getParent().resolve("migrations");
		
		POSTGRES_CONTAINER = new PostgreSQLContainer<>("postgres:latest")
				.withDatabaseName("scrapper")
				.withUsername("postgres")
				.withPassword("password");
				// .withClasspathResourceMapping(migrationsDirectory.toString(), "migrations", BindMode.READ_ONLY);
		POSTGRES_CONTAINER.start();
		
		Connection connection = null;
		try {
			connection = DriverManager.getConnection(POSTGRES_CONTAINER.getJdbcUrl(), POSTGRES_CONTAINER.getUsername(), POSTGRES_CONTAINER.getPassword());
		} catch (NoDriverFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Database database = null;
		try {
			database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Liquibase liquibase = new Liquibase(migrationsDirectory.toString() + "master.xml", new ClassLoaderResourceAccessor(), database);
		try {
			liquibase.update(new Contexts(), new LabelExpression());
		} catch (LiquibaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	void someTest() {
		
	}
}
