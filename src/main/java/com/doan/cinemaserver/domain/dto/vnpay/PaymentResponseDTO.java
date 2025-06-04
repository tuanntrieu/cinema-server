package com.doan.cinemaserver.domain.dto.vnpay;

import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResponseDTO {
    private String status;
    private String message;
    private String URL;
    private Integer orderId;
}
