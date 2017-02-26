package com.rsw.product.exception;

import com.rsw.product.domain.ErrorResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Locale;

/**
 * Created by DAlms on 2/19/17.
 */
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private MessageSource messageSource;

    @Autowired
    public RestExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        BindingResult bindingResult = ex.getBindingResult();
        String responseMessage = resolveMessage(bindingResult.getFieldError());
        ErrorResponse response = new ErrorResponse("BadRequest", responseMessage);
        return handleExceptionInternal(ex, response, headers, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class })
    protected ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        String message = resolveMessage("error.entityNotFound", String.valueOf(ex.getId()));
        ErrorResponse response = new ErrorResponse("NotFound", message);
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = {PermissionDeniedException.class })
    protected ResponseEntity<Object> handleNotFound(PermissionDeniedException ex, WebRequest request) {
    	// TODO Do we want to give the requester more information?
        String message = resolveMessage("error.permissionDenied");
        ErrorResponse response = new ErrorResponse("Permission Denied", message);
        return handleExceptionInternal(ex, response, new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    private String resolveMessage(FieldError error) {
        return resolveMessage(error.getDefaultMessage());
    }

    private String resolveMessage(String messageKey, String... args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(messageKey, args, locale);
    }
}
