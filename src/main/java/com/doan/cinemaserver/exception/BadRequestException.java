package com.doan.cinemaserver.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@Getter
@Setter
public class BadRequestException extends RuntimeException {
    private Object[] params;
    private HttpStatus status;

    public BadRequestException(String message, Object... params) {
        super(message);
        this.params = params;
        this.status = HttpStatus.FORBIDDEN;
    }
    public BadRequestException( String message) {
        super(message);
        this.status = HttpStatus.FORBIDDEN;
    }
}
