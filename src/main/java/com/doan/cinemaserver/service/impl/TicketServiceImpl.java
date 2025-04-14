package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.ticket.ComboOrderDto;
import com.doan.cinemaserver.domain.dto.ticket.OrderRequestDto;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.*;
import com.doan.cinemaserver.service.TicketService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final MessageSourceUtil messageSourceUtil;
    private final ScheduleRepository scheduleRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;
    private final CustomerRepository customerRepository;
    private final ComboRepository comboRepository;
    private final TicketComboRepository ticketComboRepository;


    @Override
    @Transactional
    public CommonResponseDto checkOut(OrderRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{String.valueOf(requestDto.getScheduleId())})
        );
        Map<Long,SeatStatus> seatMap = schedule.getSeats();
        Movie movie = movieRepository.findById(requestDto.getMovieId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE, new String[]{String.valueOf(requestDto.getMovieId())})
        );
        Room room = schedule.getRoom();
        Cinema cinema = room.getCinema();

        Customer customer = customerRepository.findById(requestDto.getCustomerId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Customer.ERR_NOT_FOUND_CUSTOMER, new String[]{String.valueOf(requestDto.getCustomerId())})
        );

        List<Seat> seats = Arrays.stream(requestDto.getSeatId())
                .map(id -> seatRepository.findById(id).orElseThrow(
                        () -> new NotFoundException(ErrorMessage.Seat.ERR_NOT_FOUND_SEAT,
                                new String[]{String.valueOf(id)})
                ))
                .collect(Collectors.toList());
        StringBuilder seatsStrBui = new StringBuilder();
        AtomicReference<Long> price = new AtomicReference<>(0L);
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        seats.forEach(seat -> {
            if(!seatMap.get(seat.getId()).equals(SeatStatus.AVAILABLE)){
                throw new InvalidException(ErrorMessage.Seat.ERR_INVALID_SEAT_TYPE);
            }
            seatsStrBui.append(seat.getSeatName()).append(",");
            seatRepository.updateSeatStatus(schedule.getId(), seat.getId(), SeatStatus.SOLD.toString());
            price.updateAndGet(v -> v + (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY) ? seat.getSeatType().getWeekendPrice() : seat.getSeatType().getWeekdayPrice()) + room.getRoomType().getSurcharge());
        });
        String seatsStr = seatsStrBui.length() > 0
                ? seatsStrBui.substring(0, seatsStrBui.length() - 2)
                : "";
        Ticket ticket = Ticket.builder()
                .cinemaName(cinema.getCinemaName())
                .addressCinema(cinema.getDetailAddress() + ", " + cinema.getWard() + ", " + cinema.getDistrict() + ", " + cinema.getProvince())
                .customer(customer)
                .customerName(requestDto.getCustomerName())
                .customerEmail(requestDto.getCustomerEmail())
                .movie(movie)
                .movieName(movie.getName())
                .roomName(room.getName())
                .seatsName(seatsStr)
                .schedule(schedule)
                .build();
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setLastModifiedDate(LocalDateTime.now());
        ticket.setTicketCombo(new ArrayList<>());
        ticketRepository.save(ticket);
        for (ComboOrderDto combos : requestDto.getCombos()) {
            Combo combo = comboRepository.findById(combos.getComboId()).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.Combo.ERR_NOT_FOUND_COMBO, new String[]{String.valueOf(combos.getComboId())})
            );
            TicketCombo ticketCombo = TicketCombo.builder()
                    .combo(combo)
                    .ticket(ticket)
                    .quantity(combos.getQuantity())
                    .build();
            price.updateAndGet(p -> p + (combo.getPrice() * combos.getQuantity()));
            combo.getTicketCombo().add(ticketCombo);
            comboRepository.save(combo);

            ticketCombo.setTicket(ticket);
            ticketCombo.setCombo(combo);
            ticketComboRepository.save(ticketCombo);
            ticket.getTicketCombo().add(ticketCombo);
        }
        ticket.setCurrentPrice(price.get());
        ticketRepository.save(ticket);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }
}