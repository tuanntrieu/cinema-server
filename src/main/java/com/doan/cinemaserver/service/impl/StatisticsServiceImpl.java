package com.doan.cinemaserver.service.impl;

import com.doan.cinemaserver.domain.dto.pagination.PaginationResponseDto;
import com.doan.cinemaserver.domain.dto.statistics.*;
import com.doan.cinemaserver.repository.CustomerRepository;
import com.doan.cinemaserver.repository.StatisticRepository;
import com.doan.cinemaserver.repository.TicketRepository;
import com.doan.cinemaserver.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatisticsServiceImpl implements StatisticsService {

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final StatisticRepository statisticRepository;

    @Override
    public int countCustomerByDate(LocalDate date) {
        return customerRepository.countCustomerByDate(date);
    }

    @Override
    public int countCustomerByWeek(LocalDate date) {
        return customerRepository.countCustomerByWeek(date);
    }

    @Override
    public int countCustomerByMonth(LocalDate date) {
        return customerRepository.countCustomerByMonth(date);
    }

    @Override
    public int countTicketByDate(LocalDate date) {
        return ticketRepository.countTicketByDate(date);
    }

    @Override
    public int countTicketByWeek(LocalDate date) {
        return ticketRepository.countTicketByWeek(date);
    }

    @Override
    public int countTicketByMonth(LocalDate date) {
        return ticketRepository.countTicketByMonth(date);
    }

    @Override
    public long sumTotalByDate(LocalDate date) {
        return ticketRepository.sumTotalByDate(date);
    }

    @Override
    public long sumTotalByWeek(LocalDate date) {
        return ticketRepository.sumTotalByWeek(date);
    }

    @Override
    public long sumTotalByMonth(LocalDate date) {
        return ticketRepository.sumTotalByMonth(date);
    }

    @Override
    public PaginationResponseDto<RevenueMovieResponseDto> getRevenueMovie(RevenueMovieRequestDto requestDto) {
        return statisticRepository.getRevenueMovie(requestDto);
    }

    @Override
    public PaginationResponseDto<RevenueCinemaResponseDto> getRevenueCinema(RevenueCinemaRequestDto requestDto) {
        return statisticRepository.getRevenueCinema(requestDto);
    }

    @Override
    public List<RevenueChartResponseDto> getRevenueChartByMonth(RevenueChartRequestDto request) {
        List<RevenueChartResponseDto> result = new ArrayList<>();
        LocalDate firstDay = request.getDate().withDayOfMonth(1);
        int lastDayOfMonth = request.getDate().lengthOfMonth();
        int week=1;
        for (int day = 1; day <= lastDayOfMonth; day += 7) {
            LocalDate start = firstDay.withDayOfMonth(day);
            LocalDate end = start.plusDays(6);
            if (end.getMonthValue() != start.getMonthValue()) {
                end = firstDay.withDayOfMonth(lastDayOfMonth);
            }
            RevenueChartResponseDto dto = statisticRepository.getRevenueChart(start, end, request.getCinemaId());
            dto.setLabel("Tuần "+week);
            result.add(dto);
            week++;
        }

        return result;
    }

    @Override
    public List<RevenueChartResponseDto> getRevenueChartByYear(RevenueChartRequestDto request) {
        List<RevenueChartResponseDto> result = new ArrayList<>();
        int year = request.getDate().getYear();
        for (int month = 1; month <= 12; month++) {
            LocalDate start = LocalDate.of(year, month, 1);
            LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
            RevenueChartResponseDto dto = statisticRepository.getRevenueChart(start, end, request.getCinemaId());
            dto.setLabel("Tháng "+month);
            result.add(dto);
        }
        return result;
    }



}
