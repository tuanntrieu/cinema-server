package com.doan.cinemaserver.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class InvalidException extends RuntimeException {
    private Object[] params;
    private HttpStatus status;

    public InvalidException(String message, Object... params) {
        super(message);
        this.params = params;
        this.status = HttpStatus.BAD_REQUEST;
    }
    public InvalidException( String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
}
