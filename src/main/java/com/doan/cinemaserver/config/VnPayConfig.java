package com.doan.cinemaserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
public class VnPayConfig {
    @Value("${vnpay.tmn-code}")
    private String tmnCode;
    @Value("${vnpay.hash-secret}")
    private String hashSecret;
    @Value("${vnpay.pay-url}")
    private String payUrl;
    @Value("${vnpay.return-url}")
    private String returnUrl;
    @Value("${vnpay.version}")
    private String version;
    @Value("${vnpay.command}")
    private String command;
    @Value("${vnpay.orderType}")
    private String orderType;
}
