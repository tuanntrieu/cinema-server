package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.payos.CreateUrlRequestDto;
import com.doan.cinemaserver.domain.dto.payos.PayOsRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.DataCacheForOrderRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.OrderRequestDto;
import com.doan.cinemaserver.domain.dto.vnpay.PaymentResponseDTO;
import com.doan.cinemaserver.service.PayOsService;
import com.doan.cinemaserver.service.TicketService;
import com.doan.cinemaserver.util.PaymentUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PayOsServiceImpl implements PayOsService {
    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Value("${payos.api-url}")
    private String apiUrl;

    private final TicketService ticketService;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;

    @Override
    public PaymentResponseDTO createPaymentUrl(CreateUrlRequestDto requestDto, HttpServletRequest request) {
        String orderCode = PaymentUtil.getRandomNumber(8);
        String description = "Thanh toan ve #" + orderCode;
        String rawData = "amount=" + requestDto.getAmount() +
                "&cancelUrl=" + requestDto.getCancelUrl() +
                "&description=" + description +
                "&orderCode=" + orderCode +
                "&returnUrl=" + requestDto.getReturnUrl();
        String signature = PaymentUtil.generateSignature(rawData, checksumKey);
        PayOsRequestDto paymentRequestDTO = PayOsRequestDto.builder()
                .orderCode(Integer.parseInt(orderCode))
                .amount(requestDto.getAmount())
                .description(description)
                .cancelUrl(requestDto.getCancelUrl())
                .returnUrl(requestDto.getReturnUrl())
                .signature(signature)
                .expiredAt((int) (System.currentTimeMillis() / 1000 + 600)) // expires after 15 minute
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("x-client-id", clientId);
        headers.set("x-api-key", apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PayOsRequestDto> entity = new HttpEntity<>(paymentRequestDTO, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(apiUrl, entity, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> body = response.getBody();
            log.warn("PayOS response body: {}", body);
            Object codeObj = body.get("code");
            // PayOS trả về code == 00 khi thành công
            if ("00".equals(String.valueOf(codeObj))) {
                Map<String, Object> data = (Map<String, Object>) body.get("data");
                String checkoutUrl = (String) data.get("checkoutUrl");
                String orderId = String.valueOf(data.get("orderCode"));
                return PaymentResponseDTO.builder()
                        .status("ok")
                        .message("success")
                        .orderId(Integer.parseInt(orderId))
                        .URL(checkoutUrl)
                        .build();
            } else {
                String desc = (String) body.getOrDefault("desc", "Unknown error");
                return PaymentResponseDTO.builder()
                        .status("error")
                        .message("PayOS error: " + desc)
                        .URL(null)
                        .build();
            }
        } else {
            return PaymentResponseDTO.builder()
                    .status("error")
                    .message("Request failed or no response")
                    .URL(null)
                    .build();
        }
    }

    @Override
    public void handleWebhook(String rawBody) {
        if (rawBody == null || rawBody.trim().isEmpty()) {
            log.warn("Received empty or null webhook body");
            return;
        }
        if (rawBody.length() > 10_000) {
            log.warn("Webhook body too large, possible abuse. Size: {}", rawBody.length());
            return;
        }
        log.info("Received PayOS webhook rawBody: {}", rawBody);
        try {
            Map<String, Object> payload = mapper.readValue(rawBody, Map.class);

            if (!payload.containsKey("signature") || !payload.containsKey("data")) {
                log.warn("Webhook payload missing required fields: {}", payload);
                return;
            }
            String signature = (String) payload.get("signature");
            Map<String, Object> data = (Map<String, Object>) payload.get("data");
            // Validate structure of data
            if (data == null) {
                log.warn("Invalid data structure in webhook: {}", data);
                return;
            }
            // Tạo rawData string theo đúng thứ tự key alpha từ data
            List<String> keys = new ArrayList<>(data.keySet());
            Collections.sort(keys);
            StringBuilder rawDataBuilder = new StringBuilder();
            for (int i = 0; i < keys.size(); i++) {
                String key = keys.get(i);
                Object value = data.get(key);
                rawDataBuilder.append(key).append("=").append(value);
                if (i < keys.size() - 1) {
                    rawDataBuilder.append("&");
                }
            }

            String rawData = rawDataBuilder.toString();
            String expectedSignature = PaymentUtil.generateSignature(rawData, checksumKey);

            if (!expectedSignature.equalsIgnoreCase(signature)) {
                log.error("Invalid PayOS webhook signature. Expected: {}, Received: {}. Payload: {}",
                        expectedSignature, signature, payload);
                return;
            }

            if (!data.containsKey("orderCode") || !data.containsKey("code")) {
                log.warn("Missing required fields in webhook data: {}", data);
                return;
            }
            String orderCode = String.valueOf(data.get("orderCode"));
            String responseCode = (String) data.get("code");
            if ("00".equals(responseCode)) {
                DataCacheForOrderRequestDto request = ticketService.readDataOrder(orderCode);
                OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                        .id(request.getVnp_TxnRef())
                        .combos(request.getCombos())
                        .customerEmail(request.getCustomerEmail())
                        .seatId(request.getSeatId())
                        .scheduleId(request.getScheduleId())
                        .customerName(request.getCustomerName())
                        .customerId(request.getCustomerId())
                        .movieId(request.getMovieId())
                        .build();
                CommonResponseDto response = ticketService.checkOut(orderRequestDto);
            } else {
                log.error("That bai");
            }

        } catch (Exception e) {
            log.error("Error while processing PayOS webhook. Exception: ", e);
        }
    }

}
