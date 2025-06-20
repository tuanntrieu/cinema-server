package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.payos.CreateUrlRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.DataCacheForOrderRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.OrderRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.TicketRequestDto;
import com.doan.cinemaserver.service.PayOsService;
import com.doan.cinemaserver.service.TicketService;
import com.doan.cinemaserver.service.VnPayService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestApiV1
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
    private final VnPayService vnPayService;
    private final PayOsService payOsService;

    @Operation(summary = "API Check Out")
    @PostMapping(UrlConstant.Ticket.CHECKOUT)
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDto requestDto) {
        return VsResponseUtil.success(ticketService.checkOut(requestDto));
    }

    @Operation(summary = "API Get Tickets By Customer")
    @PostMapping(UrlConstant.Ticket.GET_TICKETS_BY_CUSTOMER)
    public ResponseEntity<?> getTicketsByCustomer(@RequestBody TicketRequestDto requestDto) {
        return VsResponseUtil.success(ticketService.getTickets(requestDto));
    }

    @Operation(summary = "API Get Payment Url")
    @GetMapping(UrlConstant.Ticket.GET_PAYMENT_URL)
    public ResponseEntity<?> getPaymentUrl(@RequestParam("amount") Long amount, HttpServletRequest request) {
        return VsResponseUtil.success(vnPayService.createPaymentUrl(amount, request));
    }

    @Operation(summary = "API Handle Return")
    @GetMapping(UrlConstant.Ticket.HANDLE_VNPAY_RETURN)
    public ResponseEntity<?> handleReturn(@RequestParam("vnp_SecureHash") String vnp_SecureHash, HttpServletRequest request) {
        return VsResponseUtil.success(vnPayService.handleVNPayReturn(vnp_SecureHash, request));
    }

    @Operation(summary = "API Save Data Tmp")
    @PostMapping(UrlConstant.Ticket.SAVE_DATA_TMP)
    public ResponseEntity<?> saveDataTmp(@RequestBody DataCacheForOrderRequestDto request) {
        return VsResponseUtil.success(ticketService.saveDataTmp(request));
    }

    @Operation(summary = "API Read Data Tmp")
    @GetMapping(UrlConstant.Ticket.READ_DATA_TMP)
    public ResponseEntity<?> readData(@RequestParam("vnp_TxnRef") String vnp_TxnRef) {
        return VsResponseUtil.success(ticketService.readDataOrder(vnp_TxnRef));
    }

    @Operation(summary = "API Ticket Exist By Id")
    @GetMapping(UrlConstant.Ticket.EXIST_BY_ID)
    public ResponseEntity<?> existById(@RequestParam("vnp_TxnRef") String vnp_TxnRef) {
        return VsResponseUtil.success(ticketService.existById(vnp_TxnRef));
    }

    @Operation(summary = "API Get Ticket By Id")
    @GetMapping(UrlConstant.Ticket.GET_TICKET_DETAIL)
    public ResponseEntity<?> getTicketDetail(@PathVariable String id) {
        return VsResponseUtil.success(ticketService.getTicketDetail(id));
    }
    @Operation(summary = "API Get PayOS Payment Url")
    @PostMapping(UrlConstant.Ticket.GET_PAYOS_PAYMENTURL)
    public ResponseEntity<?> getPayOsPaymentUrl(@RequestBody CreateUrlRequestDto requestDto, HttpServletRequest request) {
        return VsResponseUtil.success(payOsService.createPaymentUrl(requestDto, request));
    }

    @Operation(summary = "API Get PayOS Payment Url")
    @PostMapping(UrlConstant.Ticket.HANDLE_WEBHOOK)
    public ResponseEntity<?> handleWebHook(@RequestBody String rawBody) {
        payOsService.handleWebhook(rawBody);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        return VsResponseUtil.success(response);
    }

}
