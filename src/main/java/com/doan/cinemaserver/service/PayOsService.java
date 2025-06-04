package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.payos.CreateUrlRequestDto;
import com.doan.cinemaserver.domain.dto.payos.PayOsRequestDto;
import com.doan.cinemaserver.domain.dto.vnpay.PaymentResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public interface PayOsService {
    public PaymentResponseDTO createPaymentUrl(CreateUrlRequestDto requestDto, HttpServletRequest request);
    public void handleWebhook(String rawBody);
}
