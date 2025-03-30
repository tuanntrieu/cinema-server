package com.doan.cinemaserver.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class UnauthorizedException extends RuntimeException {
    private Object[] params;
    private HttpStatus status;

    public UnauthorizedException(String message, Object... params) {
        super(message);
        this.params = params;
        this.status = HttpStatus.UNAUTHORIZED;
    }
    public UnauthorizedException( String message) {
        super(message);
        this.status = HttpStatus.UNAUTHORIZED;
    }
}
