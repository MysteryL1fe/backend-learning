package ru.khanin.dmitrii.bot.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ru.khanin.dmitrii.bot.DTO.ApiErrorResponse;

@RestControllerAdvice
public class BotExceptionHandler extends ResponseEntityExceptionHandler {
	public static final Logger log = LoggerFactory.getLogger(Logger.class);
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse("Некорректные параметры запроса",
				HttpStatusCode.valueOf(400).toString(), ex.getClass().toString(), ex.getMessage(), stacktrace));
	}
}
