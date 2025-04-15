package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.ticket.*;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.*;
import com.doan.cinemaserver.service.TicketService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
        Map<Long, SeatStatus> seatMap = schedule.getSeats();
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
                .toList();
        StringBuilder seatsStrBui = new StringBuilder();
        AtomicReference<Long> price = new AtomicReference<>(0L);
        DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
        seats.forEach(seat -> {
            if (!seatMap.get(seat.getId()).equals(SeatStatus.AVAILABLE)) {
                throw new InvalidException(ErrorMessage.Seat.ERR_INVALID_SEAT_TYPE);
            }
            seatsStrBui.append(seat.getSeatName()).append(",");
            seatRepository.updateSeatStatus(schedule.getId(), seat.getId(), SeatStatus.SOLD.toString());
            price.updateAndGet(v -> v + (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY) ? seat.getSeatType().getWeekendPrice() : seat.getSeatType().getWeekdayPrice()) + room.getRoomType().getSurcharge());
        });
        String seatsStr = !seatsStrBui.isEmpty()
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
                .priceSeat(price.get())
                .build();
        ticket.setCreatedDate(LocalDateTime.now());
        ticket.setLastModifiedDate(LocalDateTime.now());
        ticket.setTicketCombo(new ArrayList<>());
        ticketRepository.save(ticket);
        long priceCombo = 0L;
        for (ComboOrderDto combos : requestDto.getCombos()) {
            Combo combo = comboRepository.findById(combos.getComboId()).orElseThrow(
                    () -> new NotFoundException(ErrorMessage.Combo.ERR_NOT_FOUND_COMBO, new String[]{String.valueOf(combos.getComboId())})
            );
            TicketCombo ticketCombo = TicketCombo.builder()
                    .combo(combo)
                    .ticket(ticket)
                    .quantity(combos.getQuantity())
                    .build();
            priceCombo += (combo.getPrice() * combos.getQuantity());
            combo.getTicketCombo().add(ticketCombo);
            comboRepository.save(combo);

            ticketCombo.setTicket(ticket);
            ticketCombo.setCombo(combo);
            ticketCombo.setCurrentPrice(combo.getPrice());
            ticketComboRepository.save(ticketCombo);
            ticket.getTicketCombo().add(ticketCombo);
        }
        ticket.setPriceCombo(priceCombo);
        ticketRepository.save(ticket);

        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }

    @Override
    public PaginationResponseDto<TicketResponseDto> getTickets(TicketRequestDto requestDto) {
        Customer customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Customer.ERR_NOT_FOUND_CUSTOMER, new String[]{requestDto.getCustomerId().toString()}));
        Sort sort = requestDto.getIsAscending() != null && requestDto.getIsAscending()
                ? Sort.by(requestDto.getSortBy()).ascending()
                : Sort.by(requestDto.getSortBy()).descending();
        Pageable pageable = PageRequest.of(requestDto.getPageNo(), requestDto.getPageSize(), sort);
        Page<Ticket> ticketPage = ticketRepository.findAll(
                new Specification<Ticket>() {
                    @Override
                    public Predicate toPredicate(Root<Ticket> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                        List<Predicate> predicates = new ArrayList<>();
                        predicates.add(criteriaBuilder.equal(root.get(Ticket_.CUSTOMER).get(Customer_.ID), customer.getId()));
                        if (requestDto.getDateOrder() != null) {
                            predicates.add(criteriaBuilder.equal(criteriaBuilder.function("DATE", LocalDate.class, root.get(Ticket_.createdDate)), requestDto.getDateOrder()));
                        }
                        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                    }
                }
                , pageable);

        List<TicketResponseDto> ticketResponses = new ArrayList<>();

        ticketPage.getContent().forEach(
                ticket -> {
                    Schedule schedule = ticket.getSchedule();
                    List<ComboTicketResponseDto> ticketComboList = ticket.getTicketCombo().stream()
                            .map(tc -> ComboTicketResponseDto.builder()
                                    .quantity(tc.getQuantity())
                                    .price(tc.getCurrentPrice())
                                    .name(tc.getCombo().getName()+"( "+tc.getCombo().getDescription()+")")
                                    .build())
                            .collect(Collectors.toList());
                    TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                            .id(ticket.getId())
                            .createdDate(ticket.getCreatedDate())
                            .customerName(ticket.getCustomerName())
                            .customerEmail(ticket.getCustomerEmail())
                            .date(schedule.getScheduleTime().toLocalDate())
                            .time(schedule.getScheduleTime().toLocalTime())
                            .seats(ticket.getSeatsName())
                            .movieName(ticket.getMovie().getName())
                            .roomName(schedule.getRoom().getName())
                            .totalSeats(ticket.getPriceSeat())
                            .cinemaName(ticket.getCinemaName())
                            .cinemaAddress(ticket.getAddressCinema())
                            .combo(ticketComboList)
                            .totalCombos(ticket.getPriceCombo())
                            .build();
                    ticketResponses.add(ticketResponseDto);
                }
        );
        PaginationResponseDto<TicketResponseDto> response = new PaginationResponseDto<>(
                ticketPage.getTotalElements(), ticketPage.getTotalPages(), ticketPage.getNumber(), ticketPage.getNumberOfElements(), sort.toString(), ticketResponses
        );

        return response;
    }



}
