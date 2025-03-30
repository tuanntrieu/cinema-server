package com.doan.cinemaserver.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class NotFoundException extends RuntimeException{

    private Object[] params;
    private HttpStatus status;

    public NotFoundException(String message, Object... params) {
        super(message);
        this.params = params;
        this.status = HttpStatus.NOT_FOUND;
    }
    public NotFoundException( String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }
}
