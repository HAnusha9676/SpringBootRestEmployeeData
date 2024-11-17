package com.sathya.rest.handler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.sathya.rest.exception.EmployeeNotFoundException;
import com.sathya.rest.model.EmployeeErrorResponse;

	@RestControllerAdvice
	public class GlobalExceptionHandler {
		@ExceptionHandler
		public ResponseEntity<EmployeeErrorResponse> exceptionHandling1(EmployeeNotFoundException exception){
			EmployeeErrorResponse employeeErrorResponse=new EmployeeErrorResponse();
			employeeErrorResponse.setLocalDateTime(LocalDateTime.now());
			employeeErrorResponse.setMessage(exception.getMessage());
			employeeErrorResponse.setStatuscode(HttpStatus.NOT_FOUND.value());
			
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(employeeErrorResponse);
		}
	
		
		public ResponseEntity<Map<String,String>> exceptionHandling2(MethodArgumentNotValidException exception){
			Map<String,String> errorMap=new HashMap<>();
			 exception.getBindingResult()
			.getFieldErrors()
			.forEach(error->{
				String name=error.getField();
				String message=error.getDefaultMessage();
				errorMap.put(name,message);
				
				
			});
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(errorMap);
		}

		


	

}
