package org.splv.evouchers.api.error;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonGetter;

import lombok.Getter;

@Getter
public class APIError {

	private final LocalDateTime timestamp;
	private final HttpStatus status;
	private String key = "";
	private String message = "";
	private List<String> errors = new ArrayList<>();

	public APIError(HttpStatus status, String key, String message, List<String> errors) {
		this.timestamp = LocalDateTime.now();
		this.key = key;
		this.status = status;
		this.message = message;
		this.errors = errors;
	}

	public APIError(HttpStatus status, String key, String message, String error) {
		this(status, key, message, Arrays.asList(error));
	}

	public APIError(HttpStatus status, String key, String message) {
		this(status, key, message, List.of());
	}

	@JsonGetter("status")
	public int getStatusCode() {
		return status.value();
	}
}
