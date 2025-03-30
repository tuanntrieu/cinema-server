package com.doan.cinemaserver.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class DataIntegrityViolationException extends RuntimeException {
    private Object[] params;
    private HttpStatus status;

    public DataIntegrityViolationException(String message, Object... params) {
        super(message);
        this.params = params;
        this.status = HttpStatus.CONFLICT;
    }
    public DataIntegrityViolationException( String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }
}
