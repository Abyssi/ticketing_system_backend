package com.isssr.ticketing_system.exception_handler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class InterceptedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();

        List<MethodArgumentFieldError> methodArgumentFieldErrors = bindingResult
                .getFieldErrors()
                .stream()
                .map(fieldError -> new MethodArgumentFieldError(fieldError.getField(), fieldError.getCode(), fieldError.getRejectedValue()))
                .collect(Collectors.toList());

        List<MethodArgumentGlobalError> methodArgumentGlobalErrors = bindingResult
                .getGlobalErrors()
                .stream()
                .map(globalError -> new MethodArgumentGlobalError(globalError.getCode()))
                .collect(Collectors.toList());

        MethodArgumentError methodArgumentError = new MethodArgumentError(methodArgumentFieldErrors, methodArgumentGlobalErrors);

        return new ResponseEntity<>(methodArgumentError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        MissingParameterError missingParameterError = new MissingParameterError(ex.getParameterName(), ex.getMessage());

        return new ResponseEntity<>(missingParameterError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        List<ConstraintViolationError> constraintViolationErrors = ex
                .getConstraintViolations()
                .stream()
                .map(constraintViolation -> new ConstraintViolationError(constraintViolation.getInvalidValue().toString(), constraintViolation.getMessage()))
                .collect(Collectors.toList());
        return new ResponseEntity<>(constraintViolationErrors, HttpStatus.BAD_REQUEST);
    }

    @Data
    @AllArgsConstructor
    public class MethodArgumentError {
        private List<MethodArgumentFieldError> fieldErrors;
        private List<MethodArgumentGlobalError> globalErrors;
    }

    @Data
    @AllArgsConstructor
    public class MethodArgumentFieldError {
        private String field;
        private String code;
        private Object rejectedValue;
    }

    @Data
    @AllArgsConstructor
    public class MethodArgumentGlobalError {
        private String code;
    }

    @Data
    @AllArgsConstructor
    public class MissingParameterError {
        private String parameterName;
        private String message;
    }

    @Data
    @AllArgsConstructor
    public class ConstraintViolationError {
        private String invalidValue;
        private String message;
    }
}