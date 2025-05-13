package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.vnpay.PaymentResponseDTO;
import com.doan.cinemaserver.domain.dto.vnpay.PaymentStatusResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface VnPayService {
    PaymentResponseDTO createPaymentUrl(Long amount, HttpServletRequest request);

    public PaymentStatusResponse handleVNPayReturn(String receivedHash, HttpServletRequest request);
}
