package com.doan.cinemaserver.service;

import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.ticket.OrderRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.TicketRequestDto;
import com.doan.cinemaserver.domain.dto.ticket.TicketResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface TicketService {

    public CommonResponseDto checkOut(OrderRequestDto requestDto);

    public PaginationResponseDto<TicketResponseDto> getTickets(TicketRequestDto requestDto);
}
