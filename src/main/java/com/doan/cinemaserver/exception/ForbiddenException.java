package com.doan.cinemaserver.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ForbiddenException extends RuntimeException {
    private Object[] params;
    private HttpStatus status;

    public ForbiddenException(String message, Object... params) {
        super(message);
        this.params = params;
        this.status = HttpStatus.FORBIDDEN;
    }
    public ForbiddenException( String message) {
        super(message);
        this.status = HttpStatus.FORBIDDEN;
    }
}
