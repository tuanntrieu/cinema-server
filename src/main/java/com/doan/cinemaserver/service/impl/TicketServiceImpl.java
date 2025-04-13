package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SeatType;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.entity.Room;
import com.doan.cinemaserver.domain.entity.Schedule;
import com.doan.cinemaserver.domain.entity.Seat;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.ScheduleRepository;
import com.doan.cinemaserver.repository.SeatRepository;
import com.doan.cinemaserver.repository.TicketRepository;
import com.doan.cinemaserver.service.TicketService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;


}