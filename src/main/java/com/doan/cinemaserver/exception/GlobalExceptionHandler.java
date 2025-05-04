package com.doan.cinemaserver.exception;


import com.doan.cinemaserver.common.RestData;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;


@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSourceUtil messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestData<?>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage(), ex);
        StringBuilder errors = new StringBuilder(LocaleContextHolder.getLocale().equals(Locale.ENGLISH)? "Validation errors: ":"Lỗi: ");
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.append(messageSource.getMessage(error.getDefaultMessage(),new String[]{error.getField().toString()}))
                        .append("; ")
        );
        return VsResponseUtil.error(HttpStatus.BAD_REQUEST, errors.toString());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<RestData<?>> handlerInternalServerError(Exception ex) {
        String message = messageSource.getMessage(ErrorMessage.ERR_EXCEPTION_GENERAL, null);
        log.error(message, ex);
        return VsResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<RestData<?>> handlerNotFoundException(NotFoundException ex) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getParams());
        log.error(errorMessage, ex);
        return VsResponseUtil.error(ex.getStatus(), errorMessage);
    }

    @ExceptionHandler(InvalidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<RestData<?>> handlerInvalidException(InvalidException ex) {
        String errorMessage = messageSource.getMessage(ex.getMessage(), ex.getParams());
        log.error(errorMessage, ex);
        return VsResponseUtil.error(ex.getStatus(), errorMessage);
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<RestData<?>> handleAccessDeniedException(ForbiddenException ex) {
        String message = messageSource.getMessage(ex.getMessage(), ex.getParams());
        log.error(message, ex);
        return VsResponseUtil.error(ex.getStatus(), message);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
   // @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<RestData<?>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = messageSource.getMessage(ex.getMessage(), ex.getParams());
        log.error(message, ex);
        return VsResponseUtil.error(ex.getStatus(), message);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<RestData<?>> handleUnauthorizedException(UnauthorizedException ex) {
        String message = messageSource.getMessage(ex.getMessage(), ex.getParams());
        log.error(message, ex);
        return VsResponseUtil.error(ex.getStatus(), message);
    }

    @ExceptionHandler(UploadFileException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity<RestData<?>> handleUploadFileException(UploadFileException ex) {
        String message = messageSource.getMessage(ex.getMessage(), ex.getParams());
        log.error(message, ex);
        return VsResponseUtil.error(ex.getStatus(), message);
    }


}
