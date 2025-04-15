package com.doan.cinemaserver.domain.dto.common;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CommonResponseDto {

    private String message;

    private Object data;

    public CommonResponseDto(String message) {
        this.message = message;
        this.data = null;
    }

    public CommonResponseDto(String message, Object data) {
        this.message = message;
        this.data = data;
    }

}
