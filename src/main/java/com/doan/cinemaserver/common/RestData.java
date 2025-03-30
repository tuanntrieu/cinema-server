package com.doan.cinemaserver.common;


import com.doan.cinemaserver.constant.RestStatus;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RestData<T> {
    private RestStatus status;

    private int statusCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public RestData(T data) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
        this.statusCode = 200;
    }

    public RestData(int statusCode,T data) {
        this.status = RestStatus.SUCCESS;
        this.data = data;
        this.statusCode = statusCode;
    }

    public static RestData<?> error(int statusCode,Object message) {
        return new RestData<>(RestStatus.ERROR,statusCode, message, null);
    }
}