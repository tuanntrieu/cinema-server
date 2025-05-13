package com.doan.cinemaserver.domain.dto.vnpay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusResponse {
    private String message;
    private boolean paymentStatus;
}