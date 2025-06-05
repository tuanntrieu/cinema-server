package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.constant.ErrorMessage;
import com.doan.cinemaserver.constant.SeatStatus;
import com.doan.cinemaserver.constant.SuccessMessage;
import com.doan.cinemaserver.domain.dto.common.CommonResponseDto;
import com.doan.cinemaserver.domain.dto.common.DataMailDto;
import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.ticket.*;
import com.doan.cinemaserver.domain.entity.*;
import com.doan.cinemaserver.exception.BadRequestException;
import com.doan.cinemaserver.exception.InvalidException;
import com.doan.cinemaserver.exception.NotFoundException;
import com.doan.cinemaserver.repository.*;
import com.doan.cinemaserver.service.TicketService;
import com.doan.cinemaserver.util.MessageSourceUtil;
import com.doan.cinemaserver.util.SendMailUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Slf4j
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
    private final RedisTemplate<String, Object> redisTemplate;
    private final SendMailUtil sendMailUtil;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    @Transactional
    public CommonResponseDto checkOut(OrderRequestDto requestDto) {
        Schedule schedule = scheduleRepository.findById(requestDto.getScheduleId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Schedule.ERR_NOT_FOUND_SCHEDULE, new String[]{String.valueOf(requestDto.getScheduleId())})
        );
        Map<Long, ScheduleSeat> seatMap = schedule.getScheduleSeats()
                .stream()
                .collect(Collectors.toMap(ss -> ss.getSeat().getId(), ss -> ss));
        ;
        Movie movie = movieRepository.findById(requestDto.getMovieId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Movie.ERR_NOT_FOUND_MOVIE, new String[]{String.valueOf(requestDto.getMovieId())})
        );
        Room room = schedule.getRoom();
        Cinema cinema = room.getCinema();

        Customer customer = customerRepository.findById(requestDto.getCustomerId()).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Customer.ERR_NOT_FOUND_ID, new String[]{String.valueOf(requestDto.getCustomerId())})
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
            if (!seatMap.get(seat.getId()).getSeatStatus().equals(SeatStatus.HOLDING)) {
                throw new InvalidException(ErrorMessage.Seat.ERR_INVALID_SEAT_STATUS);
            }
            messagingTemplate.convertAndSend("/topic/seat-expired/" + schedule.getId(), seat.getId());
            seatsStrBui.append(seat.getSeatName()).append(",");
            seatRepository.updateSeatStatus(schedule.getId(), seat.getId(), SeatStatus.SOLD.toString());
            price.updateAndGet(v -> v + (dayOfWeek.equals(DayOfWeek.SATURDAY) || dayOfWeek.equals(DayOfWeek.SUNDAY) ? seat.getSeatType().getWeekendPrice() : seat.getSeatType().getWeekdayPrice()) + room.getRoomType().getSurcharge());
        });
        String seatsStr = !seatsStrBui.isEmpty()
                ? seatsStrBui.substring(0, seatsStrBui.length() - 1)
                : "";
        Ticket ticket = Ticket.builder()
                .id(requestDto.getId())
                .cinemaName(cinema.getCinemaName())
                .addressCinema(cinema.getDetailAddress() + ", " + cinema.getWard() + ", " + cinema.getDistrict() + ", " + cinema.getProvince())
                .customer(customer)
                .customerName(requestDto.getCustomerName())
                .customerEmail(requestDto.getCustomerEmail())
                .movie(movie)
                .scheduleTime(schedule.getScheduleTime())
                .movieName(movie.getName())
                .roomName(room.getName())
                .seatsName(seatsStr)
                .schedule(schedule)
                .priceSeat(price.get())
                .cinema(cinema)
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
        deleteDataCache(requestDto.getId());
        messagingTemplate.convertAndSend("/topic/seat-expired/" + schedule.getId(), requestDto.getSeatId());
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        DataMailDto mailDto = new DataMailDto();
        mailDto.setTo(requestDto.getCustomerEmail());
        mailDto.setSubject("Đặt vé thành công");
        Map<String, Object> properties = new HashMap<>();
        properties.put("movieName", ticket.getMovieName());
        properties.put("cinemaName", ticket.getCinemaName());
        properties.put("cinemaAddress", ticket.getAddressCinema());
        properties.put("id", ticket.getId());
        properties.put("date", schedule.getScheduleTime().toLocalDate().format(dateFormatter));
        properties.put("time", schedule.getScheduleTime().toLocalTime().format(timeFormatter));
        properties.put("roomName", ticket.getRoomName());
        properties.put("seats", ticket.getSeatsName());
        properties.put("createdDate", ticket.getCreatedDate().format(dateTimeFormatter));
        properties.put("totalCombos", ticket.getPriceCombo().toString() + " VND");
        properties.put("totalSeats", ticket.getPriceSeat().toString() + " VND");
        properties.put("total", String.valueOf(ticket.getPriceSeat() +ticket.getPriceCombo())+ " VND");
        mailDto.setProperties(properties);
        try {
            sendMailUtil.sendEmailWithHTML(mailDto, "ticket.html");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }

    @Override
    public PaginationResponseDto<TicketResponseDto> getTickets(TicketRequestDto requestDto) {
        Customer customer = customerRepository.findById(requestDto.getCustomerId())
                .orElseThrow(() -> new NotFoundException(ErrorMessage.Customer.ERR_NOT_FOUND_ID, new String[]{requestDto.getCustomerId().toString()}));
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
                    TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                            .id(ticket.getId())
                            .createdDate(ticket.getCreatedDate())
                            .customerName(ticket.getCustomerName())
                            .customerEmail(ticket.getCustomerEmail())
                            .date(ticket.getScheduleTime().toLocalDate())
                            .time(ticket.getScheduleTime().toLocalTime())
                            .seats(ticket.getSeatsName())
                            .movieName(ticket.getMovie().getName())
                            .roomName(schedule.getRoom().getName())
                            .totalSeats(ticket.getPriceSeat())
                            .cinemaName(ticket.getCinemaName())
                            .cinemaAddress(ticket.getAddressCinema())
                            .totalCombos(ticket.getPriceCombo())
                            .build();
                    ticketResponses.add(ticketResponseDto);
                }
        );
        PaginationResponseDto<TicketResponseDto> response = new PaginationResponseDto<>(
                ticketPage.getTotalElements(), ticketPage.getTotalPages(), ticketPage.getNumber(), requestDto.getPageSize(), sort.toString(), ticketResponses
        );

        return response;
    }

    @Override
    public CommonResponseDto saveDataTmp(DataCacheForOrderRequestDto requestDto) {
        try {
            if (requestDto != null) {
                ObjectMapper mapper = new ObjectMapper();
                String json = mapper.writeValueAsString(requestDto);
                redisTemplate.opsForValue().set(requestDto.getVnp_TxnRef(), json, Duration.ofMinutes(10));
            }
        } catch (JsonProcessingException e) {
            throw new BadRequestException(ErrorMessage.ERR_EXCEPTION_GENERAL);
        }
        return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.CREATE_SUCCESS, null));
    }
    @Override
    public DataCacheForOrderRequestDto readDataOrder(String vnp_TxnRef) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String json = (String) redisTemplate.opsForValue().get(vnp_TxnRef);
            if (json == null) throw new BadRequestException(ErrorMessage.Payment.ERR_PAYMENT_TIMEOUT);
            return mapper.readValue(json, DataCacheForOrderRequestDto.class);
        } catch (JsonProcessingException e) {
            throw new BadRequestException(ErrorMessage.ERR_EXCEPTION_GENERAL);
        }
    }
    @Override
    public CommonResponseDto deleteDataCache(String vnp_TxnRef) {
        Boolean existed = redisTemplate.hasKey(vnp_TxnRef);
        if (Boolean.TRUE.equals(existed)) {
            redisTemplate.delete(vnp_TxnRef);
            return new CommonResponseDto(messageSourceUtil.getMessage(SuccessMessage.DELETE_SUCCESS, null));
        } else {
            throw new BadRequestException(ErrorMessage.ERR_EXCEPTION_GENERAL);
        }
    }

    @Override
    public boolean existById(String vnp_TxnRef) {
        return ticketRepository.existsById(vnp_TxnRef);
    }

    @Override
    public TicketResponseDto getTicketDetail(String id) {
        Ticket ticket = ticketRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ErrorMessage.Ticket.ERR_NOT_FOUND_TICKET)
        );
        Schedule schedule = ticket.getSchedule();
        TicketResponseDto ticketResponseDto = TicketResponseDto.builder()
                .id(ticket.getId())
                .createdDate(ticket.getCreatedDate())
                .customerName(ticket.getCustomerName())
                .customerEmail(ticket.getCustomerEmail())
                .date(ticket.getScheduleTime().toLocalDate())
                .time(ticket.getScheduleTime().toLocalTime())
                .seats(ticket.getSeatsName())
                .movieName(ticket.getMovie().getName())
                .roomName(schedule.getRoom().getName())
                .totalSeats(ticket.getPriceSeat())
                .cinemaName(ticket.getCinemaName())
                .cinemaAddress(ticket.getAddressCinema())
                .totalCombos(ticket.getPriceCombo())
                .build();

        return ticketResponseDto;
    }


}
