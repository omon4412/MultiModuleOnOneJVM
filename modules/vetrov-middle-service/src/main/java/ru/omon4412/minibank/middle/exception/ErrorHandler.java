package ru.omon4412.minibank.middle.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice(basePackages = "ru.omon4412.minibank.middle")
public class ErrorHandler {

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.CONFLICT, path);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String path = request.getRequestURI();
        Map<String, Object> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ApiError.from(errors.toString(), HttpStatus.BAD_REQUEST, path);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleNoResourceFoundException(NoResourceFoundException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.NOT_FOUND, path);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiError handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e,
                                                                 HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.METHOD_NOT_ALLOWED, path);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.BAD_REQUEST, path);
    }

    @ExceptionHandler(ServerNotAvailableException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public ApiError handleServerNotAvailableException(ServerNotAvailableException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.SERVICE_UNAVAILABLE, path);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalServerErrorException(InternalServerErrorException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.NOT_FOUND, path);
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(Throwable e, HttpServletRequest request) {
        String path = request.getRequestURI();
        return ApiError.from(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, path);
    }
}
