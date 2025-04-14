package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.ticket.OrderRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface TicketService {

    public CommonResponseDto checkOut(OrderRequestDto requestDto);

}
