package com.smart.rabbit.payload;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiResponse<T> {
	
	private Boolean success;
	private String message;
	private Integer code;
	private T data;
	private List<String> errors;
	
	public static <T> ApiResponse<T> success(String message, T data) {
		return ApiResponse.<T>builder()
				.success(true)
				.message(message)
				.code(200)
				.data(data)
				.build();
	}
	
	public static <T> ApiResponse<T> failure(String message, List<String> errors, Integer code) {
		return ApiResponse.<T>builder()
				.success(false)
				.message(message)
				.code(code)
				.errors(errors)
				.build();
	}
	
	
	

}
