package com.doan.cinemaserver.controller;

import com.doan.cinemaserver.common.RestApiV1;
import com.doan.cinemaserver.common.VsResponseUtil;
import com.doan.cinemaserver.constant.UrlConstant;
import com.doan.cinemaserver.domain.dto.ticket.OrderRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.TicketRequestDto;
import com.doan.cinemaserver.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiV1
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @Operation(summary = "API Check Out")
    @PostMapping(UrlConstant.Ticket.CHECKOUT)
    public ResponseEntity<?> checkout(@RequestBody OrderRequestDto requestDto){
        return VsResponseUtil.success(ticketService.checkOut(requestDto));
    }

    @Operation(summary = "API Get Tickets By Customer")
    @PostMapping(UrlConstant.Ticket.GET_TICKETS_BY_CUSTOMER)
    public ResponseEntity<?> checkout(@RequestBody TicketRequestDto requestDto){
        return VsResponseUtil.success(ticketService.getTickets(requestDto));
    }

}
