package com.doan.cinemaserver.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class UploadFileException extends RuntimeException {
    private Object[] params;
    private HttpStatus status;

    public UploadFileException(String message, Object... params) {
        super(message);
        this.params = params;
        this.status = HttpStatus.BAD_GATEWAY;
    }
    public UploadFileException( String message) {
        super(message);
        this.status = HttpStatus.BAD_GATEWAY;
    }
}
