package ru.khanin.dmitrii.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import ru.khanin.dmitrii.DTO.ApiErrorResponse;
import ru.khanin.dmitrii.exception.*;

@RestControllerAdvice
public class ScrapperExceptionHandler extends ResponseEntityExceptionHandler {
	public static final Logger log = LoggerFactory.getLogger(Logger.class);

	@Override
	protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		if (ex instanceof MissingRequestHeaderException)
			return handleMissingRequestHeaderException((MissingRequestHeaderException) ex);
		return handleExceptionInternal(ex, null, headers, status, request);
	}

	@Override
	protected ResponseEntity<Object> handleMissingPathVariable(MissingPathVariableException ex, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		return handleMissingPathVariableException(ex);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse("Некорректные параметры запроса",
				HttpStatusCode.valueOf(400).toString(), ex.getClass().toString(), ex.getMessage(), stacktrace));
	}

	public ResponseEntity<Object> handleMissingPathVariableException(MissingPathVariableException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse("Некорректные параметры запроса",
				HttpStatusCode.valueOf(400).toString(), ex.getClass().toString(), ex.getMessage(), stacktrace));
	}

	public ResponseEntity<Object> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse("Некорректные параметры запроса",
				HttpStatusCode.valueOf(400).toString(), ex.getClass().toString(), ex.getMessage(), stacktrace));
	}

	@ExceptionHandler(ChatNotExistException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiErrorResponse> handleException(ChatNotExistException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse("Чат не существует",
				HttpStatusCode.valueOf(400).toString(), ex.getClass().toString(), ex.getMessage(), stacktrace));
	}

	@ExceptionHandler(LinkNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<ApiErrorResponse> handleException(LinkNotFoundException ex) {
		List<String> stacktrace = new ArrayList<>();
		Arrays.asList(ex.getStackTrace()).forEach(e -> stacktrace.add(e.toString()));
		return ResponseEntity.badRequest().body(new ApiErrorResponse("Ссылка не найдена",
				HttpStatusCode.valueOf(400).toString(), ex.getClass().toString(), ex.getMessage(), stacktrace));
	}

}