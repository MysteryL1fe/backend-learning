package ru.khanin.dmitrii.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import ru.khanin.dmitrii.DTO.ApiErrorResponse;
import ru.khanin.dmitrii.exception.*;

@RestControllerAdvice
public class ScrapperExceptionHandler {
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleException(HttpMessageNotReadableException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse(
				"Некорректные параметры запроса", HttpStatusCode.valueOf(400).toString(),
				ex.getClass().toString(), ex.getMessage(), stacktrace
		));
	}
	
	@ExceptionHandler(MissingPathVariableException.class)
	public ResponseEntity<ApiErrorResponse> exceptionHandle(MissingPathVariableException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse(
				"Некорректные параметры запроса", HttpStatusCode.valueOf(400).toString(),
				ex.getClass().toString(), ex.getMessage(), stacktrace
		));
	}
	
	@ExceptionHandler(ChatNotExistException.class)
	public ResponseEntity<ApiErrorResponse> exceptionHandle(ChatNotExistException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse(
				"Чат не существует", HttpStatusCode.valueOf(400).toString(),
				ex.getClass().toString(), ex.getMessage(), stacktrace
		));
	}
	
	@ExceptionHandler(LinkNotFoundException.class)
	public ResponseEntity<ApiErrorResponse> exceptionHandle(LinkNotFoundException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse(
				"Ссылка не найдена", HttpStatusCode.valueOf(400).toString(),
				ex.getClass().toString(), ex.getMessage(), stacktrace
		));
	}
}